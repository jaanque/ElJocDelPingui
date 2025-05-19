package controlador;

import vista.pantallaJuegoController;

import java.sql.*;
import java.time.LocalDateTime;

public class GestorPartides {

    // ✅ Guarda una nova partida a la base de dades
    public static int guardarPartida(Connection con, int idJugador, int posicion, String inventario) {
        try {
            String insertPartida = "INSERT INTO PARTIDAS (FECHA, HORA, ESTADO_TABLERO) VALUES (?, ?, ?)";
            PreparedStatement stmtPartida = con.prepareStatement(insertPartida, new String[]{"ID_PARTIDA"});

            LocalDateTime ahora = LocalDateTime.now();
            stmtPartida.setDate(1, java.sql.Date.valueOf(ahora.toLocalDate()));
            stmtPartida.setTime(2, java.sql.Time.valueOf(ahora.toLocalTime()));
            stmtPartida.setString(3, ""); // ESTADO_TABLERO es pot afegir després si serialitzes el mapa

            stmtPartida.executeUpdate();

            ResultSet rs = stmtPartida.getGeneratedKeys();
            int idPartida = -1;
            if (rs.next()) {
                idPartida = rs.getInt(1);
            }

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

    // ✅ Obté la partida més recent guardada per un jugador
    public static ResultSet cargarUltimaPartida(Connection con, int idJugador) {
        try {
            String sql = "SELECT pj.*, p.FECHA, p.HORA FROM PARTIDAS_JUGADORES pj " +
                         "JOIN PARTIDAS p ON pj.ID_PARTIDA = p.ID_PARTIDA " +
                         "WHERE pj.ID_JUGADOR = ? " +
                         "ORDER BY p.FECHA DESC, p.HORA DESC FETCH FIRST 1 ROWS ONLY";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, idJugador);
            return stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ✅ Obté totes les partides guardades d’un jugador (per ComboBox)
    public static ResultSet obtenirPartidesJugador(Connection con, int idJugador) {
        try {
            String sql = "SELECT pj.ID_PARTIDA, p.FECHA, p.HORA FROM PARTIDAS_JUGADORES pj " +
                         "JOIN PARTIDAS p ON pj.ID_PARTIDA = p.ID_PARTIDA " +
                         "WHERE pj.ID_JUGADOR = ? " +
                         "ORDER BY p.FECHA DESC, p.HORA DESC";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, idJugador);
            return stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ✅ Carrega una partida específica pel seu ID
    public static ResultSet carregarPerId(Connection con, int idJugador, int idPartida) {
        try {
            String sql = "SELECT * FROM PARTIDAS_JUGADORES WHERE ID_JUGADOR = ? AND ID_PARTIDA = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, idJugador);
            stmt.setInt(2, idPartida);
            return stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void guardar(Connection con, pantallaJuegoController controlador, int idJugador, LocalDateTime timestamp) {
        int posicio = controlador.getP1Position();
        String inventari = "R:" + controlador.getDadosRapidos() +
                           ",L:" + controlador.getDadosLentos() +
                           ",P:" + controlador.getPeces() +
                           ",N:" + controlador.getBolasNieve();

        int idPartida = controlador.getIdPartidaActual();

        if (idPartida == -1) {
            // 🔸 No hi ha partida encara, fem INSERT a PARTIDAS i PARTIDAS_JUGADORES
            try {
                String insertPartida = "INSERT INTO PARTIDAS (FECHA, HORA, ESTADO_TABLERO) VALUES (?, ?, ?)";
                PreparedStatement stmtPartida = con.prepareStatement(insertPartida, new String[]{"ID_PARTIDA"});

                stmtPartida.setDate(1, java.sql.Date.valueOf(timestamp.toLocalDate()));
                stmtPartida.setTime(2, java.sql.Time.valueOf(timestamp.toLocalTime()));
                stmtPartida.setString(3, ""); // ESTADO_TABLERO si vols afegir-ho més endavant

                stmtPartida.executeUpdate();
                ResultSet rs = stmtPartida.getGeneratedKeys();

                if (rs.next()) {
                    idPartida = rs.getInt(1);
                    controlador.setIdPartidaActual(idPartida); // ✅ Guardem l’ID a la sessió
                }

                String insertJugador = "INSERT INTO PARTIDAS_JUGADORES (ID_PARTIDA, ID_JUGADOR, POSICION_ACTUAL, INVENTARIO) VALUES (?, ?, ?, ?)";
                PreparedStatement stmtJugador = con.prepareStatement(insertJugador);
                stmtJugador.setInt(1, idPartida);
                stmtJugador.setInt(2, idJugador);
                stmtJugador.setInt(3, posicio);
                stmtJugador.setString(4, inventari);
                stmtJugador.executeUpdate();

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            // 🔹 La partida ja existeix, fem UPDATE
            try {
                String update = "UPDATE PARTIDAS_JUGADORES SET POSICION_ACTUAL = ?, INVENTARIO = ? " +
                                "WHERE ID_PARTIDA = ? AND ID_JUGADOR = ?";
                PreparedStatement stmt = con.prepareStatement(update);
                stmt.setInt(1, posicio);
                stmt.setString(2, inventari);
                stmt.setInt(3, idPartida);
                stmt.setInt(4, idJugador);
                stmt.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // ✅ Mètode complet per carregar la partida al controlador
    public static void carregar(Connection con, pantallaJuegoController controlador, int idJugador, int idPartida) {
        try {
            ResultSet rs = carregarPerId(con, idJugador, idPartida);
            if (rs != null && rs.next()) {
                controlador.setP1Position(rs.getInt("POSICION_ACTUAL"));

                String inventari = rs.getString("INVENTARIO");
                if (inventari != null) {
                    for (String part : inventari.split(",")) {
                        String[] parell = part.split(":");
                        switch (parell[0]) {
                            case "R": controlador.setDadosRapidos(Integer.parseInt(parell[1])); break;
                            case "L": controlador.setDadosLentos(Integer.parseInt(parell[1])); break;
                            case "P": controlador.setPeces(Integer.parseInt(parell[1])); break;
                        }
                    }
                }

                controlador.actualitzarPosicioJugador();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
