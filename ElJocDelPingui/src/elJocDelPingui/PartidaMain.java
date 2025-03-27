package elJocDelPingui;
import java.util.Scanner;
import java.util.ArrayList;

public class PartidaMain {
    
    public static void main(String[] args) {
        
        Scanner s = new Scanner(System.in);

        
        // LLAMAR A EntradaJugadors PARA OBTENER LA LISTA DE NOMBRES
        ArrayList<String> nomjugadors = Jugador.demanarJugadors(s);

        
        // MOSTRAR NOMBRES DE LOS JUGADORES
        System.out.println("NOM DELS JUGADORS:");
        for (String jugador : nomjugadors) {
            System.out.println("- " + jugador);
        }
    }
}