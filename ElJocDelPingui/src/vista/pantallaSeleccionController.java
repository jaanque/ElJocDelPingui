package vista;

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

public class pantallaSeleccionController {

    @FXML private Button btnNuevaPartida;
    @FXML private Button btnCargarPartida;
    @FXML private ComboBox<String> comboPartides;

    private Connection con;
    private String jugadorActual;

    private List<Partida> partidesJugador = new ArrayList<>();

    @FXML
    public void initialize() {
        con = Bdades.conectarBaseDatos(con);

        int idJugador = controlador.Sessio.getIdJugador();      // ✅ obtingut de la sessió
        String nickname = controlador.Sessio.getNickname();     // ✅ obtingut de la sessió

        carregarPartidesJugador(idJugador); // Ara li passes l’ID del jugador
    }


    private void carregarPartidesJugador(int idJugador) {
        partidesJugador.clear();
        comboPartides.getItems().clear();

        String sql = "SELECT pj.ID_PARTIDA, p.FECHA, p.HORA FROM PARTIDAS_JUGADORES pj " +
                     "JOIN PARTIDAS p ON pj.ID_PARTIDA = p.ID_PARTIDA " +
                     "WHERE pj.ID_JUGADOR = " + idJugador + " ORDER BY p.FECHA DESC, p.HORA DESC";

        ResultSet rs = Bdades.select(con, sql);

        try {
            while (rs.next()) {
                int id = rs.getInt("ID_PARTIDA");
                String dataHora = rs.getString("FECHA") + " " + rs.getString("HORA");
                Partida partida = new Partida(id, dataHora);
                partidesJugador.add(partida);
                comboPartides.getItems().add("Partida del " + dataHora);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mostrarError("Error", "No s'han pogut carregar les partides.");
        }
    }


    @FXML
    private void handleNuevaPartida() {
        carregarPantallaJoc(null); // passa null si és nova partida
    }

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

    private void carregarPantallaJoc(Partida partida) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/pantallaJuego.fxml"));
            Parent root = loader.load();
            pantallaJuegoController controlador = loader.getController();

            if (partida != null) {
                controlador.carregarPartidaDesDeBD(partida.getId()); // aquest mètode ha d'existir
            }

            Stage stage = (Stage) btnNuevaPartida.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarError("Error", "No s'ha pogut carregar la pantalla del joc.");
        }
    }

    private void mostrarError(String titol, String missatge) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titol);
        alert.setHeaderText(null);
        alert.setContentText(missatge);
        alert.showAndWait();
    }
}
