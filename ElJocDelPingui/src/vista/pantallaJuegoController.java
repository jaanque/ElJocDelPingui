package vista;

import java.io.InputStream;
import java.net.URL;
import java.util.Random;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import modelo.EventosJuego;

public class pantallaJuegoController {

    @FXML private MenuItem newGame, saveGame, loadGame, quitGame;
    @FXML private Button dado, rapido, lento, peces, nieve, reiniciar, cambiarAvatarBtn;
    @FXML private Text dadoResultText, rapido_t, lento_t, peces_t, nieve_t, eventos;
    @FXML private GridPane tablero;
    @FXML private Circle P1, P2, P3, P4;

    private int p1Position = 0;
    private final int COLUMNS = 5;
    private final int ROWS = 10;
    private String[][] mapaEventos = new String[ROWS][COLUMNS];

    private ImageView avatarView = null; // Para avatar si se elige

    @FXML
    private void initialize() {
        eventos.setText("¡El juego ha comenzado!");
        generarEventosAleatorios();
        posicionarFicha(P1, 0, 0);
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

        posicionarFicha(P1, row, col);
        if (avatarView != null) {
            posicionarAvatar(row, col);
        }

        activarEventoEn(row, col);
    }

    private void posicionarFicha(Circle ficha, int row, int col) {
        GridPane.setRowIndex(ficha, row);
        GridPane.setColumnIndex(ficha, col);
    }

    private void posicionarAvatar(int row, int col) {
        GridPane.setRowIndex(avatarView, row);
        GridPane.setColumnIndex(avatarView, col);
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
            Image image = new Image(ruta);
            ImageView icono = new ImageView(image);
            icono.setFitWidth(30);
            icono.setFitHeight(30);
            tablero.add(icono, columna, fila);
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

    @FXML private void handleNewGame()  { System.out.println("New game."); }
    @FXML private void handleSaveGame() { System.out.println("Saved game."); }
    @FXML private void handleLoadGame() { System.out.println("Loaded game."); }
    @FXML private void handleQuitGame() { System.out.println("Exit..."); }
    @FXML private void handleRapido()   { System.out.println("Fast."); }
    @FXML private void handleLento()    { System.out.println("Slow."); }
    @FXML private void handlePeces()    { System.out.println("Fish."); }
    @FXML private void handleNieve()    { System.out.println("Snow."); }

    @FXML
    private void handleReiniciar() {
        tablero.getChildren().removeIf(node -> node instanceof ImageView && node != avatarView);
        p1Position = 0;
        mapaEventos = new String[ROWS][COLUMNS];
        posicionarFicha(P1, 0, 0);
        if (avatarView != null) posicionarAvatar(0, 0);
        dadoResultText.setText("Ha salido: ");
        rapido_t.setText("Dado rápido: 0");
        lento_t.setText("Dado lento: 0");
        peces_t.setText("Peces: 0");
        nieve_t.setText("Bolas de nieve: 0");
        eventos.setText("¡Nueva partida iniciada!");
        generarEventosAleatorios();
    }

    @FXML
    private void handleCambiarAvatar() {
        Stage ventanaSeleccion = new Stage();
        ventanaSeleccion.setTitle("Selecciona un nuevo avatar");

        HBox avatarBox = new HBox(10);
        avatarBox.setPadding(new Insets(10));

        String[] rutasAvatares = {
            "/avatars/avatar1.png",
            "/avatars/avatar2.png",
            "/avatars/avatar3.png",
            "/avatars/avatar4.png"
        };

        for (String ruta : rutasAvatares) {
            InputStream stream = getClass().getResourceAsStream(ruta);
            if (stream == null) {
                System.out.println("❌ No se encontró la imagen: " + ruta);
                continue;
            }
            Image avatar = new Image(stream);
            ImageView miniatura = new ImageView(avatar);
            miniatura.setFitWidth(80);
            miniatura.setFitHeight(80);
            miniatura.setOnMouseClicked(e -> {
                System.out.println("✅ Avatar seleccionado: " + ruta);

                if (avatarView != null) tablero.getChildren().remove(avatarView);

                avatarView = new ImageView(avatar);
                avatarView.setFitWidth(45);
                avatarView.setFitHeight(45);
                posicionarAvatar(p1Position / COLUMNS, p1Position % COLUMNS);
                tablero.getChildren().add(avatarView);

                P1.setVisible(false); // ocultar círculo azul
                ventanaSeleccion.close();
            });
            avatarBox.getChildren().add(miniatura);
        }

        Scene scene = new Scene(avatarBox, 400, 120);
        ventanaSeleccion.setScene(scene);
        ventanaSeleccion.show();
    }
}
