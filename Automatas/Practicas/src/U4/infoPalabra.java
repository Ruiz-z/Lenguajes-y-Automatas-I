package U4;
/*
* Nos ayudara para verificar con que estamos tratando,
* ya que almacenara las palabras o tokens que se han identificado
* con nuestro codigo principal en este caso Practica 4
*
*
* */
public class infoPalabra {
private String palabra; //Contendra el lexema
private int token; //Es para idemtificar los tokens
private int identificador; // Para verifificar si es un identificador o no
private int posicion; // Como su nombre lo dice ayudara a la posicion del texto
private int  linea; //para alamcenar informacion adicional

public infoPalabra(String palabra,int token,int identificador,int posicion,int linea) {
    this.palabra = palabra;
    this.token = token;
    this.identificador = identificador;
    this.posicion = posicion;
    this.linea = linea;
}

public infoPalabra(String palabra,int token,int identificador,int posicion) {
this.palabra = palabra;
this.token = token;
this.identificador = identificador;
this.posicion = posicion;
}
public infoPalabra(String palabra, int posicion) {
    this.palabra = palabra;
    this.posicion = posicion;
}

public String getPalabra() {
    return palabra;
}
public int getToken() {
    return token;
}
public int getIdentificador() {
    return identificador;
}
public int getPosicion() {
    return posicion;
}
public int getLinea() {
    return linea;
}
public void setPalabra(String palabra) {
    this.palabra = palabra;
}
public void setToken(int token) {
    this.token = token;
}
public void setIdentificador(int identificador) {
this.identificador = identificador;
}
public void setPosicion(int posicion) {
    this.posicion = posicion;
}
@Override
    public String toString() {
        return(palabra != null && !palabra.isEmpty() ? palabra : "") + ","+
                (token != 0 ? + token : "") +","+
                ( identificador!= 0 ? + identificador : "")+","+
                (posicion != 0 ? + posicion : "");
    }

}
