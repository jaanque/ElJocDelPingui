package elJocDelPingui;

import java.util.List;

public class Tablero {
    private int filas;
    private int columnas;
    private Casilla[][] casillas;
    private List<Pingüino> jugadores;
    
    // Constructor
    public Tablero(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        this.casillas = new Casilla[filas][columnas];
    }
    
    // Getters
    public int getFilas() {
        return filas;
    }

    public void setFilas(int filas) {
        this.filas = filas;
    }

    public int getColumnas() {
        return columnas;
    }

    public void setColumnas(int columnas) {
        this.columnas = columnas;
    }

    public Casilla[][] getCasillas() {
        return casillas;
    }
    
    
    // Setters
    public void setCasillas(Casilla[][] casillas) {
        this.casillas = casillas;
    }

    public List<Pingüino> getJugadores() {
        return jugadores;
    }

    public void setJugadores(List<Pingüino> jugadores) {
        this.jugadores = jugadores;
    }
}
