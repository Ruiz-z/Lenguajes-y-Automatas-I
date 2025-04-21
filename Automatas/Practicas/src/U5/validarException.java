package U5;

public class validarException extends Exception {
    private int numLinea;

    public validarException(String message) {
        super(message);
        this.numLinea = numLinea;
    }

    public int getNumLinea() {
        return numLinea;
    }
}
