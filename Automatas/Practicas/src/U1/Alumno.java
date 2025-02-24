package U1;

public class Alumno {
 private String Nombre;
 private int Calificaciones;
 
 
public Alumno(String nombre, int calificaciones) {
	super();
	Nombre = nombre;
	Calificaciones = calificaciones;
}


public String getNombre() {
	return Nombre;
}


public void setNombre(String nombre) {
	Nombre = nombre;
}


public int getCalificaciones() {
	return Calificaciones;
}


public void setCalificaciones(int calificaciones) {
	Calificaciones = calificaciones;
}


@Override
public String toString() {
	return "Alumno [Nombre=" + Nombre + ", Calificaciones=" + Calificaciones + "]";
}



 
}
