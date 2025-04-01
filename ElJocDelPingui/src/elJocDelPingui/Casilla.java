package elJocDelPingui;

public class Casilla {
    
    public int posicio; // Atribut per emmagatzemar la posició de la casella

    // Constructor per inicialitzar la posició
    public Casilla(int posicio) {
        this.posicio = posicio;
    }

    // Mètode getter per obtenir la posició
    public int getPosicion() {
        return this.posicio;
    }
}
