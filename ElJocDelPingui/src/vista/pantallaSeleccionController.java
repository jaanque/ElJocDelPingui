package vista;

// Imports necessaris per a controladors, interfície gràfica i accés a dades
import controlador.Bdades;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import modelo.Partida;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import controlador.Sessio;

public class pantallaSeleccionController {

    // ------------------------------
    // Components de la interfície (FXML)
    // ------------------------------
    @FXML private Button btnNuevaPartida;
    @FXML private Button btnCargarPartida;
    @FXML private ComboBox<String> comboPartides;

    // ------------------------------
    // Variables internes
    // ------------------------------
    private Connection con;
    private String jugadorActual;
    private List<Partida> partidesJugador = new ArrayList<>();

    // ------------------------------
    // Inicialització de la pantalla
    // ------------------------------
    @FXML
    public void initialize() {
        // Connecta amb la base de dades
        con = Bdades.conectarBaseDatos(con);

        // Obté les dades del jugador que ha iniciat sessió
        int idJugador = controlador.Sessio.getIdJugador();
        String nickname = controlador.Sessio.getNickname();

        // Carrega les partides guardades d’aquest jugador
        carregarPartidesJugador(idJugador);
    }

    // ------------------------------
    // Carrega les partides del jugador des de la BD
    // ------------------------------
    private void carregarPartidesJugador(int idJugador) {
        partidesJugador.clear();               // Neteja la llista actual
        comboPartides.getItems().clear();      // Neteja el desplegable

        // Consulta SQL per obtenir les partides associades al jugador
        String sql = "SELECT pj.ID_PARTIDA, p.FECHA, p.HORA FROM PARTIDAS_JUGADORES pj " +
                     "JOIN PARTIDAS p ON pj.ID_PARTIDA = p.ID_PARTIDA " +
                     "WHERE pj.ID_JUGADOR = " + idJugador + " ORDER BY p.FECHA DESC, p.HORA DESC";

        ResultSet rs = Bdades.select(con, sql);

        try {
            while (rs.next()) {
                int id = rs.getInt("ID_PARTIDA");
                String dataHora = rs.getString("FECHA") + " " + rs.getString("HORA");

                // Crea l'objecte Partida i l’afegeix a la llista i al ComboBox
                Partida partida = new Partida(id, dataHora);
                partidesJugador.add(partida);
                comboPartides.getItems().add("Partida del " + dataHora);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mostrarError("Error", "No s'han pogut carregar les partides.");
        }
    }

    // ------------------------------
    // Iniciar nova partida
    // ------------------------------
    @FXML
    private void handleNuevaPartida() {
        carregarPantallaJoc(null); // Es passa null perquè no és una partida guardada
    }

    // ------------------------------
    // Carregar una partida existent
    // ------------------------------
    @FXML
    private void handleCargarPartida() {
        int index = comboPartides.getSelectionModel().getSelectedIndex();
        if (index >= 0) {
            Partida partidaSeleccionada = partidesJugador.get(index);
            carregarPantallaJoc(partidaSeleccionada);
        } else {
            mostrarError("Selecció requerida", "Selecciona una partida de la llista.");
        }
    }

    // ------------------------------
    // Carrega la pantalla del joc amb o sense partida
    // ------------------------------
    private void carregarPantallaJoc(Partida partida) {
        try {
            // Carrega l’FXML de la pantalla de joc
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/pantallaJuego.fxml"));
            Parent root = loader.load();
            pantallaJuegoController controlador = loader.getController();

            // Passa la connexió i ID del jugador al controlador del joc
            int idJugador = Sessio.getIdJugador();
            controlador.setDatosConexionYJugador(con, idJugador);

            // Si hi ha una partida seleccionada, la carrega
            if (partida != null) {
                controlador.carregarPartidaDesDeBD(partida.getId());
            }

            // Canvia l’escena per mostrar la pantalla del joc
            Stage stage = (Stage) btnNuevaPartida.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarError("Error", "No s'ha pogut carregar la pantalla del joc.");
        }
    }

    // ------------------------------
    // Mostra un missatge d’error
    // ------------------------------
    private void mostrarError(String titol, String missatge) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titol);
        alert.setHeaderText(null);
        alert.setContentText(missatge);
        alert.showAndWait();
    }
}

