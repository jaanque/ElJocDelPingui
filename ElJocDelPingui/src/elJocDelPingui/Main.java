package elJocDelPingui;
import elJocDelPingui.Bdades;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        int opcion = 0;
        Connection con = null;
        // CONECTAR BASE DE DADES
        con = Bdades.conectarBaseDatos(con);
        String nombreUsuario = "";
        String contrasena = "";

        // LOGIN O REGISTRE
        while (true) {
            System.out.println("Vols fer login o registrar-te? (login/registro)");
            String eleccio = s.nextLine();

            System.out.print("Nom d'usuari: ");
            nombreUsuario = s.nextLine();

            System.out.print("Contrasenya: ");
            contrasena = s.nextLine();

            if (eleccio.equalsIgnoreCase("registro")) {
                String sqlInsert = "INSERT INTO JUGADORES (NICKNAME, CONTRASENYA) VALUES ('"
                        + nombreUsuario + "', '" + contrasena + "')";
                Bdades.insert(con, sqlInsert);
                System.out.println("Usuari registrat correctament.");
                break;

            } else if (eleccio.equalsIgnoreCase("login")) {
                String sqlLogin = "SELECT * FROM JUGADORES WHERE NICKNAME = '"
                        + nombreUsuario + "' AND CONTRASENYA = '" + contrasena + "'";
                ResultSet rs = Bdades.select(con, sqlLogin);

                try {
                    if (rs != null && rs.next()) {
                        System.out.println("Login correcte. Benvingut/da, " + nombreUsuario + "!");
                        break;
                    } else {
                        System.out.println("Usuari o contrasenya incorrectes. Torna-ho a intentar.");
                    }
                } catch (SQLException e) {
                    System.out.println("Error durant el login: " + e.getMessage());
                }

            } else {
                System.out.println("Opció no vàlida. Escriu 'login' o 'registro'.");
            }
        }

        // Mostrar menú principal
        System.out.println("=========================================");
        System.out.println("      BENVINGUTS AL JOC DEL PINGÜÍ       ");
        System.out.println("=========================================");
        System.out.println("1. JUGAR");
        System.out.println("2. COM JUGAR I REGLES");
        System.out.println("3. SORTIR DEL JOC");
        System.out.println("-----------------------------------------");

        // BUCLE ENTRADES
        while (true) {
            System.out.print("Escull una opció (1-3): ");
            if (s.hasNextInt()) {
                opcion = s.nextInt();
                s.nextLine(); // PASSAR SALT
                if (opcion >= 1 && opcion <= 3) {
                    break;
                } else {
                    System.out.println("Sisplau ingressa un número entre 1 i 3.");
                }
            } else {
                System.out.println("Entrada no vàlida. Sisplau ingressa un número.");
                s.nextLine(); // LLIMPIAR ENTRADA
            }
        }

        switch (opcion) {
            case 1:
                // CREAR PARTIDA
                Partida partida = new Partida();
                partida.iniciarPartida();

                // COMENÇAR PARTIDA COP FINALITZADA ENTRADA DE JUGADORS
                if (!partida.getJugadors().isEmpty()) {
                    iniciarFaseDeJuego(partida, s);
                }
                break;

            case 2:
                mostrarReglas();
                break;

            case 3:
                System.out.println("Sortint del joc. Fins aviat!");
                break;
        }

        s.close();
    }

    private static void mostrarReglas() {
        System.out.println("\n=========================================");
        System.out.println("      REGLES DEL JOC DEL PINGÜÍ         ");
        System.out.println("=========================================");
        System.out.println("Objectiu: Ser el primer pingüí en arribar al final del tauler de 50 caselles.");
        System.out.println("\nTipus de caselles:");
        System.out.println("- Pingüí: Fitxa del jugador");
        System.out.println("- Ós: Si un jugador és atacat, retorna a l'inici del joc");
        System.out.println("- Forat al gel: Envia al jugador al forat d'abans");
        System.out.println("- Trineu: Permet avançar al següent trineu del tauler");
        System.out.println("- Casella d'interrogant: Activa un event aleatori");
        System.out.println("\nInventari:");
        System.out.println("- Màxim 3 daus (normal o especial)");
        System.out.println("- Màxim 2 peixos (per subornar l'ós)");
        System.out.println("- Màxim 6 boles de neu (per fer retrocedir altres jugadors)");
        System.out.println("\nTorn de joc:");
        System.out.println("En el teu torn pots:");
        System.out.println("1. Tirar un dau normal (1-3 caselles)");
        System.out.println("2. Tirar un dau especial (5-10 caselles) si en tens");
        System.out.println("3. Llançar boles de neu a un altre jugador per fer-lo retrocedir");
        System.out.println("\nPrem ENTER per tornar al menú principal...");

        Scanner sc = new Scanner(System.in);
        sc.nextLine();
        main(new String[0]); // TORNAR MENU INICI
    }

    private static void iniciarFaseDeJuego(Partida partida, Scanner s) {
        ArrayList<Pingüino> jugadores = partida.getJugadors();
        int turnoActual = 0;
        boolean juegoTerminado = false;

        Dado dado = new Dado();

        while (!juegoTerminado) {
            Pingüino jugadorActual = jugadores.get(turnoActual);

            System.out.println("\n=========================================");
            System.out.println("Torn de " + jugadorActual.getNom() + " (" + jugadorActual.getColor() + ")");
            System.out.println("Posició actual: " + jugadorActual.getPosicio());
            System.out.println("Inventari: " + jugadorActual.getInventario().toString());
            dado.mostrarDadosEspeciales();
            System.out.println("-----------------------------------------");

            System.out.println("Què vols fer?");
            System.out.println("1. Tirar dau normal");
            if (dado.cantidadDadosEspeciales() > 0) {
                System.out.println("2. Tirar dau especial (" + dado.cantidadDadosEspeciales() + " disponibles)");
            }
            if (jugadorActual.getInventario().getbolesNeu() > 0) {
                System.out.println("3. Llançar bola de neu (" + jugadorActual.getInventario().getbolesNeu() + " disponibles)");
            }

            System.out.print("Escull una opció: ");
            int accion = 0;
            try {
                accion = Integer.parseInt(s.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Entrada invàlida. Intentant de nou...");
                continue;
            }

            int nuevaPosicion = jugadorActual.getPosicio();

            switch (accion) {
                case 1:
                    int resultadoDado = dado.tirarDadoBasico();
                    nuevaPosicion = jugadorActual.avanzar(resultadoDado);
                    System.out.println("Has avançat " + resultadoDado + " caselles. Nova posició: " + nuevaPosicion);
                    break;

                case 2:
                    if (dado.cantidadDadosEspeciales() > 0) {
                        dado.mostrarDadosEspeciales();
                        System.out.print("Escull quin dau especial vols usar (0-" + (dado.cantidadDadosEspeciales() - 1) + "): ");
                        int cualDado = 0;
                        try {
                            cualDado = Integer.parseInt(s.nextLine());
                            int resultadoEspecial = dado.usarDadoEspecial(cualDado);
                            if (resultadoEspecial != -1) {
                                nuevaPosicion = jugadorActual.avanzar(resultadoEspecial);
                                System.out.println("Has avançat " + resultadoEspecial + " caselles amb el dau especial. Nova posició: " + nuevaPosicion);
                            } else {
                                continue;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Entrada invàlida.");
                            continue;
                        }
                    } else {
                        System.out.println("No tens daus especials disponibles!");
                        continue;
                    }
                    break;

                case 3:
                    if (jugadorActual.getInventario().getbolesNeu() > 0) {
                        System.out.println("A quin jugador vols llançar la bola?");
                        for (int i = 0; i < jugadores.size(); i++) {
                            if (i != turnoActual) {
                                System.out.println((i + 1) + ". " + jugadores.get(i).getNom() + " (" + jugadores.get(i).getColor() + ")");
                            }
                        }

                        System.out.print("Escull un jugador: ");
                        int objetivo = 0;
                        try {
                            objetivo = Integer.parseInt(s.nextLine()) - 1;

                            if (objetivo >= 0 && objetivo < jugadores.size() && objetivo != turnoActual) {
                                Pingüino jugadorObjetivo = jugadores.get(objetivo);
                                jugadorObjetivo.retroceder(2);
                                jugadorActual.getInventario().setbolesNeu(jugadorActual.getInventario().getbolesNeu() - 1);

                                System.out.println("Has llançat una bola de neu a " + jugadorObjetivo.getNom() +
                                        "! Retrocedeix 2 caselles a la posició " + jugadorObjetivo.getPosicio());
                            } else {
                                System.out.println("Jugador invàlid. Intentant de nou...");
                                continue;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Entrada invàlida. Intentant de nou...");
                            continue;
                        }
                    } else {
                        System.out.println("No tens boles de neu disponibles!");
                        continue;
                    }
                    break;

                default:
                    System.out.println("Opció invàlida. Intentant de nou...");
                    continue;
            }

            if (nuevaPosicion >= 50) {
                System.out.println("\n¡FELICITATS " + jugadorActual.getNom() + "! Has guanyat la partida!");
                juegoTerminado = true;
            }

            turnoActual = (turnoActual + 1) % jugadores.size();
            System.out.println("\nPrem ENTER per continuar...");
            s.nextLine();
        }

        System.out.println("\nPartida finalitzada. Tornant al menú principal...");
        System.out.println("Prem ENTER per continuar...");
        s.nextLine();
        main(new String[0]);
    }
}
