package U4;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class practica4 {

    static String entrada = "C:/Users/Mauro/Desktop/.A/Automatas/src/U4/Entrada.txt";
    static ArrayList<infoPalabra> palabrasArchivo = new ArrayList<>();
    static ArrayList<infoPalabra> erroresArchivo = new ArrayList<>();

    public static void main(String[] args) {
        if (leerArchivo()){
            analisisLexico();
            agruparErrores();
            erroresLexico();
            escribirArchivo();
        }
    }

    private static boolean VerificarContenido() {
        try {
            FileReader fr = new FileReader(entrada);
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
            FileReader fr = new FileReader(entrada);
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
        Pattern er = Pattern.compile("\".*?\"|/\\*.*?\\*/|-?\\d+\\.\\d+|-?\\d+|&&|\\|\\||!|==|!=|<=|>=|<|>|\\+|-|\\*|/|%|=|[(),;:\\[\\]]|[a-z][a-zA-Z0-9_]*|\\s+");
        Matcher matcher = er.matcher(linea);
        int start = 0;
        while (matcher.find()) {
            if (matcher.start() != start) {
                // Aquí usamos matcher.start() para la posición
                palabrasArchivo.addLast(new infoPalabra(linea.substring(start, matcher.start()).trim(), 0, 0, start, numLinea));
            }
            if (!matcher.group().matches("\\s+")) {
                // Usamos matcher.start() para asignar la posición real del token
                palabrasArchivo.addLast(new infoPalabra(matcher.group(), 0, 0, matcher.start(), numLinea));
            }
            start = matcher.end();
        }
        if (start != linea.length()) {
            palabrasArchivo.addLast(new infoPalabra(linea.substring(start).trim(), 0, 0, start, numLinea));
        }
    }


    private static void esIdentificadores(infoPalabra palabra) {
        String identificador = palabra.getPalabra();
        if (identificador.matches("[a-z][a-zA-Z0-9_]*")) {
            palabra.setIdentificador(-2);
            palabra.setToken(-51);  // Asigna el token para identificadores
        }
    }


    private static void esPalabraReservada(infoPalabra palabra) {
        String pRes = palabra.getPalabra();
        if (pRes.matches("program|begin|end|read|write|int|real|string|bool|if|else|then|while|do|repeat|until|var|procedure|function|array|true|false")) {
            palabra.setIdentificador(-1); //Como no es identificador se le agrega un -1
//Aquí se les da su token a cada una de las palabras con el switch
            switch (pRes) {
                case "program":
                    palabra.setToken(-1);
                    break;
                case "begin":
                    palabra.setToken(-2);
                    break;
                case "end":
                    palabra.setToken(-3);
                    break;
                case "read":
                    palabra.setToken(-4);
                    break;
                case "write":
                    palabra.setToken(-5);
                    break;
                case "int":
                    palabra.setToken(-6);
                    break;
                case "real":
                    palabra.setToken(-7);
                    break;
                case "string":
                    palabra.setToken(-8);
                    break;
                case "bool":
                    palabra.setToken(-9);
                    break;
                case "if":
                    palabra.setToken(-10);
                    break;
                case "else":
                    palabra.setToken(-11);
                    break;
                case "then":
                    palabra.setToken(-12);
                    break;
                case "while":
                    palabra.setToken(-13);
                    break;
                case "do":
                    palabra.setToken(-14);
                    break;
                case "repeat":
                    palabra.setToken(-15);
                    break;
                case "until":
                    palabra.setToken(-16);
                    break;
                case "var":
                    palabra.setToken(-17);
                    break;
                case "procedure":
                    palabra.setToken(-18);
                    break;
                case "function":
                    palabra.setToken(-19);
                    break;
                case "array":
                    palabra.setToken(-20);
                    break;
                case "true":
                    palabra.setToken(-21);
                    break;
                case "false":
                    palabra.setToken(-22);
                    break;
                default:
                    //Se rompe si no es una palabra reservada
                    break;


            }
        }

    }

    private static void esCaracterEspecial(infoPalabra palabra) {
        String cE = palabra.getPalabra();
        if (cE.matches("[(),;:\\[\\]]")) {
            palabra.setIdentificador(-1); //Como no es identificador se le pone -1

            switch (cE) {
                case "(":
                    palabra.setToken(-71);
                    break;
                case ")":
                    palabra.setToken(-72);
                    break;
                case ";":
                    palabra.setToken(-73);
                    break;
                case ",":
                    palabra.setToken(-74);
                    break;
                case ":":
                    palabra.setToken(-75);
                    break;
                case "[":
                    palabra.setToken(-76);
                    break;
                case "]":
                    palabra.setToken(-77);
                    break;
                default:
                    break;

            }
        }
    }

    private static void esNumerosEnteros(infoPalabra palabra) {
        String nE = palabra.getPalabra();
        if (nE.matches("-?\\d+")) {
            palabra.setIdentificador(-1);
            palabra.setToken(-61);
        }
    }

    private static void esNumerosDecimales(infoPalabra palabra) {
        String nD = palabra.getPalabra();
        if (nD.matches("-?\\d+(\\.)\\d+")) {
            palabra.setIdentificador(-1);
            palabra.setToken(-62);
        }
    }

    private static void esConstanteString(infoPalabra palabra) {
        String cS = palabra.getPalabra();
        if (cS.matches("\".*?\"")) {
            palabra.setIdentificador(-1);
            palabra.setToken(-63);
        }
    }


    private static void esOperadorAritmetico(infoPalabra palabra) {
        String op = palabra.getPalabra();
        if (op.matches("\\+|-|\\*|/|%|=")) {
            palabra.setIdentificador(-1);// No es un identificador
            switch (op) {
                case "+":
                    palabra.setToken(-24);
                    break;
                case "-":
                    palabra.setToken(-25);
                    break;
                case "*":
                    palabra.setToken(-21);
                    break;
                case "/":
                    palabra.setToken(-22);
                    break;
                case "%":
                    palabra.setToken(-23);
                    break;
                case "=":
                    palabra.setToken(-32);
                    break;
            }
        }
    }

    private static void esOperadorRelacional(infoPalabra palabra) {
        String OpL = palabra.getPalabra();
        if (OpL.matches("==|!=|<=|>=|<|>")) {
            palabra.setIdentificador(-1);
            switch (OpL) {
                case "==":
                    palabra.setToken(-28);
                    break;
                case "!=":
                    palabra.setToken(-29);
                    break;
                case "<=":
                    palabra.setToken(-34);
                    break;
                case ">=":
                    palabra.setToken(-36);
                    break;
                case "<":
                    palabra.setToken(-33);
                    break;
                case ">":
                    palabra.setToken(-35);
                    break;


            }
        }
    }

    private static void esOperadorLogico(infoPalabra palabra) {
        String OpL = palabra.getPalabra();
        if (OpL.matches("|&&|\\|\\||!")) {
            palabra.setIdentificador(-1);
            switch (OpL) {
                case "&&":
                    palabra.setToken(-30);
                    break;
                case "||":
                    palabra.setToken(-31);
                    break;
                case "!":
                    palabra.setToken(-37);
                    break;
                default:
                    break;
            }
        }

    }
    private static void esComentarios(infoPalabra palabra) {
        String comentario = palabra.getPalabra();
        //A los comentarios no se le ponen token porque no es una accion como tal
        if (comentario.matches("/\\*.*?\\*/")) {
            palabra.setIdentificador(-1);
        }
    }
    private static void analisisLexico() {
        for (infoPalabra palabra : palabrasArchivo){
            esIdentificadores(palabra);
            esPalabraReservada(palabra);
            esCaracterEspecial(palabra);
            esNumerosEnteros(palabra);
            esNumerosDecimales(palabra);
            esConstanteString(palabra);
            esOperadorAritmetico(palabra);
            esOperadorRelacional(palabra);
            esOperadorLogico(palabra);
            esComentarios(palabra);
        }
    }

    private static void erroresLexico() {
        for (infoPalabra palabra : palabrasArchivo) {
            if (palabra.getToken() == 0 && !palabra.getPalabra().matches("\\s+")) {
                erroresArchivo.add(new infoPalabra(palabra.getPalabra(), palabra.getToken(), palabra.getIdentificador(), palabra.getPosicion()));
            }
        }
    }

    /**
     * Recorre la lista de tokens y concatena en la misma línea los fragmentos de error adyacentes para formar un lexema completo.
     */
    private static void agruparErrores() {
        // Creamos una nueva lista para almacenar los tokens agrupados
        ArrayList<infoPalabra> nuevaLista = new ArrayList<>();
        for (int i = 0; i < palabrasArchivo.size(); i++) {
            infoPalabra actual = palabrasArchivo.get(i);
            // Solo procesamos si el token actual es de error (token == 0)
            if (actual.getToken() == 0) {
                // Calculamos la posición final del lexema actual
                int posFinal = actual.getPosicion() + actual.getPalabra().length();

                // Verificar tokens siguientes que sean error, en la misma línea
                while (i + 1 < palabrasArchivo.size()) {
                    infoPalabra siguiente = palabrasArchivo.get(i + 1);
                    // Si están en la misma línea y el token siguiente inicia antes o justo en el final del actual...
                    if (siguiente.getLinea() == actual.getLinea() && siguiente.getPosicion() <= posFinal) {
                        // se concatena el lexema
                        actual.setPalabra(actual.getPalabra() + siguiente.getPalabra());
                        // se actualiza la posición final tomando en cuenta el siguiente token
                        posFinal = siguiente.getPosicion() + siguiente.getPalabra().length();
                        i++; // Se salta al sig token
                    } else {
                        break;
                    }
                }
            }
            nuevaLista.add(actual);
        }

        // Actualizamos la lista original
        palabrasArchivo = nuevaLista;
    }



    public static void tablaTokens() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("C:/Users/Mauro/Desktop/.A/Automatas/src/U4/Tabla de Tokens.txt"))) {
            for (infoPalabra palabra : palabrasArchivo) {
                if (palabra.getPalabra().matches("/\\*.*?\\*/")) {
                    continue;
                }
                if (palabra.getToken() != 0) {
                    bw.write(palabra.toString());
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Problemas para escribir el archivo de tokens");
        }
    }


    public static void tablaErrores(){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("C:/Users/Mauro/Desktop/.A/Automatas/src/U4/Tabla de errores.txt"));
            for (infoPalabra palabra : erroresArchivo) {
                if (palabra.getPalabra().matches("/\\*.*?\\*/")) {
                    continue;
                }
                bw.write("Error en la línea: " + palabra.getPosicion() + ". En la palabra: " + palabra.getPalabra());
                bw.newLine();
            }
            bw.close();
        } catch (IOException e){
            JOptionPane.showMessageDialog(null, "Problemas para escribir el archivo de errores");
        }
    }

    private static void escribirArchivo() {
        tablaTokens();
        tablaErrores();

    }
}

