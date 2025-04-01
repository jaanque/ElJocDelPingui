package elJocDelPingui;
import java.util.Random;
public class DadoEspecial {

	
	//DAU ESPECIAL A DE AVANÇAR DE 5 A 10 POSICIONS 
	
	 public static void tirarDauEspecial(String[] args) {
		 Random random = new Random(); 
		 
		 System.out.println("TIRANDO  DADO... ");
		 int resultatdau = random.nextInt(10)+5;
		 
		 System.out.println(resultatdau);
		 
		 
	 }
}	

		
