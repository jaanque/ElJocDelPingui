package elJocDelPingui;
public class Oso {
    
    private int posicion;  //POSICIÓ TAULER 
    
    public Oso(int posicion) {
        this.posicion = posicion;
    }
    
    public boolean encontrarOso(Pingüino jugador) {
        if (jugador.getInventario() != null && jugador.getInventario().getpeixos() > 0) {   //SI TE PEIXOS DONARLI AL OS 
            jugador.getInventario().setPeixos(jugador.getInventario().getpeixos() - 1);
            return false;
        } else {
            //NO PEIXOS TORNAR INICI 
            jugador.setPosicio(0);
            return true;
        }
    }
    
    // Getters y setters
    public int getPosicion() {
        return posicion;
    }
    
    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }
}