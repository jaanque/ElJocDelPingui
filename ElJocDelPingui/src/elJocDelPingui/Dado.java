package elJocDelPingui;
import java.util.Random;
public class Dado {

	
	//DAU BÀSIC AS DE AVANÇAR DE 1 A 3 POSICIONS 
	
	 public static void tirarDauBasic(String[] args) {
		 Random random = new Random(); 
		 
		 System.out.println("TIRANDO  DADO... ");
		 int resultatdau = random.nextInt(3)+1;
		 
		 System.out.println(resultatdau);
		 
		 
	 }
}	

	
	
	
	
	
	
