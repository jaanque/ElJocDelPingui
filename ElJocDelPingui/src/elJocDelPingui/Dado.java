package elJocDelPingui;
import java.util.Random;
public class Dado {
    protected Random random;
    public Dado() {
        this.random = new Random();
    }
    public int tirarDado() {
        System.out.println("TIRANDO DADO BÁSICO...");
        int resultado = random.nextInt(3) + 1; // Valor entre 1 y 3
        System.out.println("Resultado: " + resultado);
        return resultado;
    }
}