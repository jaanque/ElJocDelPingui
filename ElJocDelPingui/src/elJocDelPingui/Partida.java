package elJocDelPingui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Partida {
    private ArrayList<Pingüino> jugadors;
    private static final ArrayList<String> COLORS_DISPONIBLES = 
        new ArrayList<>(Arrays.asList("blau", "verd", "groc", "vermell"));

    public Partida() {
        this.jugadors = new ArrayList<>();
    }

    public void iniciarPartida() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Quants jugadors volen jugar? ");
        
        int numJugadors;
        while (true) {
            try {
                numJugadors = Integer.parseInt(scanner.nextLine());
                if (numJugadors > 0 && numJugadors <= COLORS_DISPONIBLES.size()) break;
                else System.out.print("Si us plau, introdueix un número vàlid de jugadors (1-4): ");
            } catch (NumberFormatException e) {
                System.out.print("Si us plau, introdueix un número vàlid de jugadors: ");
            }
        }

        ArrayList<String> colorsDisponibles = new ArrayList<>(COLORS_DISPONIBLES);

        for (int i = 0; i < numJugadors; i++) {
            System.out.print("Nom del jugador " + (i + 1) + ": ");
            String nom = scanner.nextLine();

            String color;
            while (true) {
                System.out.println("Escull un color disponible: " + colorsDisponibles);
                System.out.print("Color del jugador " + (i + 1) + ": ");
                color = scanner.nextLine().toLowerCase();

                if (colorsDisponibles.contains(color)) {
                    colorsDisponibles.remove(color); // BORRAR EL COLOR PER EVITAR QUE ES PODIN REPETIR A L'HORA DE ESCOLLIR-LOS 
                    break;
                } else {
                    System.out.println("Color no disponible. Tria un dels colors indicats.");
                }
            }

          //CREAR JUGADOR I INICIAR EL SEU INVENTARI 
            Pingüino jugador = new Pingüino(nom, color);
            jugador.setInventario(new Inventario(0, 0, 0)); //SENSE OBJECTES INICIA 
            
            jugadors.add(jugador);
        }

        System.out.println("\nJugadors registrats correctament!");
        mostrarJugadores();
    }

    public void mostrarJugadores() {
        System.out.println("\nJugadors de la partida:");
        System.out.println("-----------------------------------------");
        for (Pingüino jugador : jugadors) {
            System.out.println(jugador.getNom() + " (" + jugador.getColor() + ")");
        }
        System.out.println("-----------------------------------------");
    }

    public ArrayList<Pingüino> getJugadors() {
        return jugadors;
    }

    public Pingüino getJugadorByNom(String nom) {
        for (Pingüino jugador : jugadors) {
            if (jugador.getNom().equalsIgnoreCase(nom)) {
                return jugador;
            }
        }
        return null; //NO JUGADOR = NULL 
    }
    public void finalizarPartida() {
        System.out.println("\nLa partida ha finalitzat!");
        System.out.println("Resultats finals:");
        for (Pingüino jugador : jugadors) {
            System.out.println(jugador.getNom() + " (" + jugador.getColor() + ") - Inventario: " + jugador.getInventario());
        }
    }
}
