package elJocDelPingui;
import java.util.Scanner;
import java.util.ArrayList;

/**
 * Classe principal que controla el flux del joc
 */
public class Main {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        int opcion = 0;
        
        // Mostrar menú principal
        System.out.println("=========================================");
        System.out.println("      BENVINGUTS AL JOC DEL PINGÜÍ       ");
        System.out.println("=========================================");
        System.out.println("1. JUGAR");
        System.out.println("2. COM JUGAR I REGLES");
        System.out.println("3. SORTIR DEL JOC");
        System.out.println("-----------------------------------------");
        
        //BUCLE ENTRADES
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
                s.nextLine(); //LLIMPIAR ENTRADA 
            }
        }
        
        switch (opcion) {
            case 1:
                //CREAR PARTIDA 
                Partida partida = new Partida();
                partida.iniciarPartida();
                
                //CREAR TAULER
                Tauler tauler = new Tauler();
                
                //COMENÇAR PARTIDA COP FINALITZADA ENTRADA DE JUGADORS
                if (!partida.getJugadors().isEmpty()) {
                    iniciarFaseDeJuego(partida, tauler, s);
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
    
    /**
     * Mostra les regles del joc a l'usuari
     */
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
        main(new String[0]);  //TORNAR MENU INICI 
    }
    
    /**
     * Inicia la fase de joc principal
     * @param partida La partida actual
     * @param tauler El tauler de joc
     * @param s L'scanner per entrada de l'usuari
     */
    private static void iniciarFaseDeJuego(Partida partida, Tauler tauler, Scanner s) {
        ArrayList<Pingüino> jugadores = partida.getJugadors();
        int turnoActual = 0;
        boolean juegoTerminado = false;
        
        // Mostrar el tauler inicial
        tauler.mostrarTauler();
        
        // Crear dados
        Dado dadoNormal = new Dado();
        DadoEspecial dadoEspecial = new DadoEspecial();
        
        // Bucle principal del juego
        while (!juegoTerminado) {
            Pingüino jugadorActual = jugadores.get(turnoActual);
            
            System.out.println("\n=========================================");
            System.out.println("Torn de " + jugadorActual.getNom() + " (" + jugadorActual.getColor() + ")");
            System.out.println("Posició actual: " + jugadorActual.getPosicio());
            System.out.println(jugadorActual.getInventario().toString());
            System.out.println("-----------------------------------------");
            System.out.println("Què vols fer?");
            System.out.println("1. Tirar dau normal");
            
            //VENTALL OPCIONS SEGONS INVENTARIO 
            if (jugadorActual.getInventario().getdaus() > 0) {
                System.out.println("2. Tirar dau especial (" + jugadorActual.getInventario().getdaus() + " disponibles)");
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
            
            //PROCESSAR ACCIO 
            String resultatMoviment = "";
            int posicioOriginal = jugadorActual.getPosicio();
            
            switch (accion) {
                case 1:  //TIRAR DAU NORMAL
                    int resultadoDado = dadoNormal.tirarDado();
                    System.out.println("Has tret un " + resultadoDado + " al dau.");
                    
                    // Processar el moviment al tauler i aplicar efectes de caselles
                    resultatMoviment = tauler.processarMoviment(jugadorActual, resultadoDado);
                    System.out.println(resultatMoviment);
                    break;
                    
                case 2:  //TIRAR DAU ESPECIAL SI EN TE 
                    if (jugadorActual.getInventario().getdaus() > 0) {
                        int resultadoEspecial = dadoEspecial.tirarDado();
                        System.out.println("Has tret un " + resultadoEspecial + " al dau especial.");
                        
                        // Processar el moviment al tauler i aplicar efectes de caselles
                        resultatMoviment = tauler.processarMoviment(jugadorActual, resultadoEspecial);
                        System.out.println(resultatMoviment);
                        
                        // Restar un dado del inventario
                        jugadorActual.getInventario().setdaus(jugadorActual.getInventario().getdaus() - 1);
                    } else {
                        System.out.println("No tens daus especials disponibles!");
                        continue; //ESCOLLIR NOVA OPCIÓ 
                    }
                    break;
                    
                case 3:  //TIRAR BOLA DE NEU 
                    if (jugadorActual.getInventario().getbolesNeu() > 0) {
                        //LLISTA JUGADORS ALS QUE PODEM ATACAR 
                        System.out.println("A quin jugador vols llançar la bola?");
                        for (int i = 0; i < jugadores.size(); i++) {
                            if (i != turnoActual) { 
                                System.out.println((i+1) + ". " + jugadores.get(i).getNom() + " (" + jugadores.get(i).getColor() + ")");
                            }
                        }
                        
                        System.out.print("Escull un jugador: ");
                        int objetivo = 0;
                        try {
                            objetivo = Integer.parseInt(s.nextLine()) - 1;
                            
                            if (objetivo >= 0 && objetivo < jugadores.size() && objetivo != turnoActual) {
                                //TIRAR ENRERE JUGADOR 
                                Pingüino jugadorObjetivo = jugadores.get(objetivo);
                                jugadorObjetivo.retroceder(2); //RETROCEDIR 2 CASELLES
                                
                                //RESTAR DEL INVENTARI LA BOLA DE NEU 
                                jugadorActual.getInventario().setbolesNeu(jugadorActual.getInventario().getbolesNeu() - 1);
                                
                                System.out.println("Has llançat una bola de neu a " + jugadorObjetivo.getNom() + 
                                                   "! Retrocedeix 2 caselles a la posició " + jugadorObjetivo.getPosicio());
                            } else {
                                System.out.println("Jugador invàlid. Intentant de nou...");
                                continue;   //AL CONTINUE DEMANE NOVA ACCIÓ 
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
            
            // Mostrem el tauler després de cada torn per veure els canvis
            tauler.mostrarTauler();
            
            // Comprovem si el jugador ha arribat a la meta
            if (jugadorActual.getPosicio() >= 50) {
                System.out.println("\nFELICITATS " + jugadorActual.getNom() + "! Has guanyat la partida!");
                juegoTerminado = true;
            }
            
            // Comprovar lluites entre jugadors
            if (!juegoTerminado) {
                ArrayList<Pingüino> jugadorsMateixaCasella = tauler.jugadorsEnCasella(jugadores, jugadorActual.getPosicio());
                if (jugadorsMateixaCasella.size() > 1) {
                    System.out.println("\nHi ha diversos jugadors a la mateixa casella! Comença una lluita de boles de neu!");
                    // La lògica de lluita s'implementaria aquí en versions avançades
                }
            }
            
            //PASSAR DE TORN 
            turnoActual = (turnoActual + 1) % jugadores.size();
            
            System.out.println("\nPrem ENTER per continuar...");
            s.nextLine();
        }
        
        //ACABAR PARTIDA TORNAR MAIN MENU 
        System.out.println("\nPartida finalitzada. Tornant al menú principal...");
        System.out.println("Prem ENTER per continuar...");
        s.nextLine();
        main(new String[0]);
    }
}