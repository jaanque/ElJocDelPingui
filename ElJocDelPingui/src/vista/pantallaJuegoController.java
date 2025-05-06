package vista;

import java.net.URL;
import java.util.Random;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import modelo.EventosJuego;

public class pantallaJuegoController {

    @FXML private MenuItem newGame, saveGame, loadGame, quitGame;
    @FXML private Button dado, rapido, lento, peces, nieve;
    @FXML private Text dadoResultText, rapido_t, lento_t, peces_t, nieve_t, eventos;
    @FXML private GridPane tablero;
    @FXML private Circle P1, P2, P3, P4;

    private int p1Position = 0;
    private final int COLUMNS = 5;
    private final int ROWS = 10;
    private String[][] mapaEventos = new String[ROWS][COLUMNS];

    @FXML
    private void initialize() {
        if (tablero == null) System.out.println("❌ tablero es null");
        if (eventos == null) System.out.println("❌ eventos es null");

        eventos.setText("¡El juego ha comenzado!");
        generarEventosAleatorios();
    }

    private void generarEventosAleatorios() {
        Random rand = new Random();
        String[] eventos = { "oso", "agujero", "trineo", "interrogante", "pez" };

        for (String evento : eventos) {
            int fila, columna;
            do {
                fila = rand.nextInt(ROWS);
                columna = rand.nextInt(COLUMNS);
            } while (mapaEventos[fila][columna] != null || (fila == 0 && columna == 0) || (fila == 9 && columna == 4));

            mapaEventos[fila][columna] = evento;

            String ruta = "/resources/" + evento + ".png";
            URL recurso = getClass().getResource(ruta);
            if (recurso != null) {
                mostrarIconoEvento(recurso.toExternalForm(), fila, columna);
            } else {
                System.out.println("❌ No se encontró la imagen: " + ruta);
            }
        }
    }

    private void moveP1(int steps) {
        p1Position += steps;
        if (p1Position >= 50) p1Position = 49;

        int row = p1Position / COLUMNS;
        int col = p1Position % COLUMNS;

        GridPane.setRowIndex(P1, row);
        GridPane.setColumnIndex(P1, col);

        activarEventoEn(row, col);
    }

    private void activarEventoEn(int fila, int columna) {
        String tipo = mapaEventos[fila][columna];
        if (tipo == null) return;

        switch (tipo) {
            case "oso": EventosJuego.eventoOso(); break;
            case "agujero": EventosJuego.eventoAgujero(); break;
            case "trineo": EventosJuego.eventoTrineo(); break;
            case "interrogante": EventosJuego.eventoInterrogante(); break;
            case "pez": EventosJuego.eventoPez(); break;
            default: System.out.println("Evento no reconocido: " + tipo);
        }
    }

    private void mostrarIconoEvento(String ruta, int fila, int columna) {
        try {
            System.out.println("Cargando imagen desde: " + ruta);
            Image image = new Image(ruta);
            if (image.isError()) {
                System.out.println("❌ No se pudo cargar la imagen desde: " + ruta);
                return;
            }

            ImageView icono = new ImageView(image);
            icono.setFitWidth(30);
            icono.setFitHeight(30);
            tablero.add(icono, columna, fila);
            System.out.println("✅ Imagen colocada en [" + fila + "," + columna + "]");
        } catch (Exception e) {
            System.out.println("❌ Excepción al cargar imagen: " + ruta);
            e.printStackTrace();
        }
    }

    @FXML private void handleDado(ActionEvent event) {
        Random rand = new Random();
        int diceResult = rand.nextInt(6) + 1;
        dadoResultText.setText("Ha salido: " + diceResult);
        moveP1(diceResult);
    }

    @FXML private void handleNewGame() { System.out.println("New game."); }
    @FXML private void handleSaveGame() { System.out.println("Saved game."); }
    @FXML private void handleLoadGame() { System.out.println("Loaded game."); }
    @FXML private void handleQuitGame() { System.out.println("Exit..."); }
    @FXML private void handleRapido() { System.out.println("Fast."); }
    @FXML private void handleLento() { System.out.println("Slow."); }
    @FXML private void handlePeces() { System.out.println("Fish."); }
    @FXML private void handleNieve() { System.out.println("Snow."); }
}
