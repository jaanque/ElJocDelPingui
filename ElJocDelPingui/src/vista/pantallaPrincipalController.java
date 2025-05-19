// pantallaPrincipalController.java
// Controlador de la pantalla d'inici de sessió i registre d'usuaris

package vista;

// Imports per a interfície gràfica i base de dades
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

public class pantallaPrincipalController {

    // ------------------------------
    // Elements de la interfície (FXML)
    // ------------------------------
    @FXML private TextField userField;        // Camp per al nom d'usuari
    @FXML private PasswordField passField;    // Camp per a la contrasenya
    @FXML private Button loginButton;         // Botó d'inici de sessió

    private Connection con;                   // Connexió a la base de dades

    // ------------------------------
    // Inicialització del controlador
    // ------------------------------
    @FXML
    public void initialize() {
        System.out.println("pantallaPrincipalController initialized");
        con = controlador.Sessio.connexio; // Reutilitza la connexió creada al main
    }

    // ------------------------------
    // Iniciar sessió amb credencials
    // ------------------------------
    @FXML
    private void handleLogin() {
        if (con == null) {
            mostrarError("Error de conexión", "No se pudo conectar a la base de datos.");
            return;
        }

        String usuario = userField.getText();
        String contrasena = passField.getText();

        // Consulta SQL per comprovar credencials
        String sqlLogin = "SELECT * FROM JUGADORES WHERE NICKNAME = '" + usuario + "' AND CONTRASENYA = '" + contrasena + "'";
        ResultSet rs = Bdades.select(con, sqlLogin);

        try {
            if (rs != null && rs.next()) {
                // Usuari i contrasenya correctes
                System.out.println("Login correcto. Bienvenido, " + usuario);

                int idJugador = rs.getInt("ID_JUGADOR");
                String nickname = rs.getString("NICKNAME");

                // Desa les dades de sessió
                controlador.Sessio.iniciarSessio(idJugador, nickname);

                // Carrega la següent pantalla (pantalla de selecció de partida)
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/pantallaSeleccion.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) loginButton.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } else {
                // Credencials incorrectes
                mostrarError("Error de login", "Usuario o contraseña incorrectos.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            mostrarError("Error inesperado", "Ha ocurrido un error al intentar iniciar sesión.");
        }
    }

    // ------------------------------
    // Registrar un nou usuari
    // ------------------------------
    @FXML
    private void handleRegister() {
        if (con == null) {
            mostrarError("Error de conexión", "No se pudo conectar a la base de datos.");
            return;
        }

        String usuario = userField.getText();
        String contrasena = passField.getText();

        // Inserció del nou jugador a la base de dades
        String sqlInsert = "INSERT INTO JUGADORES (NICKNAME, CONTRASENYA) VALUES ('" + usuario + "', '" + contrasena + "')";
        try {
            int filasAfectadas = Bdades.insert(con, sqlInsert);
            if (filasAfectadas > 0) {
                // Registre correcte
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

    // ------------------------------
    // Mostrar missatges d'error
    // ------------------------------
    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}

