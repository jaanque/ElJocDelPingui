package elJocDelPingui;

import java.util.ArrayList;
import java.util.Random;

public class Tauler {
    private ArrayList<Casilla> caselles; // Llista de totes les caselles
    private ArrayList<Oso> posicionsOsos; // Llista de posicions dels osos
    private ArrayList<AgujeroHielo> posicionsForats; // Llista de posicions dels forats
    private ArrayList<Trineo> posicionsTrineus; // Llista de posicions dels trineus
    private ArrayList<CasillaInterrogante> posicionsInterrogants; // Llista de posicions dels interrogants
    private Random random;
    
    public Tauler() {
        this.caselles = new ArrayList<>();
        this.posicionsOsos = new ArrayList<>();
        this.posicionsForats = new ArrayList<>();
        this.posicionsTrineus = new ArrayList<>();
        this.posicionsInterrogants = new ArrayList<>();
        this.random = new Random();
        
        // Inicialitzem les 50 caselles bàsiques
        for (int i = 0; i < 50; i++) {
            caselles.add(new Casilla(i));
        }
        
        // Generem els elements especials
        generarElementsEspecials();
    }
    
    private void generarElementsEspecials() {
        // Generem 3 osos en posicions aleatòries (evitant la primera i última casella)
        for (int i = 0; i < 3; i++) {
            int posicio = random.nextInt(48) + 1; // Entre 1 i 48
            Oso oso = new Oso(posicio);
            posicionsOsos.add(oso);
        }
        
        // Generem 5 forats en posicions aleatòries (evitant la primera casella)
        for (int i = 0; i < 5; i++) {
            int posicio = random.nextInt(48) + 1; // Entre 1 i 48
            AgujeroHielo forat = new AgujeroHielo(posicio);
            posicionsForats.add(forat);
        }
        
        // Generem 4 trineus en posicions aleatòries (evitant la primera i última casella)
        for (int i = 0; i < 4; i++) {
            int posicio = random.nextInt(48) + 1; // Entre 1 i 48
            Trineo trineu = new Trineo(posicio);
            posicionsTrineus.add(trineu);
        }
        
        // Generem 8 interrogants en posicions aleatòries
        for (int i = 0; i < 8; i++) {
            int posicio = random.nextInt(48) + 1; // Entre 1 i 48
            CasillaInterrogante interrogant = new CasillaInterrogante(posicio);
            posicionsInterrogants.add(interrogant);
        }
    }
    
    public Oso hiHaOs(int posicio) {
        for (Oso os : posicionsOsos) {
            if (os.getPosicion() == posicio) {
                return os;
            }
        }
        return null;
    }
    
    public AgujeroHielo hiHaForat(int posicio) {
        for (AgujeroHielo forat : posicionsForats) {
            if (forat.getposicio() == posicio) {
                return forat;
            }
        }
        return null;
    }
    
    public Trineo hiHaTrineu(int posicio) {
        for (Trineo trineu : posicionsTrineus) {
            if (trineu.getposiciotrineo() == posicio) {
                return trineu;
            }
        }
        return null;
    }
    
    public CasillaInterrogante hiHaInterrogant(int posicio) {
        for (CasillaInterrogante interrogant : posicionsInterrogants) {
            if (interrogant.posicio == posicio) {
                return interrogant;
            }
        }
        return null;
    }
    
    public int trobarSeguentTrineu(int posicioActual) {
        int propera = 50; // Valor per defecte (final del tauler)
        for (Trineo trineu : posicionsTrineus) {
            if (trineu.getposiciotrineo() > posicioActual && trineu.getposiciotrineo() < propera) {
                propera = trineu.getposiciotrineo();
            }
        }
        return propera == 50 ? -1 : propera;
    }
    
    public int trobarForatAnterior(int posicioActual) {
        int anterior = 0; // Valor per defecte (inici del tauler)
        for (AgujeroHielo forat : posicionsForats) {
            if (forat.getposicio() < posicioActual && forat.getposicio() > anterior) {
                anterior = forat.getposicio();
            }
        }
        return anterior;
    }
    
    public String processarMoviment(Pingüino jugador, int caselles) {
        int posicioOriginal = jugador.getPosicio();
        int novaPosicio = jugador.avanzar(caselles);
        StringBuilder resultat = new StringBuilder("Has avançat " + caselles + " caselles. ");
        
        // Comprovem si s'ha arribat o superat la meta
        if (novaPosicio >= 50) {
            return "Has arribat a la meta! Has guanyat la partida!";
        }
        
        // Comprovem si hi ha un os
        Oso os = hiHaOs(novaPosicio);
        if (os != null) {
            boolean retrocedeix = os.encontrarOso(jugador);
            if (retrocedeix) {
                resultat.append("Has trobat un os i no tenies peixos! Tornes a l'inici del tauler.");
            } else {
                resultat.append("Has trobat un os, però l'has subornat amb un peix!");
            }
            return resultat.toString();
        }
        
        // Comprovem si hi ha un forat
        AgujeroHielo forat = hiHaForat(novaPosicio);
        if (forat != null) {
            int posicioForatAnterior = trobarForatAnterior(novaPosicio);
            jugador.setPosicio(posicioForatAnterior);
            resultat.append("Has caigut en un forat! Retrocedeixes fins al forat anterior (posició " + posicioForatAnterior + ").");
            return resultat.toString();
        }
        
        // Comprovem si hi ha un trineu
        Trineo trineu = hiHaTrineu(novaPosicio);
        if (trineu != null) {
            int posicioSeguentTrineu = trobarSeguentTrineu(novaPosicio);
            if (posicioSeguentTrineu != -1) {
                jugador.setPosicio(posicioSeguentTrineu);
                resultat.append("Has trobat un trineu! Avances fins al següent trineu (posició " + posicioSeguentTrineu + ").");
            } else {
                resultat.append("Has trobat un trineu, però no hi ha més trineus endavant.");
            }
            return resultat.toString();
        }
        
        // Comprovem si hi ha un interrogant
        CasillaInterrogante interrogant = hiHaInterrogant(novaPosicio);
        if (interrogant != null) {
            String resultatEvent = interrogant.activarEvento(jugador);
            resultat.append("Has caigut en una casella d'interrogant! " + resultatEvent);
            return resultat.toString();
        }
        
        // Si no hi ha cap casella especial
        resultat.append("Has arribat a una casella normal.");
        return resultat.toString();
    }
    public void mostrarTauler() {
        System.out.println("\n=== TAULER DEL JOC DEL PINGÜÍ ===");
        for (int i = 0; i < 50; i++) {
            String casella = "[" + i + "]";
            
            // Comprovem si hi ha elements especials a la casella
            if (hiHaOs(i) != null) {
                casella += "(Ós)";
            } 
            if (hiHaForat(i) != null) {
                casella += "(Forat)";
            }
            if (hiHaTrineu(i) != null) {
                casella += "(Trineu)";
            }
            if (hiHaInterrogant(i) != null) {
                casella += "(?)";
            }
            
            System.out.print(casella + " ");
            
            // Canviem de línia cada 10 caselles per millorar la visualització
            if ((i + 1) % 10 == 0) {
                System.out.println();
            }
        }
        System.out.println("\n================================");
    }
    public ArrayList<Pingüino> jugadorsEnCasella(ArrayList<Pingüino> jugadors, int posicio) {
        ArrayList<Pingüino> jugadorsEnCasella = new ArrayList<>();
        for (Pingüino jugador : jugadors) {
            if (jugador.getPosicio() == posicio) {
                jugadorsEnCasella.add(jugador);
            }
        }
        return jugadorsEnCasella;
    }
}