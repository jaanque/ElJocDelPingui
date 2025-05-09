package modelo;

public class Partida {
    private int id;
    private String dataHora;

    public Partida(int id, String dataHora) {
        this.id = id;
        this.dataHora = dataHora;
    }

    public int getId() {
        return id;
    }

    public String getDataHora() {
        return dataHora;
    }
}
