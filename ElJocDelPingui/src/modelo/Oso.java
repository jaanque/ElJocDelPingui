package modelo;
public class Oso {
    
    // ==============================
    // ATRIBUT
    // ==============================
    private int posicion;  // POSICIÓ TAULER 
    
    // ==============================
    // CONSTRUCTOR
    // ==============================
    public Oso(int posicion) {
        this.posicion = posicion;
    }
    
    // ==============================
    // COMPORTAMENT QUAN EL JUGADOR TROBA L'ÓS
    // ==============================
    public boolean encontrarOso(Pingüino jugador) {
        if (jugador.getInventario() != null && jugador.getInventario().getpeixos() > 0) {   // SI TÉ PEIXOS DONAR-LI AL ÓS 
            jugador.getInventario().setPeixos(jugador.getInventario().getpeixos() - 1);
            return false;
        } else {
            // NO TÉ PEIXOS → TORNAR A L'INICI 
            jugador.setPosicio(0);
            return true;
        }
    }
    
    // ==============================
    // GETTERS I SETTERS
    // ==============================
    public int getPosicion() {
        return posicion;
    }
    
    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }
}
