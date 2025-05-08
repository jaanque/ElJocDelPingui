package modelo;

import java.util.Random;

import javafx.scene.control.Alert;
import vista.pantallaJuegoController;

public class EventosJuego {

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
        Random rand = new Random();
        String seleccionat = possibles[rand.nextInt(possibles.length)];

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
        controlador.afegirPez(); // Actualitza el comptador de peixos
    }

    private static void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Evento");
        alert.setHeaderText(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
