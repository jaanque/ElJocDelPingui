package vista;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class pantallaSeleccionController {

    @FXML private Button btnNuevaPartida;
    @FXML private Button btnCargarPartida;

    @FXML
    private void handleNuevaPartida() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/pantallaJuego.fxml"));
            Parent root = loader.load();

            // Si cal fer setup previ, accedeix al controlador
            pantallaJuegoController controlador = loader.getController();
            // controlador.setupNovaPartida(); // si tens algun setup especial

            Stage stage = (Stage) btnNuevaPartida.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCargarPartida() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/pantallaJuego.fxml"));
            Parent root = loader.load();

            pantallaJuegoController controlador = loader.getController();
            controlador.carregarPartida(); // Mètode per carregar estat

            Stage stage = (Stage) btnCargarPartida.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
