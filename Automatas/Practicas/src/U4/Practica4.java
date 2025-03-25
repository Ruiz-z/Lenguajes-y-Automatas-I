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
        Pattern er = Pattern.compile(
                "(\\\".*?\\\")|" +                   // Constante String
                        "(/\\*.*?\\*/)|" +                   // Comentarios
                        "(\\d+\\.\\d+)|" +                 // Números decimales
                        "(\\d+)|" +                       // Números enteros
                        "(\"&&|\\\\\\\\|\\\\\\\\||!\")|" +                   // Operadores lógicos
                        "(\\\\+|-|\\\\*|/|=)|" +                    // Operadores aritméticos
                        "(==|!=|<=|>=|<|>)|" +                // Operadores relacionales
                        "([()\\[\\],;:])|" +                 // Caracteres especiales
                        "(program|begin|end|read|write|int|real|string|bool|if|else|then|while|do|repeat|until|var|procedure|function|array|true|false)|" + // Palabras reservadas
                        "([a-z][a-zA-Z0-9_]*|\\\\s+)"         // Identificadores
        );
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
        }//no se encontro coincidencia
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
    
     private static void esCaracterEspecial(infoPalabra palabra) {
        String cE = palabra.getPalabra();
        if (cE.matches("[()\\[\\],;:]")) {
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
        if (nE.matches("(\\d+)")) {
            palabra.setIdentificador(-1);
            palabra.setToken(-61);
        }
    }

    private static void esNumerosDecimales(infoPalabra palabra) {
        String nD = palabra.getPalabra();
        if (nD.matches("(\\d+\\.\\d+)")) {
            palabra.setIdentificador(-1);
            palabra.setToken(-62);
        }
    }

    private static void esConstanteString(infoPalabra palabra) {
        String cS = palabra.getPalabra();
        if (cS.matches("(\\\".*?\\\")")) {
            palabra.setIdentificador(-1);
            palabra.setToken(-63);
        }
    }

    private static void esOperadorAritmeticos(infoPalabra palabra) {
        String OpM = palabra.getPalabra();
        if (OpM.matches("(\\\\+|-|\\\\*|/|%|=)")) {
            palabra.setIdentificador(-1);
            switch (OpM) {
                case "+":
                    palabra.setToken(-26);
                    break;
                case "-":
                    palabra.setToken(-27);
                    break;
                case "*":
                    palabra.setToken(-23);
                    break;
                case "/":
                    palabra.setToken(-24);
                    break;
                case "%":
                    palabra.setToken(-25);
                    break;
                case "=":
                    palabra.setToken(-32);
                    break;
                default:
                    break;

            }
        }
    }

    private static void esOperadorRelacional(infoPalabra palabra) {
        String OpL = palabra.getPalabra();
        if (OpL.matches("(==|!=|<=|>=|<|>)")) {
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
        if (OpL.matches(("&&|\\\\|\\\\||!"))) {
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
        //A los comentarios no se le ponen token porque es no es una accion como tal
        if (comentario.matches("(/\\*.*?\\*/)")) {
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
            esOperadorAritmeticos(palabra);
            esOperadorLogico(palabra);
            esOperadorRelacional(palabra);
            esComentarios(palabra);
        }
    }

    private static void erroresLexico() {
        for (infoPalabra palabra : palabrasArchivo){
            if (palabra.getToken() == 0){
                    erroresArchivo.addLast(new infoPalabra(palabra.getPalabra().trim(), palabra.getToken(), palabra.getIdentificador(), palabra.getPosicion()));
                }
            }
        }

    private static void tablaTokens() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("C:/Users/Mauro/Desktop/.A/Automatas/src/U4/Tabla de Tokens.txt"));
            for (infoPalabra palabra : palabrasArchivo) {
                if (palabra.getToken() != 0) {
                    bw.write(palabra.getPalabra() + "\t" + palabra.getToken() + "\t" + palabra.getIdentificador() + "\t" + palabra.getPosicion());
                    bw.newLine();
                }
            }
            bw.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Problemas para escribir el archivo de tokens");
        }
    }

    public static void tablaErrores(){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("C:/Users/Mauro/Desktop/.A/Automatas/src/U4/Tabla de errores.txt"));
            for (infoPalabra palabra : erroresArchivo) {
                bw.write("Error en la línea: " + palabra.getPosicion() + ". En la palabra: " + palabra.getPalabra());
                bw.newLine();
            }
            bw.close();
        } catch (IOException e){
            JOptionPane.showMessageDialog(null, "Problemas para escribir el archivo de errores");
        }
    }

    private static void escribirArchivo() {
        if (erroresArchivo.isEmpty()){
            tablaTokens();
        }else {
            tablaErrores();
        }
    }
}
