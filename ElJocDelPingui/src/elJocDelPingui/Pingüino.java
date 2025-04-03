package elJocDelPingui;

// CLASE
public class Pingüino {

    private String nom; // Nombre del jugador
    private String color; // Color del jugador
    private int posicio; // Posición del jugador
    private Inventario inventario; // Inventario del jugador

    // CONSTRUCTOR
    public Pingüino(String nom, String color) {
        this.nom = nom;
        this.color = color;
        this.posicio = 0; // Posición inicial del jugador
    }

    // MÉTODO PARA ACTUALIZAR INVENTARIO (opcional, no es la mejor forma)
    public void actualitzarInventari(Inventario nuevoInventario) {
        if (nuevoInventario != null) { // Verifica que el inventario no sea nulo
            this.inventario.setbolesNeu(nuevoInventario.getbolesNeu());  
            this.inventario.setPeixos(nuevoInventario.getpeixos());  
            this.inventario.setdaus(nuevoInventario.getdaus());  
        }
    }

    // GETTERS
    public String getNom() { return nom; }
    public String getColor() { return color; }
    public int getPosicio() { return posicio; }
    public Inventario getInventario() { return inventario; }

    // SETTERS
    public void setNom(String nom) { this.nom = nom; }
    public void setColor(String color) { this.color = color; }
    public void setPosicio(int posicio) { this.posicio = posicio; }
    public void setInventario(Inventario inventario) { this.inventario = inventario; }
}
