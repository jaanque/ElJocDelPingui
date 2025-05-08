package controlador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class gestorPartidas {

    public static int guardarPartida(Connection con, int idJugador, int posicion, String inventario) {
        try {
            // 1. Insertar en PARTIDAS
            String insertPartida = "INSERT INTO PARTIDAS (FECHA, HORA, ESTADO_TABLERO) VALUES (?, ?, ?)";
            PreparedStatement stmtPartida = con.prepareStatement(insertPartida, new String[] {"ID_PARTIDA"});
            LocalDateTime ahora = LocalDateTime.now();
            stmtPartida.setDate(1, java.sql.Date.valueOf(ahora.toLocalDate()));
            stmtPartida.setTime(2, java.sql.Time.valueOf(ahora.toLocalTime()));
            stmtPartida.setString(3, ""); // Pots serialitzar l'estat del tauler si cal
            stmtPartida.executeUpdate();

            // 2. Obtenir l'ID de la partida
            ResultSet rs = stmtPartida.getGeneratedKeys();
            int idPartida = -1;
            if (rs.next()) {
                idPartida = rs.getInt(1);
            }

            // 3. Insertar en PARTIDAS_JUGADORES
            String insertJugador = "INSERT INTO PARTIDAS_JUGADORES (ID_PARTIDA, ID_JUGADOR, POSICION_ACTUAL, INVENTARIO) VALUES (?, ?, ?, ?)";
            PreparedStatement stmtJugador = con.prepareStatement(insertJugador);
            stmtJugador.setInt(1, idPartida);
            stmtJugador.setInt(2, idJugador);
            stmtJugador.setInt(3, posicion);
            stmtJugador.setString(4, inventario);
            stmtJugador.executeUpdate();

            return idPartida;

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static ResultSet cargarUltimaPartida(Connection con, int idJugador) {
        try {
            String sql = "SELECT pj.*, p.FECHA, p.HORA FROM PARTIDAS_JUGADORES pj " +
                         "JOIN PARTIDAS p ON pj.ID_PARTIDA = p.ID_PARTIDA " +
                         "WHERE pj.ID_JUGADOR = ? ORDER BY p.FECHA DESC, p.HORA DESC FETCH FIRST 1 ROWS ONLY";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, idJugador);
            return stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
