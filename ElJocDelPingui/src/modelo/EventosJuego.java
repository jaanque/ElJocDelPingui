package modelo;

import java.util.Random;
import javafx.scene.control.Alert;
import vista.pantallaJuegoController;

public class EventosJuego {

    private static int[][] agujerosHielo = new int[2][2];
    private static int contadorAgujeros = 0;

    public static void eventoOso(pantallaJuegoController controlador) {
        System.out.println("🐻 Evento: Oso");
        mostrarAlerta("¡Has sido atacado por un oso!", "Vuelves al inicio del juego.");
        controlador.tornarAlInici();
    }

    public static void eventoAgujero(pantallaJuegoController controlador) {
        System.out.println("🕳️ Evento: Agujero");
        mostrarAlerta("¡Te has caído en un agujero!", "Perds el proper torn.");
        controlador.bloquejarTorn();
    }

    public static void eventoTrineo(pantallaJuegoController controlador) {
        System.out.println("🛷 Evento: Trineo");
        mostrarAlerta("¡Has encontrado un trineo!", "Avanzas 3 casillas.");
        controlador.avançarCaselles(3);
    }

    public static void eventoInterrogante(pantallaJuegoController controlador) {
        System.out.println("❓ Evento: Interrogante");
        String[] possibles = {"pez", "dadoRapido", "dadoLento"};
        String seleccionat = possibles[new Random().nextInt(possibles.length)];

        switch (seleccionat) {
            case "pez":
                mostrarAlerta("¡Evento aleatorio!", "Has trobat un PEIX!");
                controlador.afegirPez();
                break;
            case "dadoRapido":
                mostrarAlerta("¡Evento aleatorio!", "Has guanyat un DAU RÀPID!");
                controlador.incrementarDadoRapido();
                break;
            case "dadoLento":
                mostrarAlerta("¡Evento aleatorio!", "Has guanyat un DAU LENT!");
                controlador.incrementarDadoLento();
                break;
        }
    }

    public static void eventoPez(pantallaJuegoController controlador) {
        System.out.println("🐟 Evento: Pez");
        mostrarAlerta("¡Has encontrado un pez!", "Ganas un objeto.");
        controlador.afegirPez();
    }

    public static void eventoAgujeroHielo(pantallaJuegoController controlador) {
        System.out.println("🧊 Evento: Agujero en el hielo");

        int actualPos = controlador.getP1Position();
        int nuevaPos = -1;

        for (int i = contadorAgujeros - 1; i >= 0; i--) {
            int fila = agujerosHielo[i][0];
            int col = agujerosHielo[i][1];
            int pos = fila * 5 + col;

            if (pos < actualPos) {
                nuevaPos = pos;
                break;
            }
        }

        if (nuevaPos == -1) {
            nuevaPos = Math.max(actualPos - 5, 0);
            mostrarAlerta("¡Agujero en el hielo!", "No hay otro agujero antes. Retrocedes 5 casillas.");
        } else {
            mostrarAlerta("¡Agujero en el hielo!", "Retrocedes hasta el agujero anterior.");
        }

        controlador.setP1Position(nuevaPos);
        controlador.actualitzarPosicioJugador();
    }

    public static void registrarAgujeroHielo(int fila, int columna) {
        if (contadorAgujeros < 2) {
            agujerosHielo[contadorAgujeros][0] = fila;
            agujerosHielo[contadorAgujeros][1] = columna;
            contadorAgujeros++;
        }
    }
    
    public static void eventoNieve(pantallaJuegoController controlador) {
        System.out.println("❄️ Evento: Bola de nieve");
        mostrarAlerta("¡Has encontrado una bola de nieve!", "Se añade al inventario.");
        controlador.incrementarBolaNieve();
    }


    private static void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Evento");
        alert.setHeaderText(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
