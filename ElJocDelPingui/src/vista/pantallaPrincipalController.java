package vista;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.ResultSet;

import controlador.Bdades;

// ==============================
// CONTROLADOR DE LA PANTALLA PRINCIPAL (LOGIN / REGISTRE)
// ==============================
public class pantallaPrincipalController {

    // ==============================
    // ELEMENTS DE LA INTERFÍCIE
    // ==============================
    @FXML private TextField userField;
    @FXML private PasswordField passField;
    @FXML private Button loginButton;

    // ==============================
    // CONNEXIÓ A LA BASE DE DADES
    // ==============================
    private Connection con;

    // ==============================
    // INICIALITZACIÓ DEL CONTROLADOR
    // ==============================
    @FXML
    public void initialize() {
        System.out.println("pantallaPrincipalController initialized");
        con = Bdades.conectarBaseDatos(con);
    }

    // ==============================
    // ACCIÓ: INICIAR SESSIÓ
    // ==============================
    @FXML
    private void handleLogin() {
        if (con == null) {
            mostrarError("Error de conexión", "No se pudo conectar a la base de datos.");
            return;
        }

        String usuario = userField.getText();
        String contrasena = passField.getText();

        String sqlLogin = "SELECT * FROM JUGADORES WHERE NICKNAME = '" + usuario + "' AND CONTRASENYA = '" + contrasena + "'";
        ResultSet rs = Bdades.select(con, sqlLogin);

        try {
            if (rs != null && rs.next()) {
                System.out.println("Login correcto. Bienvenido, " + usuario);

                // CANVIAR A LA PANTALLA DE JOC
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/pantallaJuego.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) loginButton.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } else {
                mostrarError("Error de login", "Usuario o contraseña incorrectos.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            mostrarError("Error inesperado", "Ha ocurrido un error al intentar iniciar sesión.");
        }
    }

    // ==============================
    // ACCIÓ: REGISTRAR NOU USUARI
    // ==============================
    @FXML
    private void handleRegister() {
        if (con == null) {
            mostrarError("Error de conexión", "No se pudo conectar a la base de datos.");
            return;
        }

        String usuario = userField.getText();
        String contrasena = passField.getText();

        String sqlInsert = "INSERT INTO JUGADORES (NICKNAME, CONTRASENYA) VALUES ('" + usuario + "', '" + contrasena + "')";
        try {
            int filasAfectadas = Bdades.insert(con, sqlInsert);
            if (filasAfectadas > 0) {
                System.out.println("Registro correcto. Bienvenido, " + usuario);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Registro correcto");
                alert.setHeaderText(null);
                alert.setContentText("Usuario registrado con éxito. Puedes iniciar sesión.");
                alert.showAndWait();
            } else {
                mostrarError("Error en el registro", "No se pudo registrar el usuario.");
            }
        } catch (Exception e) {
            mostrarError("Error de base de datos", "Este usuario ya existe o ocurrió un error.");
            e.printStackTrace();
        }
    }

    // ==============================
    // MOSTRAR ERROR AMB ALERTA GRÀFICA
    // ==============================
    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
