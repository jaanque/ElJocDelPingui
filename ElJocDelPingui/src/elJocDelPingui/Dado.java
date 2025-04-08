package elJocDelPingui;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Dado {
    private Random random;

    // Lista para guardar hasta 3 dados especiales (usamos Strings: "RAPIDO", "LENTO")
    private List<String> dadosEspeciales;

    // Constructor
    public Dado() {
        random = new Random();
        dadosEspeciales = new ArrayList<>();
    }

    // =============================
    // MÉTODOS PARA TIRAR DADOS
    // =============================

    // Dado básico: siempre disponible (1-6)
    public int tirarDadoBasico() {
        return random.nextInt(6) + 1;
    }

    // Dado rápido: valores entre 5 y 10
    private int tirarDadoRapido() {
        return random.nextInt(6) + 5;
    }

    // Dado lento: valores entre 1 y 3
    private int tirarDadoLento() {
        return random.nextInt(3) + 1;
    }

    // Tirar uno de los dados especiales guardados (por su posición)
    public int usarDadoEspecial(int indice) {
        if (indice >= 0 && indice < dadosEspeciales.size()) {
            String tipo = dadosEspeciales.remove(indice);

            if (tipo.equals("RAPIDO")) {
                System.out.println("Usaste un dado RÁPIDO.");
                return tirarDadoRapido();
            } else if (tipo.equals("LENTO")) {
                System.out.println("Usaste un dado LENTO.");
                return tirarDadoLento();
            } else {
                System.out.println("Tipo de dado desconocido.");
                return -1;
            }
        } else {
            System.out.println("Índice inválido.");
            return -1;
        }
    }

    // =============================
    // INVENTARIO DE DADOS ESPECIALES
    // =============================

    // Agregar un dado especial si hay espacio (máx 3)
    public boolean agregarDadoEspecial(String tipo) {
        if (dadosEspeciales.size() < 3) {
            if (tipo.equals("RAPIDO") || tipo.equals("LENTO")) {
                dadosEspeciales.add(tipo);
                System.out.println("Agregado dado " + tipo + " al inventario.");
                return true;
            } else {
                System.out.println("Tipo de dado inválido.");
                return false;
            }
        } else {
            System.out.println("¡Inventario de dados especiales lleno!");
            return false;
        }
    }

    // Mostrar los dados especiales actuales
    public void mostrarDadosEspeciales() {
        System.out.println("Dados especiales en inventario:");
        for (int i = 0; i < dadosEspeciales.size(); i++) {
            System.out.println("[" + i + "] " + dadosEspeciales.get(i));
        }
    }

    // Saber cuántos dados especiales tienes
    public int cantidadDadosEspeciales() {
        return dadosEspeciales.size();
    }
}
