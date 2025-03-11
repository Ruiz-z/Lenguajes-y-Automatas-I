package U3;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

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
* Lenguaje 1  --->
* Lenguaje 2  --->
* Lenguaje 3  --->
*
*
* AUTORES
* MAURO RUIZ
* ORLANDO NORIEGA
* DAVID
*
    *
    * */


    public static void main(String[] args) {

    }

    static String archivoEntrada = "";
    static List<String> palabrasDeArchivo = new ArrayList<>();
    static String archivoSalida = "";
    static List<String> palabrasValidas = new ArrayList<>();
    static List<String> Invalidas = new ArrayList<>();

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
    private  static boolean leerArchivo() {
        if(!validacionDeArchivo()) {
            JOptionPane.showMessageDialog(null, "Archivo vacio");
            return false;
        }
        try{
            FileReader fr = new FileReader(archivoEntrada);
            BufferedReader br = new BufferedReader(fr);
            String linea;

            String[] palabrasLinea = new String[0];
            while((linea = br.readLine()) != null ){
                palabrasLinea = linea.split(" [,\n]+");
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

    private static void Menu(){

    }


}
