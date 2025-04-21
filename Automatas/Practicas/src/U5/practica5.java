package U5;

import U4.infoPalabra;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class practica5 {

    static String entadaDeTokens = "C:/Users/Mauro/Desktop/.A/Automatas/src/U4/Tabla de Tokens.txt";
    static LinkedList<infoPalabra> listaTokens = new LinkedList<>();


    /***
     * Como dice su nombre abre el archivo y verificar que al menos tengan una linea
     * si esta vacio manda el mensaje de error
     */
    private static boolean VerificarContenido() {
        try {
            FileReader fr = new FileReader(entadaDeTokens);
            BufferedReader br = new BufferedReader(fr);
            boolean tieneContenido = br.readLine() != null;
            br.close();
            return tieneContenido;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al leer el archivo");
            return false;
        }
    }

    private static boolean leerArchivo() {
        if (!VerificarContenido()) {
            JOptionPane.showMessageDialog(null, "Error, archivo vació");
            return false;
        }
        try {
            FileReader fr = new FileReader(entadaDeTokens);
            BufferedReader br = new BufferedReader(fr);
            String linea;
            int numLinea = 0;
            while ((linea = br.readLine()) != null) {
                numLinea++;
                logicaLectura(linea, numLinea);
            }
            br.close();
            JOptionPane.showMessageDialog(null, "Archivo leido corretcamente");
            return true;
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error al leer el archivo");
            return false;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al leer el archivo");
            return false;
        }
    }
    private static void logicaLectura(String linea, int numLinea) {
        // dividimos por tab (o por cualquier cantidad de espacios en blanco)
        String[] partes = linea.split("\\t+");
        if (partes.length < 4) {
            System.err.println("Línea " + numLinea + " mal formateada: " + linea);
            return;
        }

        String lexema = partes[0];
        int token, identificador, lineaSintactico;
        try {
            token          = Integer.parseInt(partes[1]);
            identificador  = Integer.parseInt(partes[2]);
            lineaSintactico= Integer.parseInt(partes[3]);
        } catch (NumberFormatException e) {
            System.err.println("Error numérico en línea " + numLinea + ": " + e.getMessage());
            return;
        }

        // Construye el objeto y añádelo a la lista
        listaTokens.add(new infoPalabra(
                lexema, token, identificador, /*pos*/ 0, /*línea sintáctica*/ lineaSintactico
        ));
    }


    public static int inicio(LinkedList<infoPalabra> listaTokens, int numLinea) throws validarException {
       numLinea = programa(listaTokens, numLinea);
       numLinea = bloquePrincipal(listaTokens, numLinea);
        return numLinea;
    }

    public static int programa(LinkedList<infoPalabra> listaTokens, int numLinea) throws validarException {
        //Palabra program
        if (listaTokens.get(numLinea).getToken() == -1) {
            numLinea++;
            //El identificador
            if (listaTokens.get(numLinea).getToken() == -51) {
                numLinea++;
                //El ;
                if (listaTokens.get(numLinea).getToken() == -73) {
                    numLinea++;
                    return numLinea;
                }
                throw new validarException("Error en la linea: " + listaTokens.get(numLinea).getLinea() + " .Falta un ;");
            }
            throw new validarException("Error en la linea: " + listaTokens.get(numLinea).getLinea() + " . Se esperaba un identificador");
        }
        throw new validarException("Error en la linea: " + listaTokens.get(numLinea).getLinea()+ " . Se esperaba la palabra program");
    }

    public static int bloquePrincipal(LinkedList<infoPalabra> listaTokens,
                                      int numLinea) throws validarException {
        // 1) Varías declaraciones
        numLinea = declaracionDeVariables(listaTokens, numLinea);

        // 2) Subprogramas (ε si no hay)
        numLinea = declaracionesSubprograma(listaTokens, numLinea);

        // 3) 'begin'
        if (listaTokens.get(numLinea).getToken() != -2) {  // -2 = begin
            throw new validarException(
                    "Error en la línea " + listaTokens.get(numLinea).getLinea() +
                            ": se esperaba 'begin'");
        }
        numLinea++;

        // 4) Instrucciones
        numLinea = instrucciones(listaTokens, numLinea);

        // 5) 'end'
        if (listaTokens.get(numLinea).getToken() != -3) {
            throw new validarException(
                    "Error en la línea " + listaTokens.get(numLinea).getLinea() +
                            ": se esperaba 'end'");
        }
        numLinea++;

        return numLinea;
    }


    public static int declaracionDeVariables(LinkedList<infoPalabra> listaTokens,
                                             int numLinea) throws validarException {
        // compruebo var
        if (listaTokens.get(numLinea).getToken() != -17) {
            throw new validarException(
                    "Error en la línea " + listaTokens.get(numLinea).getLinea() +
                            ": se esperaba 'var'");
        }
        numLinea++;                        // consumo 'var'

        // delego en la lista recursiva de declaraciones
        return listaDeclaraciones(listaTokens, numLinea);
    }

    public static int listaDeclaraciones(LinkedList<infoPalabra> listaTokens,
                                         int numLinea) throws validarException {
        // 1) Parseo una declaración
        numLinea = declaracion(listaTokens, numLinea);

        // 2) Espero el ';'
        if (listaTokens.get(numLinea).getToken() != -73) {
            throw new validarException(
                    "Error en la línea " + listaTokens.get(numLinea).getLinea() +
                            ": se esperaba ';' tras declaración");
        }
        numLinea++;  // consumo ;

        // 3) Si tras ese ';' viene un identificador, hay otra declaración
        if (numLinea < listaTokens.size() &&
                listaTokens.get(numLinea).getToken() == -51) {  // identificador
            numLinea = listaDeclaraciones(listaTokens, numLinea);
        }

        return numLinea;
    }


    public static int declaracion(LinkedList<infoPalabra> listaTokens, int numLinea) throws validarException {
        // 1) Lista de identificadores
        numLinea = listaIdentificadores(listaTokens, numLinea);

        // 2) Primera alternativa: ':' <tipo>
        if (listaTokens.get(numLinea).getToken() == -75) {
            numLinea++;                             // consume ':'
            return tipo(listaTokens, numLinea);     // devuelve tras <tipo>
        }
        // 3) Segunda alternativa: 'array' '[' listaIndices ']' ':' <tipo>
        else if (listaTokens.get(numLinea).getToken() == -20) {
            numLinea++;  // consume 'array'

            // 3.1) '['
            if (listaTokens.get(numLinea).getToken() != -76) {
                throw new validarException(
                        "Error en la línea " + listaTokens.get(numLinea).getLinea() +
                                ": Falta '[' tras 'array'");
            }
            numLinea++;

            // 3.2) listaIndices
            numLinea = listaIndices(listaTokens, numLinea);

            // 3.3) ']'
            if (listaTokens.get(numLinea).getToken() != -77) {
                throw new validarException(
                        "Error en la línea " + listaTokens.get(numLinea).getLinea() +
                                ": Falta ']'");
            }
            numLinea++;

            // 3.4) ':' y <tipo>
            if (listaTokens.get(numLinea).getToken() != -75) {
                throw new validarException(
                        "Error en la línea " + listaTokens.get(numLinea).getLinea() +
                                ": Falta ':' tras declaración de array");
            }
            numLinea++;

            return tipo(listaTokens, numLinea);
        }
        // 4) Ninguna alternativa coincide
        else {
            throw new validarException(
                    "Error en la línea " + listaTokens.get(numLinea).getLinea() +
                            ": Se esperaba ':' o 'array'");
        }
    }

    public static int listaIdentificadores(LinkedList<infoPalabra> listaTokens, int numLinea) throws validarException {
        // Verifica que haya al menos un identificador
        if (listaTokens.get(numLinea).getToken() != -51) {
            throw new validarException(
                    "Error en la línea " + listaTokens.get(numLinea).getLinea() +
                            ": Se esperaba un identificador");
        }

        numLinea++; // Avanzamos tras el primer identificador

        // Mientras haya comas, espera otro identificador
        while (numLinea < listaTokens.size() && listaTokens.get(numLinea).getToken() == -74) {
            numLinea++; // consume la coma

            // Validar que después de la coma venga otro identificador
            if (numLinea >= listaTokens.size() || listaTokens.get(numLinea).getToken() != -51) {
                throw new validarException(
                        "Error en la línea " + listaTokens.get(numLinea - 1).getLinea() +
                                ": Se esperaba un identificador después de la coma");
            }

            numLinea++; // consume el identificador
        }

        return numLinea;
    }


    public static int listaIndices(LinkedList<infoPalabra> listaTokens, int numLinea) throws validarException {
        if (listaTokens.get(numLinea).getToken() == -61) {
            numLinea++;
            if (listaTokens.get(numLinea).getToken() == -74) {
                numLinea++;
                if (listaTokens.get(numLinea).getToken() == -61) {
                    numLinea++;
                    return numLinea;
                }
                throw new validarException("Error en la linea:  " + listaTokens.get(numLinea).getLinea() + "se esperaba un numero entero");
            }
            throw new validarException("Error en la linea: " + listaTokens.get(numLinea).getLinea() + "falta una coma");
        }
        throw new validarException("Error en la linea:  " + listaTokens.get(numLinea).getLinea() + "se esperaba un numero entero");
    }

    public static int tipo(LinkedList<infoPalabra> listaTokens, int numLinea) throws validarException {
        if (listaTokens.get(numLinea).getToken() == -6) {
            numLinea++;
            return numLinea;
        } else if (listaTokens.get(numLinea).getToken() == -7) {
            numLinea++;
            return numLinea;
        } else if (listaTokens.get(numLinea).getToken() == -8) {
            numLinea++;
            return numLinea;
        } else if (listaTokens.get(numLinea).getToken() == -9) {
            numLinea++;
            return numLinea;
        }
        throw new validarException("Error en la linea: " + listaTokens.get(numLinea).getLinea() + "se esperaba un tipo de dato");
    }
// Aquí acaba el bloque declaracion de variables
// y comienza el bloque de declaracion de subprograma

    public static int declaracionesSubprograma(LinkedList<infoPalabra> listaTokens,
                                               int numLinea) throws validarException {
        // Verificamos si hay tokens restantes
        if (numLinea >= listaTokens.size()) {
            return numLinea;
        }

        int tok = listaTokens.get(numLinea).getToken();

        // Si no empieza con 'procedure' o 'function', regresamos (ε)
        if (tok != -18 && tok != -19) {
            return numLinea;
        }

        // Procesamos un subprograma
        numLinea = subprograma(listaTokens, numLinea);

        // Y llamamos recursivamente para buscar más subprogramas
        return declaracionesSubprograma(listaTokens, numLinea);
    }


    public static int subprograma(LinkedList<infoPalabra> listaTokens,
                                  int numLinea) throws validarException {
         int copiaLinea = numLinea;
        int tok = listaTokens.get(copiaLinea).getToken();

        if (tok == -18 ) {
            return procedimientos(listaTokens, copiaLinea);
        } else if (tok == -19) {
            return funcion(listaTokens, copiaLinea);
        }
        return copiaLinea;
    }



    public static int procedimientos(LinkedList<infoPalabra> listaTokens, int numLinea) throws validarException {
        // 1) Verificar que el primer token sea 'procedure'
        if (listaTokens.get(numLinea).getToken() != -18) {
            throw new validarException("Se esperaba la palabra 'procedure' en la línea "
                    + listaTokens.get(numLinea).getLinea());
        }
        numLinea++;  // consumir 'procedure'

        // 2) Verificar identificador
        if (listaTokens.get(numLinea).getToken() != -51) {
            throw new validarException("Se esperaba el nombre del procedimiento (identificador) en la línea "
                    + listaTokens.get(numLinea).getLinea());
        }
        numLinea++;  // consumir identificador

        // 3) Verificar apertura de paréntesis "("
        if (listaTokens.get(numLinea).getToken() != -71) {
            throw new validarException("Se esperaba '(' después del identificador en la línea "
                    + listaTokens.get(numLinea).getLinea());
        }
        numLinea++;  // consumir "("

        // 4) Llamar a <parametros>
        numLinea = parametros(listaTokens, numLinea);  // este método lo defines tú

        // 5) Verificar cierre de paréntesis ")"
        if (listaTokens.get(numLinea).getToken() != -72) {
            throw new validarException("Se esperaba ')' al final de los parámetros en la línea "
                    + listaTokens.get(numLinea).getLinea());
        }
        numLinea++;  // consumir ")"

        // 6) Verificar punto y coma ";"
        if (listaTokens.get(numLinea).getToken() != -73) {
            throw new validarException("Se esperaba ';' después de ')' en la línea "
                    + listaTokens.get(numLinea).getLinea());
        }
        numLinea++;  // consumir ";"

        // 7) Llamar a <cuerpoSubprograma>
        numLinea = cuerpoSubprograma(listaTokens, numLinea);  // este también lo defines tú

        return numLinea;
    }

    public static int funcion(LinkedList<infoPalabra> listaTokens, int numLinea) throws validarException {
        // function
        if (listaTokens.get(numLinea).getToken() == -19) {
            numLinea++;
            // identificador
            if (listaTokens.get(numLinea).getToken() == -51) {
                numLinea++;
                // (
                if (listaTokens.get(numLinea).getToken() == -71) {
                   numLinea++;
                    // parámetros (pueden ser vacíos, o tener lista de parámetros)
                    numLinea = parametros(listaTokens, numLinea);
                    // )
                    if (listaTokens.get(numLinea).getToken() == -72) {
                       numLinea++;
                        // :
                        if (listaTokens.get(numLinea).getToken() == -75) {
                           numLinea++;
                            // tipo
                            numLinea = tipo(listaTokens, numLinea);
                            // cuerpo de la función
                            numLinea = cuerpoSubprograma(listaTokens, numLinea);
                            return numLinea;
                        }else { throw new validarException("Se esperaba ':' para el tipo de retorno");}
                    }else{ throw new validarException("Se esperaba ')' tras los parámetros de la función");}
                }else{ throw new validarException("Se esperaba '(' tras el identificador de la función");}
            }else {throw new validarException("Se esperaba un identificador tras 'function'");}
        }else {  throw new validarException("Se esperaba la palabra 'function'");}
    }

    public static int cuerpoSubprograma(LinkedList<infoPalabra> listaTokens, int numLinea) throws validarException {
        if (listaTokens.get(numLinea).getToken() == -17) {
            numLinea++;
            numLinea = declaracion(listaTokens, numLinea);
            if (listaTokens.get(numLinea).getToken() == -73) {
                numLinea++;
                if (listaTokens.get(numLinea).getToken() == -2) {
                    numLinea++;
                    numLinea = instrucciones(listaTokens, numLinea);
                    if (listaTokens.get(numLinea).getToken() == -3) {
                        numLinea++;
                        if (listaTokens.get(numLinea).getToken() == -73) {
                            numLinea++;
                            return numLinea;
                        }
                        throw new validarException("Error en la linea: " + listaTokens.get(numLinea).getLinea() + " se esperaba un ;");
                    }
                    throw new validarException("Error en la linea: " + listaTokens.get(numLinea).getLinea() + " se esperaba la palabra end");
                }
                throw new validarException("Error en la linea: " + listaTokens.get(numLinea).getLinea() + " se esperaba un begin");

            }else {throw new validarException("Error en la linea: " + listaTokens.get(numLinea).getLinea() + " se esperaba un ;");}

        }
        throw new validarException("Error en la linea: " + listaTokens.get(numLinea).getLinea() + " se esperaba un var");
    }

    public static int parametros(LinkedList<infoPalabra> listaTokens, int numLinea) throws validarException {
        // Si no hay parámetro, retornamos directamente (ε)
        if (listaTokens.get(numLinea).getToken() != -51) {
            return numLinea;
        }

        // Si hay, seguimos normalmente
        numLinea++; // identificador
        if (listaTokens.get(numLinea).getToken() != -75) {
            throw new validarException("Se esperaba ':' después del parámetro");
        }
        numLinea++;
        numLinea = tipo(listaTokens, numLinea);

        if (listaTokens.get(numLinea).getToken() == -74) {  // otra coma
            numLinea++;
            return parametros(listaTokens, numLinea);
        }

        return numLinea;
    }


    public static int instrucciones(LinkedList<infoPalabra> listaTokens,
                                    int numLinea) throws validarException {
        // Mientras podamos reconocer una instrucción seguida de ';', la consumimos
        while (true) {
            // 1) Lee una instrucción
            numLinea = instruccion(listaTokens, numLinea);
            // 2) Asegúrate de que venga el ';'
            if (listaTokens.get(numLinea).getToken() != -73) {
                throw new validarException(
                        "Error en la línea " + listaTokens.get(numLinea).getLinea() +
                                ": se esperaba ';' tras instrucción");
            }
            numLinea++;  // consume ';'

            // 3) Mira si la siguiente posición **puede** ser el comienzo
            //    de otra instrucción (identificador, if, while, read, write, etc.).
            //    Como no tienes un método tipo esInicioDeInstruccion(),
            //    usa try/catch pero sobre una copia y **avanzada**:
            int prueba = numLinea;
            try {
                prueba = instruccion(listaTokens, prueba);
                // si llegamos aquí y no saltó excepción, hay otra instrucción:
                continue;  // volvemos al while para consumirla
            } catch (validarException e) {
                // no era una instrucción válida → salimos del bucle
                break;
            }
        }
        return numLinea;
    }


    public static int instruccion(LinkedList<infoPalabra> listaTokens, int numLinea) throws validarException {
        int copiaLinea = numLinea;

        // 1. Intentar asignación
        try {

            return asignacion(listaTokens, copiaLinea);
        } catch (validarException e) {

        }

        // 2. Intentar llamada a función o procedimiento
        try {
            return llamada_funcion_o_proc(listaTokens, copiaLinea);
        } catch (validarException e) {
        }

        // 3. Intentar condicional if
        try {
            return condicional_if(listaTokens, copiaLinea);
        } catch (validarException e) {
        }

        // 4. Intentar bucle while
        try {
            return bucle_while(listaTokens, copiaLinea);
        } catch (validarException e) {
        }

        // 5. Intentar bucle repeat
        try {
            return bucle_repeat(listaTokens, copiaLinea);
        } catch (validarException e) {
        }

        // 6. Intentar entrada/salida
        try {
            return entrada_salida(listaTokens, copiaLinea);
        } catch (validarException e) {
        }

        // Si ninguna instrucción fue válida
        throw new validarException("Error en la línea " + listaTokens.get(numLinea).getLinea() + ": instrucción no válida");
    }

    public static int asignacion(LinkedList<infoPalabra> listaTokens,
                                 int pos) throws validarException {
        // 1) Izquierda: variable (identificador o arreglo)
        pos = variable(listaTokens, pos);

        // 2) Compruebo el '=' en la misma posición actual
        if (listaTokens.get(pos).getToken() != -32) {
            throw new validarException(
                    "Error en la línea " + listaTokens.get(pos).getLinea() +
                            ": se esperaba el operador '=' para asignación");
        }
        pos++;  // consumimos '='

        // 3) El resto ha de ser una expresión
        pos = expresion(listaTokens, pos);

        // 4) Devolvemos la posición justo antes del ';'
        return pos;
    }


    public static int variable(LinkedList<infoPalabra> listaTokens, int copiaLinea) throws validarException {
        if (listaTokens.get(copiaLinea).getToken() == -51) { // identificador
            copiaLinea++;

            // ¿Es una variable con índice? Es decir, ¿hay un '['?
            if (copiaLinea < listaTokens.size() && listaTokens.get(copiaLinea).getToken() == -76) { // '['
                copiaLinea++;
                copiaLinea = listaExpresiones(listaTokens, copiaLinea);

                if (listaTokens.get(copiaLinea).getToken() == -77) { // ']'
                    copiaLinea++;
                    return copiaLinea;
                } else {
                    throw new validarException("Error en la línea " + listaTokens.get(copiaLinea).getLinea() + ": se esperaba ']'");
                }
            }

            // Solo fue <identificador>
            return copiaLinea;
        }

        throw new validarException("Error en la línea " + listaTokens.get(copiaLinea).getLinea() + ": se esperaba un identificador");
    }

    public static int llamada_funcion_o_proc(LinkedList<infoPalabra> listaTokens, int copiaLinea) throws validarException {
        if (listaTokens.get(copiaLinea).getToken() == -51) {
            copiaLinea++;
            if (listaTokens.get(copiaLinea).getToken() == -71) {
                copiaLinea++;
                copiaLinea = listaExpresiones(listaTokens, copiaLinea);
                if (listaTokens.get(copiaLinea).getToken() == -72) {
                    copiaLinea++;
                    return copiaLinea;
                } else {
                    throw new validarException("Error en la linea: " + listaTokens.get(copiaLinea).getLinea() + " se esperaba un )");
                }
            } else {
                throw new validarException("Error en la linea: " + listaTokens.get(copiaLinea).getLinea() + " se esperaba un (");
            }
        }
        throw new validarException("Error en la linea: " + listaTokens.get(copiaLinea).getLinea() + " se esperaba un identificador");
    }

    public static int condicional_if(LinkedList<infoPalabra> listaTokens, int copiaLinea) throws validarException {
        if (listaTokens.get(copiaLinea).getToken() == -10) {
            copiaLinea++;
            if (listaTokens.get(copiaLinea).getToken() == -71) {
                copiaLinea++;
                copiaLinea = expresion(listaTokens, copiaLinea);
                if (listaTokens.get(copiaLinea).getToken() == -72) {
                    copiaLinea++;
                    if (listaTokens.get(copiaLinea).getToken() == -12) {
                        copiaLinea++;
                        copiaLinea = bloque_if(listaTokens, copiaLinea);
                        copiaLinea = opcional_else(listaTokens, copiaLinea);
                        return copiaLinea;
                    } else {
                        throw new validarException("Error en la linea: " + listaTokens.get(copiaLinea).getLinea() + " se esperaba un then");
                    }
                } else {
                    throw new validarException("Error en la linea: " + listaTokens.get(copiaLinea).getLinea() + " se esperaba un )");
                }
            } else {
                throw new validarException("Error en la linea: " + listaTokens.get(copiaLinea).getLinea() + " se esperaba un (");
            }

        }
        throw new validarException("Error en la linea: " + listaTokens.get(copiaLinea).getLinea() + " se esperaba un if");
    }

    public static int opcional_else(LinkedList<infoPalabra> listaTokens, int copiaLinea) throws validarException {
        if (copiaLinea < listaTokens.size() && listaTokens.get(copiaLinea).getToken() == -11) { // token de "else"
            copiaLinea++;
            copiaLinea = bloque_if(listaTokens, copiaLinea);
        }
        // ε (no hay else): simplemente retornas la línea actual sin errores
        return copiaLinea;
    }

    public static int bloque_if(LinkedList<infoPalabra> listaTokens, int copiaLinea) throws validarException {
        if (listaTokens.get(copiaLinea).getToken() == -2) {
            copiaLinea++;
            copiaLinea = instrucciones(listaTokens, copiaLinea);
            if (listaTokens.get(copiaLinea).getToken() == -3) {
                copiaLinea++;
                return copiaLinea;
            } else {
                throw new validarException("Error en la linea: " + listaTokens.get(copiaLinea).getLinea() + " se esperaba un end");
            }
        }
        throw new validarException("Error en la linea: " + listaTokens.get(copiaLinea).getLinea() + " se esperaba un begin");
    }

    public static int bucle_while(LinkedList<infoPalabra> listaTokens, int copiaLinea) throws validarException {
        if (listaTokens.get(copiaLinea).getToken() == -13) {
            copiaLinea++;
            if (listaTokens.get(copiaLinea).getToken() == -71) {
                copiaLinea++;
                copiaLinea = expresion(listaTokens, copiaLinea);
                if (listaTokens.get(copiaLinea).getToken() == -72) {
                    copiaLinea++;
                    if (listaTokens.get(copiaLinea).getToken() == -14) {
                        copiaLinea++;
                        copiaLinea = bloque_if(listaTokens, copiaLinea);
                        return copiaLinea;
                    } else {
                        throw new validarException("Error en la linea: " + listaTokens.get(copiaLinea).getLinea() + " se esperaba la palabra do");
                    }
                } else {
                    throw new validarException("Error en la linea: " + listaTokens.get(copiaLinea).getLinea() + " se esperaba un )");
                }
            } else {
                throw new validarException("Error en la linea: " + listaTokens.get(copiaLinea).getLinea() + " se esperaba un (");
            }
        } else {
            throw new validarException("Error en la linea: " + listaTokens.get(copiaLinea).getLinea() + " se esperaba la palabra while");
        }
    }

    public static int bucle_repeat(LinkedList<infoPalabra> listaTokens, int copiaLinea) throws validarException {
        if (listaTokens.get(copiaLinea).getToken() == -15) {
            copiaLinea++;
            copiaLinea = bloque_if(listaTokens, copiaLinea);
            if (listaTokens.get(copiaLinea).getToken() == -16) {
                copiaLinea++;
                if (listaTokens.get(copiaLinea).getToken() == -71) {
                    copiaLinea++;
                    copiaLinea = expresion(listaTokens, copiaLinea);
                    if (listaTokens.get(copiaLinea).getToken() == -72) {
                        copiaLinea++;
                        return copiaLinea;
                    } else {
                        throw new validarException("Error en la linea: " + listaTokens.get(copiaLinea).getLinea() + " se esperaba un )");
                    }
                } else {
                    throw new validarException("Error en la linea: " + listaTokens.get(copiaLinea).getLinea() + " se esperaba un (");
                }
            } else {
                throw new validarException("Error en la linea: " + listaTokens.get(copiaLinea).getLinea() + " se esperaba la palabra until");
            }
        } else {
            throw new validarException("Error en la linea: " + listaTokens.get(copiaLinea).getLinea() + " se esperaba la palabra repeat");
        }
    }

    public static int entrada_salida(LinkedList<infoPalabra> listaTokens, int copiaLinea) throws validarException {
        if (listaTokens.get(copiaLinea).getToken() == -5) {  // write
            copiaLinea++;
            if (listaTokens.get(copiaLinea).getToken() == -71) {  // (
                copiaLinea++;
                copiaLinea = expresion(listaTokens, copiaLinea);
                if (listaTokens.get(copiaLinea).getToken() == -72) {  // )
                    copiaLinea++;
                    return copiaLinea;
                } else {
                    throw new validarException("Error en la línea: " + listaTokens.get(copiaLinea).getLinea() + " se esperaba un ')'");
                }
            } else {
                throw new validarException("Error en la línea: " + listaTokens.get(copiaLinea).getLinea() + " se esperaba un '('");
            }

        } else if (listaTokens.get(copiaLinea).getToken() == -4) {  // read
            copiaLinea++;  // ✅ CONSUMIR EL `read`

            if (listaTokens.get(copiaLinea).getToken() == -71) {  // (
                copiaLinea++;
                copiaLinea = variable(listaTokens, copiaLinea);
                if (listaTokens.get(copiaLinea).getToken() == -72) {  // )
                    copiaLinea++;
                    return copiaLinea;
                } else {
                    throw new validarException("Error en la línea: " + listaTokens.get(copiaLinea).getLinea() + " se esperaba un ')'");
                }
            } else {
                throw new validarException("Error en la línea: " + listaTokens.get(copiaLinea).getLinea() + " se esperaba un '('");
            }
        }

        throw new validarException("Error en la línea: " + listaTokens.get(copiaLinea).getLinea() + " se esperaba la palabra read o write");
    }


    public static int expresion(LinkedList<infoPalabra> listaTokens, int numLinea) throws validarException {
        // 1) Consumir la parte simple y actualizar numLinea
        numLinea = expresion_simple(listaTokens, numLinea);

        // 2) Intentar la parte relacional
        try {
            numLinea = op_relacional(listaTokens, numLinea);
            numLinea = expresion_simple(listaTokens, numLinea);
        } catch (validarException e) {
            // Si no había relacional, simplemente devolvemos
            // el numLinea que ya avanzó con expresion_simple
        }

        return numLinea;
    }

    public static int expresion_simple(LinkedList<infoPalabra> listaTokens, int numLinea) throws validarException {
        int copiaLinea = numLinea;

        // 1. Reconocer <termino>
        copiaLinea = termino(listaTokens, copiaLinea);

        // 2. Intentar repetir <op_aditivo> <termino> las veces que sea necesario
        while (copiaLinea < listaTokens.size() &&
                (listaTokens.get(copiaLinea).getToken() == -26 || listaTokens.get(copiaLinea).getToken() == -27)) {
            copiaLinea++; // consumimos el + o -
            copiaLinea = termino(listaTokens, copiaLinea);
        }
        return copiaLinea;
    }

    public static int termino(LinkedList<infoPalabra> listaTokens,
                              int numLinea) throws validarException {
        // 1. Siempre empieza con un factor
        numLinea = factor(listaTokens, numLinea);

        // 2. Si hay operador * o /, lo consumimos y otro factor
        while (numLinea < listaTokens.size() &&
                (listaTokens.get(numLinea).getToken() == -23  // *
                        || listaTokens.get(numLinea).getToken() == -24) // /
        ) {
            numLinea++;  // consumimos * o /
            numLinea = factor(listaTokens, numLinea);
        }

        // 3. Si no había *,/, simplemente devolvemos la posición actual
        return numLinea;
    }


    public static int factor(LinkedList<infoPalabra> listaTokens, int copiaLinea) throws validarException {
        int token = listaTokens.get(copiaLinea).getToken();

        // 1. Número (entero o real)
        if (token == -61 || token == -62) {
            return copiaLinea + 1;
        }

        // 2. Variable (identificador o arreglo)
        if (token == -51) {
            return variable(listaTokens, copiaLinea);
        }

        // 3. Constante booleana (true / false)
        if (token == -21 || token == -22) {
            return copiaLinea + 1;
        }

        // 4. Cadena
        if (token == -63) {
            return copiaLinea + 1;
        }

        // 5. Paréntesis: ( <expresion> )
        if (token == -71) { // (
            copiaLinea++;
            copiaLinea = expresion(listaTokens, copiaLinea);
            if (listaTokens.get(copiaLinea).getToken() != -72) { // )
                throw new validarException("Error en la línea " +
                        listaTokens.get(copiaLinea).getLinea() + ": se esperaba ')'");
            }
            return copiaLinea + 1;
        }

        // 6. Negativo unario: - <factor>
        if (token == -27) { // -
            copiaLinea++;
            return factor(listaTokens, copiaLinea); // llamada recursiva
        }

        // 7. Si no se reconoció ningún caso válido
        throw new validarException("Error en la línea " +
                listaTokens.get(copiaLinea).getLinea() + ": factor no válido");
    }

    public static int listaExpresiones(LinkedList<infoPalabra> listaTokens,
                                       int copiaLinea) throws validarException {
        // 1) Siempre comienza con una expresión
        copiaLinea = expresion(listaTokens, copiaLinea);

        // 2) Verificamos si viene una coma para continuar la lista
        if (copiaLinea < listaTokens.size() &&
                listaTokens.get(copiaLinea).getToken() == -74) { // suponiendo , es -74
            copiaLinea++; // consumimos la coma

            // 3) Recursivamente procesamos más expresiones
            copiaLinea = listaExpresiones(listaTokens, copiaLinea);
        }

        return copiaLinea;
    }


    public static int op_relacional(LinkedList<infoPalabra> listaTokens, int copiaLinea) throws validarException {
        if (listaTokens.get(copiaLinea).getToken() == -28) {
            copiaLinea++;
            return copiaLinea;
        } else if (listaTokens.get(copiaLinea).getToken() == -29) {
            copiaLinea++;
            return copiaLinea;
        } else if (listaTokens.get(copiaLinea).getToken() == -34) {
            copiaLinea++;
            return copiaLinea;
        } else if (listaTokens.get(copiaLinea).getToken() == -36) {
            copiaLinea++;
            return copiaLinea;
        } else if (listaTokens.get(copiaLinea).getToken() == -33) {
            copiaLinea++;
            return copiaLinea;
        } else if (listaTokens.get(copiaLinea).getToken() == -35) {
            copiaLinea++;
            return copiaLinea;
        } else {
            throw new validarException("Error en la linea: " + listaTokens.get(copiaLinea).getLinea() + " se esperaba un operador relacional");
        }
    }

    public static void main(String[] args) {
        try {
            if (leerArchivo()){
                int numLinea = 0;
                numLinea = inicio(listaTokens,numLinea);
                int numfinal = listaTokens.getLast().getLinea();
                JOptionPane.showMessageDialog(null, "El archivo se ha validado correctamente");
                System.exit(0);
            }
        }catch (validarException e){
            //Se quito mensaje doble de error
            System.out.println( e.getMessage());
            System.exit(1);
        }
    }

}
