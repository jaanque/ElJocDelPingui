package modelo;

import controlador.Inventario;

public class Pingüino {

    // ==============================
    // ATRIBUTS DEL JUGADOR
    // ==============================
    private String nom;         
    private String color;       
    private int posicio;        
    private Inventario inventario; 

    // ==============================
    // CONSTRUCTOR
    // ==============================
    public Pingüino(String nom, String color) {
        this.nom = nom;
        this.color = color;
        this.posicio = 0;              
        this.inventario = new Inventario();	 // INICIAR INVENTARIO     
    }
    
    // ==============================
    // ACTUALITZAR INVENTARI  
    // ==============================
    public void actualitzarInventari(Inventario nuevoInventario) {
        if (nuevoInventario != null) {
            this.inventario.setBolesNeu(nuevoInventario.getbolesNeu());  
            this.inventario.setPeixos(nuevoInventario.getpeixos());  
            this.inventario.setDaus(nuevoInventario.getdaus());  
        }
    }

    // ==============================
    // MOVIMENT: AVANÇAR I RETROCEDIR
    // ==============================
    public int avanzar(int casillas) {
        this.posicio += casillas;
        return this.posicio;
    }

    public int retroceder(int casillas) {
        this.posicio = Math.max(0, this.posicio - casillas); 
        return this.posicio;
    }

    // ==============================
    // GETTERS
    // ==============================
    public String getNom() { return nom; }
    public String getColor() { return color; }
    public int getPosicio() { return posicio; }
    public Inventario getInventario() { return inventario; }

    // ==============================
    // SETTERS
    // ==============================
    public void setNom(String nom) { this.nom = nom; }
    public void setColor(String color) { this.color = color; }
    public void setPosicio(int posicio) { this.posicio = posicio; }
    public void setInventario(Inventario inventario) { this.inventario = inventario; }
    
    // ==============================
    // SUELO QUEBRADIZO (NO IMPLEMENTAT)
    // ==============================
    public Object getSueloQuebradizo() { return null; }
    public void setSueloQuebradizo(Object sueloQuebradizo) { }

    // ==============================
    // REPRESENTACIÓ EN TEXT DEL JUGADOR
    // ==============================
    public String toString() {
        return "Pingüino " + color + " (" + nom + ") - " +
               "Posición: " + posicio + " - " +
               inventario.toString();
    }
}
