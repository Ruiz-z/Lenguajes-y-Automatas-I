package U1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


/* 
 * 
 * Autores:  Mauro Rodrigo Ruiz Alvarez
 * 			 Orlando Noriega
 * 			 David de la Fuente
 * 
 * PROGRAMA
 *  Realice una aplicación en el lenguaje de programación JAVA, que almacene la siguiente información en un archivo de texto
	Para cada Articulo de papeleria la siguiente información:

		Nombre del articulo   Existencia   Costo
		
 * 		El usuario podrá ingresar la cantidad de articulos que desee, cuando el usuario ya no ingrese más información, deberá guardarse en un archivo de texto.
		Además deberá mostrar la información almacenada, leyendo el archivo de texto que almaceno.
 * */
public class Almacen {

	  public static void main(String[] args) throws IOException {
	        String nombreArchivo = "almacen.txt";
	        String ruta = "C:\\Users\\Mauro\\Desktop\\Automatas\\";
	        File archivo = new File(ruta + nombreArchivo);

	        Scanner scanner = new Scanner(System.in);
	        ArrayList<Articulo> articulos = new ArrayList<>();

	        while (true) {
	            System.out.println("\n--- Sistema de Artículos ---");
	            System.out.println("1. Agregar Artículo");
	            System.out.println("2. Consultar Artículos");
	            System.out.println("3. Salir");
	            System.out.print("Selecciona una opción: ");

	            int opcion;
	            try {
	                opcion = Integer.parseInt(scanner.nextLine());
	            } catch (NumberFormatException e) {
	                System.out.println("Por favor, introduce un número válido.");
	                continue;
	            }

	            switch (opcion) {
	                case 1:
	                    String nombre;
	                    while (true) {
	                        System.out.print("Nombre del artículo: ");
	                        nombre = scanner.nextLine().trim();
	                        if (!nombre.isEmpty()) {
	                            break;
	                        }
	                        System.out.println("El nombre no puede estar vacío. Inténtalo de nuevo.");
	                    }

	                    System.out.print("Existencia: ");
	                    int existencia;
	                    try {
	                        existencia = Integer.parseInt(scanner.nextLine());
	                    } catch (NumberFormatException e) {
	                        System.out.println("Debe ser un número entero.");
	                        continue;
	                    }

	                    System.out.print("Costo: ");
	                    double costo;
	                    try {
	                        costo = Double.parseDouble(scanner.nextLine());
	                    } catch (NumberFormatException e) {
	                        System.out.println("Debe ser un número decimal.");
	                        continue;
	                    }

	                    articulos.add(new Articulo(nombre, existencia, costo));
	                    System.out.println("Artículo registrado correctamente.");

	                    try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, true))) {
	                        // Formato alineado
	                        String lineaFormateada = String.format("%-15s %-12d $%-7.2f", nombre, existencia, costo);
	                        bw.write(lineaFormateada);
	                        bw.newLine();
	                    } catch (IOException e) {
	                        System.out.println("Error al escribir en el archivo.");
	                    }
	                    break;

	                case 2:
	                    if (archivo.exists() && archivo.length() > 0) {
	                        System.out.println("\n--- Artículos Registrados ---");
	                        System.out.printf("%-15s %-12s %-8s%n", "Nombre", "Existencia", "Costo");
	                        System.out.println("----------------------------------------");

	                        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
	                            String linea;
	                            while ((linea = br.readLine()) != null) {
	                                System.out.println(linea);
	                            }
	                        } catch (IOException e) {
	                            System.out.println("Error al leer el archivo.");
	                        }
	                    } else {
	                        System.out.println("El archivo no existe o está vacío.");
	                    }
	                    break;

	                case 3:
	                    System.out.println("Saliendo del sistema...");
	                    scanner.close();
	                    return;

	                default:
	                    System.out.println("Opción inválida. Inténtalo de nuevo.");
	            }
	        }
	    }
	}