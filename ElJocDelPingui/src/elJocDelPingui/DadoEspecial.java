package elJocDelPingui;
import java.util.Random;

public class DadoEspecial extends Dado {
    public DadoEspecial() {
        super(); //TRUCAR (LLAMAR) A LA FUNCIÓ DADO 
    }
    public int tirarDado() {
        System.out.println("TIRANDO DADO ESPECIAL...");
        int resultado = random.nextInt(6) + 5;  //VALOR DEL ESPECIAL ENTRE 5 I 10 
        System.out.println("Resultado: " + resultado);
        return resultado;
    }
}