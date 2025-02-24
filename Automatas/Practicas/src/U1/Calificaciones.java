package U1;

import java.util.ArrayList;
import java.util.Scanner;


public class Calificaciones {
	    // Clase para representar la información de cada estudiante
	    // Distribución de trabajo: Orlando Noriega, Mauro Ruiz y David de la Fuente
	    static class Estudiante {
	        String nombre;
	        int calificacion;

	        public Estudiante(String nombre, int calificacion) {
	            this.nombre = nombre;
	            this.calificacion = calificacion;
	        }

	        @Override
	        public String toString() {
	            return "Nombre completo: " + nombre + ", Calificación: " + calificacion;
	        }
	    }

	    public static void main(String[] args) {
	        Scanner scanner = new Scanner(System.in);
	        ArrayList<Estudiante> estudiantes = new ArrayList<>();

	        while (true) {
	            System.out.println("\n--- Sistema de Calificaciones ---");
	            System.out.println("1. Agregar calificación de un alumno");
	            System.out.println("2. Consultar calificaciones");
	            System.out.println("3. Salir");
	            System.out.print("Selecciona una opción: ");

	            int opcion = 0;
	            try {
	                opcion = Integer.parseInt(scanner.nextLine());
	            } catch (NumberFormatException e) {
	                System.out.println("Por favor, introduce un número válido.");
	                continue;
	            }

	            switch (opcion) {
	                case 1:
	                    
	                    System.out.print("Nombre completo del alumno: ");
	                    String nombre = scanner.nextLine();
	                    int calificacion = -1;

	                    while (calificacion < 0 || calificacion > 10) {
	                        System.out.print("Calificación (0 a 10): ");
	                        try {
	                            calificacion = Integer.parseInt(scanner.nextLine());
	                            if (calificacion < 0 || calificacion > 10) {
	                                System.out.println("La calificación debe estar entre 0 y 10.");
	                            }
	                        } catch (NumberFormatException e) {
	                            System.out.println("Por favor, introduce un número válido.");
	                        }
	                    }

	                    estudiantes.add(new Estudiante(nombre, calificacion));
	                    System.out.println("Calificación registrada correctamente.");
	                    break;

	                case 2:
	                   
	                    if (estudiantes.isEmpty()) {
	                        System.out.println("No hay calificaciones registradas.");
	                    } else {
	                        System.out.println("\nLista de calificaciones:");
	                        for (Estudiante estudiante : estudiantes) {
	                            System.out.println(estudiante);
	                        }
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