package elJocDelPingui;
import java.util.Scanner;
import java.util.ArrayList;


public class Main {
	 public static void main(String[] args) {
	 
	 Scanner s = new Scanner(System.in);
 //-----------------------------------------------------------------------------------------------------------------|	
	 int opcion = 0;//                                                                    					     	|
//  																											 	|
	    System.out.println("BENVOLGUTS AL JOC DEL PIGÜÍ");//                                                     	|
	    System.out.println("1. JUGAR");//                                                                        	|
	    System.out.println("2. COM JUGAR Y REGLES");//															 	|
	    System.out.println("3.SORTIR DEL JOC");//																				 	|
	   //																									     	|
	    while (true) {//																						  	
	        System.out.print("Escull una opció (1-3): ");// 												      	
	        if (s.hasNextInt()) {//																				   	
	            opcion = s.nextInt();//																			   	
	            if (opcion >= 1 && opcion <= 3) {//																   	
	                break; // Sale del bucle si la opción es válida//											  	
	            } else {//																						   	
	                System.out.println("Sisplau ingressi un número entre 1 i 3.");//								MENU OPCIONS INCI JOC 
	            }//																									
	        } else {//																								
	            System.out.println("Entrada no vàlida. Sisplau ingressi un número.");//								
	            s.next(); // Limpia la entrada incorrecta															
	        }//																										
    }//																												
//																														
	    switch (opcion) {//																							
	        case 1://																								
            Partida.iniciarPartida();																				
	            break;//																							|
	        case 2://																								|
	            System.out.println("Aquí tens les regles del joc...");//											|
	            break;//																							|
	        case 3://																								|
	            System.out.println("Sortint del joc. Fins aviat!");//												|
	            break;//																							|
	    } //--------------------------------------------------------------------------------------------------------|
	    
	}
}