package U4;

import javax.swing.*;
import java.io.*;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Practica4 {

    static String entrada = "./U4/Practica4.txt";
    static LinkedList<infoPalabra> palabrasArchivo = new LinkedList<infoPalabra>();
    static LinkedList<infoPalabra> erroresArchivo = new LinkedList<infoPalabra>();

    public static void main(String[] args) {

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
            String linea = br.readLine();

            int numLinea = 0;
            while ((linea = br.readLine()) != null) {
                numLinea++;
                lectura(linea, numLinea);
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error al leer el archivo");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al leer el archivo");
        }
        return false;
    }

    private static void lectura(String linea, int numLinea) {
        Pattern er = Pattern.compile("(\\b[a-z][a-zA-Z0-9_]*\\b)|(-?\\d+\\.\\d+)|(-?\\d+)|(\\\".*?\\\")|(/\\*.*?\\*/)|" +
                "(program|begin|end|read|write|int|real|string|bool|if|else|then|while|do|repeat|until|var|procedure|function|array|true|false)|" + // Palabras reservadas
                "([+\\-*/%=])|(==|!=|<=|>=|<|>)|(\\|\\||&&|!)|([()\\[\\],;:])"); //operadores
        Matcher matcher = er.matcher(linea);
        int start = 0;
        while (matcher.find()) {
            if (matcher.start() != start) {
                palabrasArchivo.addLast(new infoPalabra(linea.substring(start, matcher.start()).trim(), numLinea));
            }
            if (!matcher.group().matches("\\s+")) {
                palabrasArchivo.addLast(new infoPalabra(matcher.group(), numLinea));
            }
            start = matcher.end();
        }//No se encuentra nada
        if (start != linea.length()) {
            palabrasArchivo.addLast(new infoPalabra(linea.substring(start).trim(), numLinea));

        }
    }

    private static void esIdentificadores(infoPalabra palabra) {
        String identificador = palabra.getPalabra();
        if (identificador.matches("[a-z][a-zA-Z0-9_]*")) {
            palabra.setIdentificador(-2);
        }
    }

    private static void esPalabraReservada(infoPalabra palabra) {
        String pRes = palabra.getPalabra();
        if (pRes.matches("program|begin|end|read|write|int|real|string|bool|if|else|then|while|do|repeat|until|var|procedure|function|array|true|false")) {
            palabra.setIdentificador(-1);

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


            }
        }

    }
}

