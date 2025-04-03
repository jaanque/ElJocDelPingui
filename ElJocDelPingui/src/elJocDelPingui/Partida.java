package elJocDelPingui;

import java.util.ArrayList;
import java.util.Scanner;

public class Partida {
    private static ArrayList<Pingüino> jugadors;

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
                if (numJugadors > 0) break;
                else System.out.print("Si us plau, introdueix un número vàlid de jugadors: ");
            } catch (NumberFormatException e) {
                System.out.print("Si us plau, introdueix un número vàlid de jugadors: ");
            }
        }

        for (int i = 0; i < numJugadors; i++) {
            System.out.print("Nom del jugador " + (i + 1) + ": ");
            String nom = scanner.nextLine();
            System.out.print("Color del jugador " + (i + 1) + ": ");
            String color = scanner.nextLine();
            
            jugadors.add(new Pingüino(nom, color));
        }

        System.out.println("Jugadors registrats correctament!");
        scanner.close();
    }

    public ArrayList<Pingüino> getJugadors() {
        return jugadors;
    }
}
