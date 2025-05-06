package modelo;

import javafx.scene.control.Alert;

public class EventosJuego {

    public static void eventoOso() {
        System.out.println("🐻 Evento: Oso");
        mostrarAlerta("¡Has sido atacado por un oso!", "Vuelves al inicio del juego.");
        // Aquí puedes añadir lógica para reiniciar la posición del jugador
    }

    public static void eventoAgujero() {
        System.out.println("🕳️ Evento: Agujero");
        mostrarAlerta("¡Te has caído en un agujero!", "Pierdes un turno.");
        // Puedes desactivar el botón de dado por un turno, por ejemplo
    }

    public static void eventoTrineo() {
        System.out.println("🛷 Evento: Trineo");
        mostrarAlerta("¡Has encontrado un trineo!", "Avanzas 3 casillas.");
        // Retorna 3 si quieres mover automáticamente al jugador
    }

    public static void eventoInterrogante() {
        System.out.println("❓ Evento: Interrogante");
        mostrarAlerta("¡Evento aleatorio!", "Pasa algo inesperado...");
        // Aquí puedes meter un Random para que llame a otro evento aleatorio
    }

    public static void eventoPez() {
        System.out.println("🐟 Evento: Pez");
        mostrarAlerta("¡Has encontrado un pez!", "Ganas un objeto.");
        // Aquí podrías sumar 1 al inventario de peces
    }

    private static void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Evento");
        alert.setHeaderText(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
