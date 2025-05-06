package modelo;

// ==============================
// CLASSE SUELO QUEBRADIZO
// ==============================
public class SueloQuebradizo {

    // ==============================
    // ATRIBUTS
    // ==============================
    private int posicio;
    private Pingüino Jugador;

    // ==============================
    // CONSTRUCTOR DE LA CLASSE
    // ==============================
    public SueloQuebradizo (int posicio, Pingüino Jugador) {
        this.posicio = posicio;
        this.Jugador = Jugador;
    }

    // ==============================
    // ACTUALITZAR REFERÈNCIA AL JUGADOR
    // ==============================
    public void actualizarJugador (Pingüino nuevoJugador) {
        this.Jugador.setSueloQuebradizo(nuevoJugador.getSueloQuebradizo()); 
    }

    // ==============================
    // GETTERS
    // ==============================
    public int getposicio() {
        return posicio;
    }

    // ==============================
    // SETTERS
    // ==============================
    public void setposicio (int posicio) {
        this.posicio = posicio;
    }
}

