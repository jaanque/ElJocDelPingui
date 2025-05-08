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
	
	//daus
	private int dadosRapidos = 0;
	private int dadosLentos = 0;

	private int contadorRapido = 0;
	private int contadorLento = 0;            //PER ACTUALITZAR PANTALLA 
	private int contadorPeces = 0;
	private boolean turnoBloqueado = false;


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
        case "oso":
            EventosJuego.eventoOso(this);
            break;
        case "agujero":
            EventosJuego.eventoAgujero(this);
            break;
        case "trineo":
            EventosJuego.eventoTrineo(this);
            break;
        case "interrogante":
            EventosJuego.eventoInterrogante(this);
            break;
        case "pez":
            EventosJuego.eventoPez(this);
            break;
    }

    }

    public void tornarAlInici() {
        p1Position = 0;
        GridPane.setRowIndex(P1, 0);
        GridPane.setColumnIndex(P1, 0);

        if (avatarView != null) {
            GridPane.setRowIndex(avatarView, 0);
            GridPane.setColumnIndex(avatarView, 0);
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
        if (turnoBloqueado) {
            eventos.setText("❌ Has perdut el torn per caure a un forat!");
            turnoBloqueado = false; // es desbloqueja per al següent
            return;
        }

        Random rand = new Random();
        int diceResult = rand.nextInt(6) + 1;
        dadoResultText.setText("Ha salido: " + diceResult);
        moveP1(diceResult);
    }
    
    @FXML
    private void handleRapido() {
        if (dadosRapidos > 0) {
            dadosRapidos--;
            rapido_t.setText("Dado rápido: " + dadosRapidos);

            // Avança entre 5 i 10 caselles
            int avan = new Random().nextInt(6) + 5; // [5,10]
            dadoResultText.setText("Rápido: avanzas " + avan + " casillas");
            moveP1(avan);
        } else {
            mostrarInfo("No tienes dados rápidos.");
        }
    }

    @FXML
    private void handleLento() {
        if (dadosLentos > 0) {
            dadosLentos--;
            lento_t.setText("Dado lento: " + dadosLentos);

            // Avança entre 1 i 3 caselles
            int avan = new Random().nextInt(3) + 1; // [1,3]
            dadoResultText.setText("Lento: avanzas " + avan + " casillas");
            moveP1(avan);
        } else {
            mostrarInfo("No tienes dados lentos.");
        }
    }



    @FXML private void handleNewGame() { System.out.println("New game."); }
    @FXML private void handleSaveGame() { System.out.println("Saved game."); }
    @FXML private void handleLoadGame() { System.out.println("Loaded game."); }
    @FXML private void handleQuitGame() { System.out.println("Exit..."); }
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

        // 🔁 Reiniciar daus especials
        dadosRapidos = 0;
        dadosLentos = 0;
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
            miniatura.setFitWidth(120);
            miniatura.setFitHeight(120);
            miniatura.setPreserveRatio(true);

            miniatura.setOnMouseClicked(e -> {
                avatarSeleccionado = avatar;
                ventanaSeleccion.close();
                colocarAvatarEnTablero();
            });

            avatarBox.getChildren().add(miniatura);
        }

        Scene scene = new Scene(avatarBox, 600, 200);
        ventanaSeleccion.setScene(scene);
        ventanaSeleccion.show();
    }

    private void colocarAvatarEnTablero() {
        if (avatarSeleccionado == null) return;

        if (avatarView != null) tablero.getChildren().remove(avatarView);

        avatarView = new ImageView(avatarSeleccionado);
        double size = P1.getRadius() * 2 * 1.4;
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
    
    //PER A CLASE INTERROGANT
    public void avançarCaselles(int quantitat) {
        moveP1(quantitat);
    }
    
    public void afegirDadoRapido() {
        contadorRapido++;
        rapido_t.setText("Dado rápido: " + contadorRapido);
    }

    public void afegirDadoLento() {
        contadorLento++;
        lento_t.setText("Dado lento: " + contadorLento);
    }

    public void afegirPez() {
        contadorPeces++;
        peces_t.setText("Peces: " + contadorPeces);
    }
    public void bloquejarTorn() {
        turnoBloqueado = true;
    }
    private void mostrarInfo(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    
    public void incrementarDadoLento() {
        dadosLentos++;
        lento_t.setText("Dado lento: " + dadosLentos);
    }

    public void incrementarDadoRapido() {
        dadosRapidos++;
        rapido_t.setText("Dado rápido: " + dadosRapidos);
    }


}

