package modelo;
import java.util.Random;
public class CasillaInterrogante extends Casilla {
    private Random random;

    public CasillaInterrogante(int posicion) {
        super(posicion);
        this.random = new Random();
    }
    
    public String activarEvento(Pingüino jugador) {
        // Generamos un número aleatorio para decidir qué evento activar
        int evento = random.nextInt(4); // 0-3 para los 4 tipos de eventos
        
        switch (evento) {
            case 0:
                // Obtener un pez
                if (jugador.getInventario().getpeixos() < 2) { // Máximo 2 peces
                    jugador.getInventario().setPeixos(jugador.getInventario().getpeixos() + 1);
                    return "¡Has obtenido un pez!";
                } else {
                    // Si ya tiene el máximo de peces, damos bolas de nieve
                    return activarEvento(jugador); // Recursivo para obtener otro evento
                }
                
            case 1:
                // Obtener 1-3 bolas de nieve
                int bolasDadas = random.nextInt(3) + 1; // 1-3 bolas
                int bolasActuales = jugador.getInventario().getbolesNeu();
                
                // Verificar que no exceda el máximo de 6 bolas
                if (bolasActuales + bolasDadas > 6) {
                    bolasDadas = 6 - bolasActuales;
                }
                
                if (bolasDadas > 0) {
                    jugador.getInventario().setbolesNeu(bolasActuales + bolasDadas);
                    return "¡Has obtenido " + bolasDadas + " bolas de nieve!";
                } else {
                    return activarEvento(jugador); // Si ya tiene 6 bolas, damos otro evento
                }
                
            case 2:
                // Obtener un dado rápido (probabilidad baja)
                if (random.nextInt(10) < 3) { // 30% de probabilidad
                    if (jugador.getInventario().getdaus() < 3) { // Máximo 3 dados
                        jugador.getInventario().setdaus(jugador.getInventario().getdaus() + 1);
                        return "¡Has obtenido un dado rápido!";
                    } else {
                        return activarEvento(jugador); // Si ya tiene 3 dados, damos otro evento
                    }
                } else {
                    return activarEvento(jugador); // No se cumple la probabilidad, intentamos otro evento
                }
                
            case 3:
                // Obtener un dado lento (probabilidad alta)
                if (random.nextInt(10) < 7) { // 70% de probabilidad
                    if (jugador.getInventario().getdaus() < 3) { // Máximo 3 dados
                        jugador.getInventario().setdaus(jugador.getInventario().getdaus() + 1);
                        return "¡Has obtenido un dado lento!";
                    } else {
                        return activarEvento(jugador); // Si ya tiene 3 dados, damos otro evento
                    }
                } else {
                    return activarEvento(jugador); // No se cumple la probabilidad, intentamos otro evento
                }
                
            default:
                return "¡No ha ocurrido nada!";
        }
    }
}