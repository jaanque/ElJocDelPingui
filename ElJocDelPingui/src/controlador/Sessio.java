package controlador;

import java.sql.Connection;

public class Sessio {

    private static int idJugador;
    private static String nickname;
    public static Connection connexio; // Connexio

    public static void iniciarSessio(int id, String nom) {
        idJugador = id;
        nickname = nom;
    }

    public static int getIdJugador() {
        return idJugador;
    }

    public static String getNickname() {
        return nickname;
    }

    public static void tancarSessio() {
        idJugador = -1;
        nickname = null;
    }
}
