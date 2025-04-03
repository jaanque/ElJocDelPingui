package elJocDelPingui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Partida {
    private static ArrayList<Pingüino> jugadors;
    private static final ArrayList<String> COLORS_DISPONIBLES = 
        new ArrayList<>(Arrays.asList("blau", "verd", "groc", "vermell"));

    public Partida() {
        this.jugadors = new ArrayList<>();
    }

    public static void iniciarPartida() {
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
                    colorsDisponibles.remove(color); // Esborra el color per evitar duplicats
                    break;
                } else {
                    System.out.println("Color no disponible. Tria un dels colors indicats.");
                }
            }

            jugadors.add(new Pingüino(nom, color));
        }

        System.out.println("Jugadors registrats correctament!");
    }

    public ArrayList<Pingüino> getJugadors() {
        return jugadors;
    }
}

