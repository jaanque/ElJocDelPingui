package vista;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import modelo.EventosJuego;

import java.net.URL;
import java.util.Random;

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
    private Image avatarSeleccionado = null;
    private ImageView avatarView = null;

    @FXML
    private void initialize() {
        eventos.setText("¡El juego ha comenzado!");
        generarEventosAleatorios();
    }

    private void generarEventosAleatorios() {
        Random rand = new Random();
        String[] eventos = {"oso", "agujero", "trineo", "interrogante", "pez"};

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

        if (avatarSeleccionado != null && avatarView != null) {
            GridPane.setRowIndex(avatarView, row);
            GridPane.setColumnIndex(avatarView, col);
        }

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
        }
    }

    private void mostrarIconoEvento(String ruta, int fila, int columna) {
        try {
            Image image = new Image(ruta);
            ImageView icono = new ImageView(image);
            icono.setFitWidth(P1.getRadius() * 2);
            icono.setFitHeight(P1.getRadius() * 2);
            icono.setPreserveRatio(true);
            GridPane.setHalignment(icono, HPos.CENTER);
            GridPane.setValignment(icono, VPos.CENTER);
            tablero.add(icono, columna, fila);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDado(ActionEvent event) {
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

    @FXML
    private void handleReiniciar() {
        tablero.getChildren().removeIf(node -> node instanceof ImageView);
        p1Position = 0;
        mapaEventos = new String[ROWS][COLUMNS];

        GridPane.setRowIndex(P1, 0);
        GridPane.setColumnIndex(P1, 0);
        P1.setVisible(true);

        avatarSeleccionado = null;
        avatarView = null;

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

        HBox avatarBox = new HBox(20);
        avatarBox.setPadding(new Insets(20));

        String[] rutasAvatares = {
            "/avatars/avatar1.png",
            "/avatars/avatar2.png",
            "/avatars/avatar3.png",
            "/avatars/avatar4.png"
        };

        for (String ruta : rutasAvatares) {
            URL recurso = getClass().getResource(ruta);
            if (recurso == null) continue;

            Image avatar = new Image(recurso.toExternalForm());
            ImageView miniatura = new ImageView(avatar);
            miniatura.setFitWidth(120); // 300% más grande
            miniatura.setFitHeight(120);
            miniatura.setPreserveRatio(true);

            miniatura.setOnMouseClicked(e -> {
                avatarSeleccionado = avatar;
                ventanaSeleccion.close();
                colocarAvatarEnTablero();
            });

            avatarBox.getChildren().add(miniatura);
        }

        Scene scene = new Scene(avatarBox, 600, 200); // 300% más grande
        ventanaSeleccion.setScene(scene);
        ventanaSeleccion.show();
    }

    private void colocarAvatarEnTablero() {
        if (avatarSeleccionado == null) return;

        if (avatarView != null) tablero.getChildren().remove(avatarView);

        avatarView = new ImageView(avatarSeleccionado);
        double size = P1.getRadius() * 2 * 1.4; // 140% más grande
        avatarView.setFitWidth(size);
        avatarView.setFitHeight(size);
        avatarView.setPreserveRatio(true);

        GridPane.setHalignment(avatarView, HPos.CENTER);
        GridPane.setValignment(avatarView, VPos.CENTER);

        int row = p1Position / COLUMNS;
        int col = p1Position % COLUMNS;

        GridPane.setRowIndex(avatarView, row);
        GridPane.setColumnIndex(avatarView, col);

        tablero.getChildren().add(avatarView);
        P1.setVisible(false);
    }
} 
