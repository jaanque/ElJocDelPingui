package controlador;

import vista.pantallaJuegoController;

import java.sql.*;
import java.time.LocalDateTime;

public class GestorPartides {

    // ✅ Guarda una nova partida a la base de dades
    public static int guardarPartida(Connection con, int idJugador, int posicion, String inventario, String estatTauler) {
        try {
            String insertPartida = "INSERT INTO PARTIDAS (FECHA, HORA, ESTADO_TABLERO) VALUES (?, ?, ?)";
            PreparedStatement stmtPartida = con.prepareStatement(insertPartida, new String[]{"ID_PARTIDA"});
            con.commit();
            LocalDateTime ahora = LocalDateTime.now();
            stmtPartida.setDate(1, java.sql.Date.valueOf(ahora.toLocalDate()));
            stmtPartida.setTime(2, java.sql.Time.valueOf(ahora.toLocalTime()));
            stmtPartida.setString(3, estatTauler);

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

        String estatTauler = controlador.serialitzarMapaEventos();
        int idPartida = controlador.getIdPartidaActual();

        if (idPartida == -1) {
            try {
                String insertPartida = "INSERT INTO PARTIDAS (FECHA, HORA, ESTADO_TABLERO) VALUES (?, ?, ?)";
                PreparedStatement stmtPartida = con.prepareStatement(insertPartida, new String[]{"ID_PARTIDA"});

                stmtPartida.setDate(1, java.sql.Date.valueOf(timestamp.toLocalDate()));
                stmtPartida.setTime(2, java.sql.Time.valueOf(timestamp.toLocalTime()));
                stmtPartida.setString(3, estatTauler);

                stmtPartida.executeUpdate();
                ResultSet rs = stmtPartida.getGeneratedKeys();

                if (rs.next()) {
                    idPartida = rs.getInt(1);
                    controlador.setIdPartidaActual(idPartida);
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
            try {
                String updateJugador = "UPDATE PARTIDAS_JUGADORES SET POSICION_ACTUAL = ?, INVENTARIO = ? " +
                                       "WHERE ID_PARTIDA = ? AND ID_JUGADOR = ?";
                PreparedStatement stmtJugador = con.prepareStatement(updateJugador);
                stmtJugador.setInt(1, posicio);
                stmtJugador.setString(2, inventari);
                stmtJugador.setInt(3, idPartida);
                stmtJugador.setInt(4, idJugador);
                stmtJugador.executeUpdate();

                String updatePartida = "UPDATE PARTIDAS SET ESTADO_TABLERO = ? WHERE ID_PARTIDA = ?";
                PreparedStatement stmtPartida = con.prepareStatement(updatePartida);
                stmtPartida.setString(1, estatTauler);
                stmtPartida.setInt(2, idPartida);
                stmtPartida.executeUpdate();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void carregar(Connection con, pantallaJuegoController controlador, int idJugador, int idPartida) {
        try {
            String sql = "SELECT pj.*, p.ESTADO_TABLERO FROM PARTIDAS_JUGADORES pj " +
                         "JOIN PARTIDAS p ON pj.ID_PARTIDA = p.ID_PARTIDA " +
                         "WHERE pj.ID_JUGADOR = ? AND pj.ID_PARTIDA = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, idJugador);
            stmt.setInt(2, idPartida);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                controlador.setP1Position(rs.getInt("POSICION_ACTUAL"));

                // Inventari
                String inventari = rs.getString("INVENTARIO");
                if (inventari != null) {
                    for (String part : inventari.split(",")) {
                        String[] parell = part.split(":");
                        switch (parell[0]) {
                            case "R": controlador.setDadosRapidos(Integer.parseInt(parell[1])); break;
                            case "L": controlador.setDadosLentos(Integer.parseInt(parell[1])); break;
                            case "P": controlador.setPeces(Integer.parseInt(parell[1])); break;
                            case "N": controlador.setBolasNieve(Integer.parseInt(parell[1])); break;
                        }
                    }
                }

                // 🧩 Recarregar esdeveniments
                String estatTauler = rs.getString("ESTADO_TABLERO");
                if (estatTauler != null && !estatTauler.isEmpty()) {
                    controlador.deserialitzarMapaEventos(estatTauler);
                }

                controlador.actualitzarPosicioJugador();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
