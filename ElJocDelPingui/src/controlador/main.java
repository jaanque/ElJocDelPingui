package controlador;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;
import java.sql.Connection;

// ==============================
// CLASSE PRINCIPAL D'INICI AMB JAVAFX
// ==============================
public class main extends Application {
	
    // ==============================
    // MÈTODE START → INICIA LA INTERFÍCIE GRÀFICA
    // ==============================
	@Override
	public void start(Stage primaryStage) throws Exception {
		Connection con = null;
        con = Bdades.conectarBaseDatos(con); // CONNEXIÓ A LA BASE DE DADES

        // CARREGAR FXML DE LA PANTALLA PRINCIPAL
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/pantallaPrincipal.fxml"));
	    Parent root = loader.load();

        // CREAR I MOSTRAR L'ESCENA
	    Scene scene = new Scene(root);
	    primaryStage.setScene(scene);
	    primaryStage.setTitle("El Juego del Pingüino");
	    primaryStage.show();
	}

    // ==============================
    // MAIN → LLANÇA L'APLICACIÓ
    // ==============================
    public static void main(String[] args) {
        launch(args);
    }
}
