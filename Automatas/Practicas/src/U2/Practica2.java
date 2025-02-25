package U2;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Practica2 {
    /*
    aplicación donde valide cadenas proporcionadas por el usuario o leidas desde
    un archivo de texto ( uso de Expresión regular  )  para cada uno de los
    componentes lexicos.

    Y mostrar la clasificación de cada cadena o palabra de acuerdo al lenguaje
    definido (haciendo uso del manejo de la expresión regular). Se deberá probar al
    menos 3 ejemplos de cada componente lexico, considere manejo errores y
    diseño lógico del programa acorde al paradigma asociado al lenguaje de
    programación, anexe pantallas de ejecución de cada lenguaje probado y elabore
    tabla con las cadenas validas e invalidas.

    Autores:
    Mauro Rodrigo Ruiz Alvarez
    David Sebastián de la Fuente Monjaras
    Orlando Noriega
     */
    public static void main(String [] args) {
        boolean archivoLeido = lecturaArchivo();
        if (!archivoLeido) {
            JOptionPane.showMessageDialog(null,"El archivo no se pudo leer");
            return;
        }
        Menu();
    }
    static String NombArch = "C://Users//Mauro//Desktop//Automatas//P2.txt";
    static List<String> palabrasValidas = new ArrayList();
    static List<String> palabrasDelArchi = new ArrayList();
    static List<String> palabrasInvalidas = new ArrayList();

    private static boolean validarContenido() {
        try{
            FileReader fr =  new FileReader(NombArch);
            BufferedReader br = new BufferedReader(fr);
            boolean conContenido = br.readLine() != null;
            br.close();
            return conContenido;
        }catch (IOException e){
            JOptionPane.showMessageDialog(null,"Problema para leer el archivo");
            return false;
        }
    }
    private static boolean  lecturaArchivo() {
        if (!validarContenido()){
            JOptionPane.showMessageDialog(null,"No hay contenido en el archivo");
            return false;
        }
        try{
            FileReader fr = new FileReader(NombArch);
            BufferedReader br = new BufferedReader(fr);
            String linea;

            while ((linea = br.readLine()) != null){
                String[] palabrasLinea = linea.split("[,\\s]+");
                palabrasDelArchi.addAll(Arrays.asList(palabrasLinea));
            }
            br.close();
            JOptionPane.showMessageDialog(null,"Archivo leido con exito");
            return true;
        }catch (FileNotFoundException e){
            JOptionPane.showMessageDialog(null,"No se pudo abrir el archivo, porque no existe o no se encuentra");

        }catch (IOException e){
            JOptionPane.showMessageDialog(null,"Problema para leer el archivo");
        }
        return false;
    }
    private static boolean esPalabraReservada(String identificador) {
        String[] palabrasReservadas = {
                "False", "None", "True", "and", "as", "assert", "async", "await", "break",
                "class", "continue", "def", "del", "elif", "else", "except", "finally",
                "for", "from", "global", "if", "import", "in", "is", "lambda", "nonlocal",
                "not", "or", "pass", "raise", "return", "try", "while", "with", "yield"
        };

        for (String palabra : palabrasReservadas) {
            if (identificador.equals(palabra)) {
                return true;
            }
        }
        return false;
    }
    private static void Menu(){
        String menu = """
                    1. Comparacion de Archivo con La numeracion en JAVA
                    2. Comparacion de Archivo con Identificadores en PYTHON
                    3. Comparacion de Archivo con Constantes String o cadenas VB
                    4. Salir
                """;
        int opcion = 0;
        while (opcion != 4){
            try {
                opcion = Integer.parseInt(JOptionPane.showInputDialog(null, menu, "MENU", JOptionPane.PLAIN_MESSAGE));
                if (opcion < 1 || opcion > 4) {
                    JOptionPane.showMessageDialog(null, "Ingrese una opcion valida");
                    continue;
                }
                switch (opcion) {
                    case 1:
                        palabrasValidas.clear();
                        palabrasInvalidas.clear();

                        String comp1 = "Comparacion de Archivo con JAVA Valida";
                        String comp2 = "Comparacion de Archivo con JAVA  Invalida";
                        String er = "-?\\d+(\\.\\d+)?";
                        Pattern pattern = Pattern.compile(er);

                        for (String palabra : palabrasDelArchi) {
                            boolean validar = pattern.matcher(palabra).matches();
                            if(validar){
                                palabrasValidas.add(palabra);
                            }else {
                                palabrasInvalidas.add(palabra);
                            }
                        }
                        String palabraValida = String.join("\n", palabrasValidas);
                        String palabraInvalida = String.join("\n", palabrasInvalidas);

                        SalidaFormateada.imprimeConScroll("Las palabras que SI coinciden con la Expresion Regular " + er + "\n" + "\n" + palabraValida, comp1);
                        SalidaFormateada.imprimeConScroll("Las palabras que NO coinciden con la Expresion Regular " + er + "\n" + "\n" + palabraInvalida,comp2);
                        break;

                    case 2:
                        palabrasValidas.clear();
                        palabrasInvalidas.clear();

                        String comp3 = "Comparacion de Archivo con PYTHON Valida";
                        String comp4 = "Comparacion de Archivo con PYTHON Invalida";
                        String  er2   = "^[a-zA-Z_][a-zA-Z0-9_]*$";
                        Pattern pattern2 = Pattern.compile(er2);
                        for (String palabra : palabrasDelArchi) {
                            boolean esValida = pattern2.matcher(palabra).matches() && !esPalabraReservada(palabra);

                            if (esValida) {
                                palabrasValidas.add(palabra);
                            } else {
                                palabrasInvalidas.add(palabra);
                            }
                        }
                        String palabraValida1 = String.join("\n", palabrasValidas);
                        String palabraInvalida1 = String.join("\n", palabrasInvalidas);

                        // Imprimir resultados con el formato deseado
                        SalidaFormateada.imprimeConScroll(
                                "Las palabras que SÍ coinciden con la Expresión Regular " + er2 + "\n\n" + palabraValida1, comp3);

                        SalidaFormateada.imprimeConScroll(
                                "Las palabras que NO coinciden con la Expresión Regular " + er2 + "\n\n" + palabraInvalida1, comp4);
                        break;

                    case 3:
                        palabrasValidas.clear();
                        palabrasInvalidas.clear();
                        String comp5 = "Comparación de Archivo con Lenguaje 3 Válida";
                        String comp6 = "Comparación de Archivo con Lenguaje 3 Inválida";
                        String er3 = "^\"([^\"]|\"\")*\"$";
                        Pattern pattern3 = Pattern.compile(er3);

                        for (String palabra : palabrasDelArchi) {
                            Matcher matcher = pattern3.matcher(palabra);
                            boolean esValida = matcher.matches();

                            if (esValida) {
                                palabrasValidas.add(palabra);
                            } else {
                                palabrasInvalidas.add(palabra);
                            }
                        }

// Formatear salida
                        String palabraValida2 = String.join("\n", palabrasValidas);
                        String palabraInvalida2 = String.join("\n", palabrasInvalidas);

// Imprimir resultados con el formato deseado
                        SalidaFormateada.imprimeConScroll(
                                "Las palabras que SÍ coinciden con la Expresión Regular " + er3 + "\n\n" + palabraValida2, comp5);

                        SalidaFormateada.imprimeConScroll(
                                "Las palabras que NO coinciden con la Expresión Regular " + er3 + "\n\n" + palabraInvalida2, comp6);

                        break;
                    case 4:
                        break;
                }
            }catch (NumberFormatException e){
                JOptionPane.showMessageDialog(null,"Ingrese una opcion valida");
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
