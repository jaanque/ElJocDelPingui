package controlador;

public class Inventario {

    // ==============================
    // ATRIBUTS D'INVENTARI
    // ==============================
    private int bolesNeu;   
    private int daus;
    private int peixos;        

    // ==============================
    // CONSTANTS MÀXIMS D'OBJECTES
    // ==============================
    public static final int MAX_BOLES_NEU = 6;
    public static final int MAX_DAUS = 3;
    public static final int MAX_PEIXOS = 2;
    
    // ==============================
    // CONSTRUCTOR AMB PARÀMETRES
    // ==============================
    public Inventario(int bolesNeu, int daus, int peixos) {
        this.bolesNeu = Math.min(bolesNeu, MAX_BOLES_NEU);
        this.daus = Math.min(daus, MAX_DAUS);
        this.peixos = Math.min(peixos, MAX_PEIXOS);
    }

    // ==============================
    // CONSTRUCTOR PER DEFECTE (0 OBJECTES)
    // ==============================
    public Inventario() {	
        this(0, 0, 0);
    }

    // ==============================
    // GETTERS 
    // ==============================
    public int getbolesNeu() {
        return bolesNeu;
    }
    
    public int getdaus() {
        return daus;
    }
    
    public int getpeixos() {
        return peixos;
    }
    
    // ==============================
    // SETTERS
    // ==============================
    public void setbolesNeu(int bolesNeu) {
        this.bolesNeu = Math.min(bolesNeu, MAX_BOLES_NEU); 
    }
    
    public void setdaus(int daus) {
        this.daus = Math.min(daus, MAX_DAUS);
    }
    
    public void setPeixos(int peixos) {
        this.peixos = Math.min(peixos, MAX_PEIXOS);
    }

    // ==============================
    // TOTAL OBJECTES DE L'INVENTARI
    // ==============================
    public int getTotalObjetos() {
        return bolesNeu + daus + peixos;
    }

    // ==============================
    // REPRESENTACIÓ EN TEXT DE L'INVENTARI
    // ==============================
    public String toString() {
        return "Inventario: " +
               bolesNeu + " bolas de nieve, " +
               daus + " dados, " +
               peixos + " peces";
    }
}
