package elJocDelPingui;
import java.util.ArrayList;
import java.util.Scanner;

public class Jugador {

    public static ArrayList<String> demanarJugadors(Scanner s) {
        int numjugadors = 0;
        ArrayList<String> nomjugadors = new ArrayList<>();

        // BUCLE PREGUNTAR NUMERO DE JUGADORS
        while (numjugadors < 1 || numjugadors > 4) {
            System.out.println("Cuants jugadors participaran en la partida? (1-4)");
            if (s.hasNextInt()) {
                numjugadors = s.nextInt();
                s.nextLine(); // LLIMPIAR BUFFER PER EVITAR ERROR 
                if (numjugadors < 1 || numjugadors > 4) {
                    System.out.println("Numero invalid, entre 1 i 4 ");
                }
            } else {
                System.out.println("Entri numero valid");
                s.next(); // LLIMPIAR ENTRADA 
            }
        }

        // BUCLE ENTRADA NOM DE JUGADORS
        for (int i = 1; i <= numjugadors; i++) {
            System.out.println("Entri el nom del jugador " + i + " : ");
            String nom = s.nextLine();
            nomjugadors.add(nom);
        }

        return nomjugadors;
    }
}