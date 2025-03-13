package U3;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Practica3 {
    /*
    * Fecha 04/03/2025
    *
    * Diseñe un autómata finito determinista para cada lenguaje, incluya descripción formal y expresión regular.

Utilizando el lenguaje de programación JAVA genere un programa o aplicación que simule
* el funcionamiento de un autómata finito determinista que clasifique cadenas o palabras válidas
* para cada lenguaje solicitado, incluya la opcion para visualizar la tabla de palabras validas en pantalla ( leida desde archivo de texto)

El programa o aplicación puede recibir como entrada uno de los siguientes:

· leer un archivo de texto
Y mostrar como salida la clasificación de cada cadena o palabra de acuerdo con el lenguaje definido, almacene la información generada en }
* un archivo de
* texto con la siguiente estructura, considere entre cada columna una COMA :
*
* Cadena palabra,Lenguaje

Por ejemplo

CADENA,LENGUAJE

2&/letras,Lenguaje1

M2$$,Lenguaje2

A_9a,lenguaje3
*
*
* Lenguaje 1---> Inicia con un o mas digitos seguido de ninguno o mas letras o & seguido de ningun o un /
* Lenguaje 2---> Inicia con una o mas letras minúsculas seguida de uno o mas digitos o / seguido de ningun par o mas $
* Lenguaje 3---> Inicia con una o mas letras mayusculas o & o % seguido de ninguna o mas letras o guion bajo o dígitos
*
*
* AUTORES
* MAURO RUIZ
* ORLANDO NORIEGA
* DAVID De la fuente monjaras
*
*
    *
    * */


    public static void main(String[] args) {
        boolean archivoLeido = leerArchivo();
        if (!archivoLeido) {
            JOptionPane.showMessageDialog(null, "El archivo no se pudo leer");
            return;
        }
        Menu();
    }

    static String archivoEntrada = "C:/Users/Mauro/Desktop/.A/Automatas/src/U3/Entrada.txt";
    static List<String> palabrasDeArchivo = new ArrayList<>();
    static String archivoSalida = "C:/Users/Mauro/Desktop/.A/Automatas/src/U3/Salida.txt";
    static List<String> palabrasValidas = new ArrayList<>();
    static List<String> palabrasInvalidas = new ArrayList<>();

    static File archivo = new File(archivoSalida);

    //Validacion de archivo
    private static boolean validacionDeArchivo() {
        try {
            FileReader fr = new FileReader(archivoEntrada);
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
        if (!validacionDeArchivo()) {
            JOptionPane.showMessageDialog(null, "Archivo vacio");
            return false;
        }
        try {
            FileReader fr = new FileReader(archivoEntrada);
            BufferedReader br = new BufferedReader(fr);
            String linea;

            String[] palabrasLinea = new String[0];
            while ((linea = br.readLine()) != null) {
                palabrasLinea = linea.trim().split("\\s+");
                palabrasDeArchivo.addAll(Arrays.asList(palabrasLinea));
            }
            br.close();
            JOptionPane.showMessageDialog(null, "Archivo validado");
            return true;

        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Archivo no encontrado, no existe el archivo");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al leer el archivo");
        }
        return false;
    }


    private static void escribirEnArchivoDeSalida() {
        // Limpiamos la lista para evitar duplicados en cada ejecución
        palabrasDeArchivo.clear();

        if (!validacionDeArchivo()) {
            JOptionPane.showMessageDialog(null, "El archivo de entrada está vacío o no se pudo leer.");
            return;
        }

        if (!leerArchivo()) {
            JOptionPane.showMessageDialog(null, "Error al leer el archivo de entrada.");
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoSalida, false))) { // sobrescribir
            bw.write("CADENA,LENGUAJE\n");

            for (String palabra : palabrasDeArchivo) {
                palabra = palabra.trim();

                // Validar en cada lenguaje
                boolean esLenguaje1 = validarLenguaje1(palabra);
                boolean esLenguaje2 = validarLenguaje2(palabra);
                boolean esLenguaje3 = validarLenguaje3(palabra);

                // Si la palabra no es válida en ninguno, la saltamos
                if (!(esLenguaje1 || esLenguaje2 || esLenguaje3)) {
                    continue;
                }

                // Asignar el tipo de lenguaje
                String lenguaje = "";
                if (esLenguaje1) {
                    lenguaje = "Lenguaje1";
                } else if (esLenguaje2) {
                    lenguaje = "Lenguaje2";
                } else if (esLenguaje3) {
                    lenguaje = "Lenguaje3";
                }

                // Escribir la línea en el archivo de salida
                bw.write(palabra + "," + lenguaje + "\n");
            }

            JOptionPane.showMessageDialog(null, "Se ha escrito correctamente en el archivo de salida.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al escribir en el archivo de salida: " + e.getMessage());
        }
    }
    private static void mostrarValidasEInvalidas() {
        // Listas para almacenar las palabras clasificadas
        List<String> palabrasValidas = new ArrayList<>();
        List<String> palabrasInvalidas = new ArrayList<>();

        // Recorrer cada palabra ya cargada en palabrasDeArchivo
        for (String palabra : palabrasDeArchivo) {
            // Limpiar espacios en blanco
            palabra = palabra.trim();
            if (palabra.isEmpty()) {
                continue;
            }

            // Validar en cada lenguaje
            boolean esLenguaje1 = validarLenguaje1(palabra);
            boolean esLenguaje2 = validarLenguaje2(palabra);
            boolean esLenguaje3 = validarLenguaje3(palabra);

            // Si es válida para alguno de los lenguajes, se agrega a la lista de válidas;
            // de lo contrario, a la de inválidas.
            if (esLenguaje1 || esLenguaje2 || esLenguaje3) {
                palabrasValidas.add(palabra);
            } else {
                palabrasInvalidas.add(palabra);
            }
        }

        // Construir la cadena de salida formateada
        StringBuilder salida = new StringBuilder();
        salida.append("Palabras Válidas:\n");
        for (String p : palabrasValidas) {
            salida.append(p).append("\n");
        }
        salida.append("\nPalabras Inválidas:\n");
        for (String p : palabrasInvalidas) {
            salida.append(p).append("\n");
        }


        SalidaFormateada.imprimeConScroll(salida.toString(), "Resultados de Validación");
    }


    public static boolean validarLenguaje1(String pal) {
        int estado = 0;

        for (int i = 0; i < pal.length(); i++) {
            char c = pal.charAt(i);

            switch (estado) {
                case 0:
                    if (Character.isDigit(c)) {
                        estado = 1;
                    } else {
                        return false;
                    }
                    break;
                case 1:
                    if (Character.isDigit(c)) {
                        estado = 1;
                    } else if (Character.isLetter(c) || c == '&') {
                        estado = 2;
                    } else if (c == '/') {
                        estado = 3;
                    } else {
                        return false;
                    }
                    break;
                case 2:
                    if (Character.isLetter(c) || c == '&') {
                        estado = 2;
                    } else if (c == '/') {
                        estado = 3;
                    } else {
                        return false;
                    }
                    break;
                case 3:
                    return false;
            }
        }

        return estado == 1 || estado == 2 || estado == 3;
    }


    private static boolean validarLenguaje2(String palabra) {
        int estado = 0; // Estado inicial

        for (int i = 0; i < palabra.length(); i++) {
            char c = palabra.charAt(i);

            switch (estado) {
                case 0: // Estado inicial, espera una letra minúscula
                    if (Character.isLowerCase(c)) {
                        estado = 1; // Va al estado 1
                    } else {
                        estado = 5; // Rechazado
                    }
                    break;

                case 1: // Ya tiene al menos una letra, espera más letras o un número o '/'
                    if (Character.isLowerCase(c)) {
                        estado = 1; // Sigue en el mismo estado
                    } else if (Character.isDigit(c) || c == '/') {
                        estado = 2; // Va al estado 2
                    } else {
                        estado = 5; // Rechazado
                    }
                    break;

                case 2: // Ya tiene dígitos o '/', ahora puede seguir con más dígitos o empezar con $
                    if (Character.isDigit(c) || c == '/') {
                        estado = 2; // Sigue en el mismo estado
                    } else if (c == '$') {
                        estado = 3; // Va al estado 3
                    } else {
                        estado = 5; // Rechazado
                    }
                    break;

                case 3: // Ha encontrado un primer $, espera otro para formar un par
                    if (c == '$') {
                        estado = 4; // Si encuentra un segundo $, transita al estado 4
                    } else {
                        estado = 5; // Rechazado si encuentra cualquier otro carácter
                    }
                    break;

                case 4: // Ha encontrado un par de $, puede seguir con más pares
                    if (c == '$') {
                        estado = 3; // Regresa al estado 3 para validar otro par
                    } else {
                        return false; // Rechazado si encuentra otro carácter
                    }
                    break;

                case 5:
                    return false;
            }
        }

        // Estados de aceptación: 2 (sin $) o 4 (pares de $)
        return (estado == 2 || estado == 4);
    }


    private static boolean validarLenguaje3(String palabra) {
        int estado = 0;
        for (char c : palabra.toCharArray()) {
            switch (estado) {
                case 0:
                    if (Character.isUpperCase(c) || c == '&' || c == '%') {
                        estado = 1;
                    } else {
                        estado = 3; // Estado de inválido
                    }
                    break;
                case 1:
                    if (Character.isUpperCase(c) || c == '&' || c == '%') {
                        estado = 1;
                    } else
                    if (Character.isLetter(c) || c == '_' || Character.isDigit(c)) {
                        estado = 2;
                    } else {
                        estado = 3; // Estado de inválido
                    }
                    break;
                case 2:
                    if (Character.isLetter(c) || c == '_' || Character.isDigit(c)) {
                        estado = 2;
                    } else {
                        estado = 3; // Estado de inválido
                    }
                    break;
                case 3:
                    return false; // Retorna invalido para cualquier valor que llegue a 3
            }
            if (estado == 3){
                return false;
            }
        }
        return (estado == 1 || estado == 2);

    }


    private static void Menu() {
        String menu = """
                    1. Escribir en archivo de salida
                    2. Tabla de cadenas validas e invalidas
                    3. Salir
                """;
        int opcion = 0;
        while (opcion != 3) {
            try {
                opcion = Integer.parseInt(JOptionPane.showInputDialog(null, menu, "MENU", JOptionPane.PLAIN_MESSAGE));
                if (opcion < 1 || opcion > 3) {
                    JOptionPane.showMessageDialog(null, "Ingrese una opcion valida");
                    continue;
                }
                switch (opcion) {
                    case 1:
                        escribirEnArchivoDeSalida();
                        break;

                    case 2:
                        mostrarValidasEInvalidas();

                        break;
                    case 3:
                        break;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Ingrese una opcion valida");
            }
        }
    }

    public class SalidaFormateada {
        public static void imprimeConScroll(String cadena,String titulo) {

            JTextArea area = new JTextArea(cadena, 50, 50); // dimensiones de la ventana
            Font font = new Font("Arial", Font.PLAIN, 14); // Por ejemplo, Arial 14pt
            area.setFont(font);
            JScrollPane panel = new JScrollPane(area,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            JOptionPane.showMessageDialog(null, panel,titulo,JOptionPane.INFORMATION_MESSAGE);
        }
    }

}
