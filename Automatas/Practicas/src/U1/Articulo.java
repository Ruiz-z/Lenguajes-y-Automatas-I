package U1;

public class Articulo {
	private String Nombre;
	private int Existencia;
	private double costo;

	public Articulo(String nombre, int Existencia, double costo) {
		super();
		Nombre = nombre;
		this.Existencia = Existencia;
		this.costo = costo;
	}

	public Articulo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getNombre() {
		return Nombre;
	}

	public void setNombre(String nombre) {
		Nombre = nombre;
	}

	public int getExistencia() {
		return Existencia;
	}

	public void setExistencia(int existencia) {
		Existencia = existencia;
	}

	public double getCosto() {
		return costo;
	}

	public void setCosto(double costo) {
		this.costo = costo;
	}

	@Override
	public String toString() {
		return "Articulo: \n" + "--------------Nombre------------------" + Nombre
				+ "--------------Existencia-----------" + Existencia + "-------------costo-------" + costo;
	}

}
