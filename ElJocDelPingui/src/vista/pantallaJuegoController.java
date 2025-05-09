package vista;

import controlador.GestorPartides;
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
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import modelo.EventosJuego;
import java.sql.ResultSet;


import java.net.URL;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.Random;

public class pantallaJuegoController {

    private int dadosRapidos = 0;
    private int dadosLentos = 0;
    private int contadorRapido = 0;
    private int contadorLento = 0;
    private int contadorPeces = 0;
    private boolean turnoBloqueado = false;

    private Connection con;
    private int idJugador;
    private int idPartidaActual = -1;

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
    private int contadorBolasNieve = 0;

    public void setDatosConexionYJugador(Connection con, int idJugador) {
        this.con = con;
        this.idJugador = idJugador;
    }
    
    public void carregarPartidaDesDeBD(int idPartida) {
        this.idPartidaActual = idPartida;
        GestorPartides.carregar(con, this, idJugador, idPartida);
    }

    public void carregarPartida(int idPartida) {
        GestorPartides.carregar(con, this, idJugador, idPartida);
    }

    @FXML
    private void initialize() {
        ajustarInterficie75();
        eventos.setText("¡El juego ha comenzado!");
        generarEventosAleatorios();
    }

    private void ajustarInterficie75() {
        tablero.setScaleX(0.75);
        tablero.setScaleY(0.75);
        P1.setRadius(P1.getRadius() * 0.75);
        P2.setRadius(P2.getRadius() * 0.75);
        P3.setRadius(P3.getRadius() * 0.75);
        P4.setRadius(P4.getRadius() * 0.75);
        dadoResultText.setFont(new Font(24));
        rapido_t.setFont(new Font(22));
        lento_t.setFont(new Font(22));
        peces_t.setFont(new Font(22));
        nieve_t.setFont(new Font(22));
        eventos.setFont(new Font(18));
    }

    private void generarEventosAleatorios() {
        Random rand = new Random();
        String[] eventos = {"oso", "agujero", "trineo", "interrogante", "pez", "agujeroHielo", "nieve"};

        int totalPorTipo = 2;

        for (String evento : eventos) {
            int colocados = 0;
            while (colocados < totalPorTipo) {
                int fila = rand.nextInt(ROWS);
                int columna = rand.nextInt(COLUMNS);
                if (mapaEventos[fila][columna] == null &&
                    !(fila == 0 && columna == 0) &&
                    !(fila == 9 && columna == 4)) {

                    mapaEventos[fila][columna] = evento;

                    String ruta = "/resources/" + evento + ".png";
                    URL recurso = getClass().getResource(ruta);
                    if (recurso != null) {
                        mostrarIconoEvento(recurso.toExternalForm(), fila, columna);
                    }
                    colocados++;
                }
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
            case "oso": EventosJuego.eventoOso(this); break;
            case "agujero": EventosJuego.eventoAgujero(this); break;
            case "trineo": EventosJuego.eventoTrineo(this); break;
            case "interrogante": EventosJuego.eventoInterrogante(this); break;
            case "pez": EventosJuego.eventoPez(this); break;
            case "agujeroHielo": EventosJuego.eventoAgujeroHielo(this); break;
            case "nieve": EventosJuego.eventoNieve(this); break;
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

    @FXML private void handleGuardarPartida() {
        GestorPartides.guardar(con, this, idJugador, LocalDateTime.now());
    }

    @FXML private void handleCargarPartida() {
        mostrarInfo("Usa la pantalla anterior per carregar una partida.");
    }

    @FXML private void handleDado(ActionEvent event) {
        if (turnoBloqueado) {
            eventos.setText("❌ Has perdut el torn!");
            turnoBloqueado = false;
            return;
        }
        int result = new Random().nextInt(6) + 1;
        dadoResultText.setText("Ha salido: " + result);
        moveP1(result);
    }

    @FXML private void handleRapido() {
        if (dadosRapidos > 0) {
            dadosRapidos--;
            rapido_t.setText("Dado rápido: " + dadosRapidos);
            int avan = new Random().nextInt(6) + 5;
            dadoResultText.setText("Rápido: avanzas " + avan + " casillas");
            moveP1(avan);
        } else {
            mostrarInfo("No tienes dados rápidos.");
        }
    }

    @FXML private void handleLento() {
        if (dadosLentos > 0) {
            dadosLentos--;
            lento_t.setText("Dado lento: " + dadosLentos);
            int avan = new Random().nextInt(3) + 1;
            dadoResultText.setText("Lento: avanzas " + avan + " casillas");
            moveP1(avan);
        } else {
            mostrarInfo("No tienes dados lentos.");
        }
    }

    @FXML private void handleNewGame() {}
    @FXML private void handleLoadGame() {}
    @FXML private void handleQuitGame() {}
    @FXML private void handlePeces() {}
    @FXML private void handleNieve() {}

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
        dadosRapidos = dadosLentos = contadorRapido = contadorLento = contadorPeces = 0;
        rapido_t.setText("Dado rápido: 0");
        lento_t.setText("Dado lento: 0");
        peces_t.setText("Peces: 0");
        nieve_t.setText("Bolas de nieve: 0");
        eventos.setText("¡Nueva partida iniciada!");
        generarEventosAleatorios();
    }

    public void avançarCaselles(int quantitat) { moveP1(quantitat); }
    public void afegirDadoRapido() { contadorRapido++; rapido_t.setText("Dado rápido: " + contadorRapido); }
    public void afegirDadoLento() { contadorLento++; lento_t.setText("Dado lento: " + contadorLento); }
    public void afegirPez() { contadorPeces++; peces_t.setText("Peces: " + contadorPeces); }
    public void bloquejarTorn() { turnoBloqueado = true; }
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
    
    public void incrementarBolaNieve() {
        contadorBolasNieve++;
        nieve_t.setText("Bolas de nieve: " + contadorBolasNieve);
    }
    
    public static void guardar(Connection con, pantallaJuegoController controlador, int idJugador, LocalDateTime timestamp) {
        int posicio = controlador.getP1Position();
        String inventari = "R:" + controlador.getDadosRapidos() +
                ",L:" + controlador.getDadosLentos() +
                ",P:" + controlador.getPeces() +
                ",N:" + controlador.getBolasNieve(); 
        GestorPartides.guardarPartida(con, idJugador, posicio, inventari);
    }
    
    public static void carregar(Connection con, pantallaJuegoController controlador, int idJugador, int idPartida) {
        try {
            ResultSet rs = GestorPartides.carregarPerId(con, idJugador, idPartida);
            if (rs.next()) {
                controlador.setP1Position(rs.getInt("POSICION_ACTUAL"));

                String inventari = rs.getString("INVENTARIO");
                if (inventari != null) {
                    for (String part : inventari.split(",")) {
                        String[] parell = part.split(":");
                        switch (parell[0]) {
                        case "R": controlador.setDadosRapidos(Integer.parseInt(parell[1])); break;
                        case "L": controlador.setDadosLentos(Integer.parseInt(parell[1])); break;
                        case "P": controlador.setPeces(Integer.parseInt(parell[1])); break;
                        case "N": controlador.setBolasNieve(Integer.parseInt(parell[1])); break; // <--- afegit
                        }
                    }
                }

                controlador.actualitzarPosicioJugador();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // GETTERS/SETTERS per guardar i carregar
    public int getP1Position() { return p1Position; }
    public void setP1Position(int pos) { p1Position = pos; }
    public String[][] getMapaEventos() { return mapaEventos; }
    public void setMapaEventos(String[][] mapa) { mapaEventos = mapa; }
    public int getDadosRapidos() { return dadosRapidos; }
    public void setDadosRapidos(int val) { dadosRapidos = val; }
    public int getDadosLentos() { return dadosLentos; }
    public void setDadosLentos(int val) { dadosLentos = val; }
    public int getPeces() { return contadorPeces; }
    public void setPeces(int val) { contadorPeces = val; }
    public void actualitzarPosicioJugador() { moveP1(0); }
    public int getBolasNieve() { return contadorBolasNieve; }
    public void setBolasNieve(int val) { contadorBolasNieve = val; nieve_t.setText("Bolas de nieve: " + contadorBolasNieve);}
}
