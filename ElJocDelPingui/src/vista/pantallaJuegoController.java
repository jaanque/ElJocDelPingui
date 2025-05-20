package vista;

import controlador.GestorPartides;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import modelo.EventosJuego;
import java.sql.ResultSet;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.Random;

public class pantallaJuegoController {
	// Variables per controlar la quantitat de daus especials i objectes recollits
	private int dadosRapidos = 0;          // Quantitat de daus ràpids disponibles
	private int dadosLentos = 0;           // Quantitat de daus lents disponibles
	private int contadorRapido = 0;        // Comptador intern de daus ràpids obtinguts (pot servir per l’inventari)
	private int contadorLento = 0;         // Comptador intern de daus lents
	private int contadorPeces = 0;         // Quantitat de peixos recollits
	private boolean turnoBloqueado = false; // Indica si el jugador ha perdut el torn

	// Connexió a la base de dades i identificadors
	private Connection con;                // Connexió a la base de dades
	private int idJugador;                 // ID del jugador actiu
	private int idPartidaActual = -1;      // ID de la partida carregada (si n’hi ha)
	private boolean partidaCarregada = false;

	// Elements de la interfície definits al FXML
	@FXML private MenuItem newGame, saveGame, loadGame, quitGame;  // Menú superior
	@FXML private Button dado, rapido, lento, peces, nieve, reiniciar, cambiarAvatarBtn; // Botons d’interacció
	@FXML private Text dadoResultText, rapido_t, lento_t, peces_t, nieve_t, eventos;     // Textos informatius
	@FXML private GridPane tablero;                  // Tauler de joc
	@FXML private Circle P1, P2, P3, P4;              // Fitxes dels jugadors

	// Variables de posició i configuració del tauler
	private int p1Position = 0;                      // Posició actual del jugador 1
	private final int COLUMNS = 5;                   // Columnes del tauler
	private final int ROWS = 10;                     // Files del tauler
	private String[][] mapaEventos = new String[ROWS][COLUMNS]; // Matriu d’esdeveniments del tauler

	// Variables per a la gestió de l'avatar del jugador
	private Image avatarSeleccionado = null;         // Imatge de l’avatar seleccionat
	private ImageView avatarView = null;             // Node gràfic de l’avatar al tauler
	private int contadorBolasNieve = 0;              // Nombre de boles de neu recollides

	// Mètode per establir la connexió i l'ID del jugador
	public void setDatosConexionYJugador(Connection con, int idJugador) {
	    this.con = con;
	    this.idJugador = idJugador;
	}

	// Carrega una partida específica de la base de dades
	public void carregarPartidaDesDeBD(int idPartida) {
	    this.idPartidaActual = idPartida;
	    this.partidaCarregada = true; // Marquem que és una partida carregada
	    GestorPartides.carregar(con, this, idJugador, idPartida);
	}

	// Alternativa per carregar una partida (potser usada internament)
	public void carregarPartida(int idPartida) {
	    GestorPartides.carregar(con, this, idJugador, idPartida);
	}

	// Inicialització automàtica quan es carrega la vista (FXML)
	@FXML
	private void initialize() {
	    ajustarInterficie75();
	    eventos.setText("¡El juego ha comenzado!");
	    afegirNumerosCaselles();
	    if (!partidaCarregada) {
	        generarEventosAleatorios(); // ✅ Només si és nova partida
	    }
	}

	// Ajusta visualment els elements de la interfície al 75% de la mida original
	private void ajustarInterficie75() {
	    tablero.setScaleX(0.75);
	    tablero.setScaleY(0.75);
	    P1.setRadius(P1.getRadius() * 0.75);
	    P2.setRadius(P2.getRadius() * 0.75);
	    P3.setRadius(P3.getRadius() * 0.75);
	    P4.setRadius(P4.getRadius() * 0.75);
	    dadoResultText.setFont(new Font(24));
	    rapido_t.setFont(new Font(22));
	    lento_t.setFont(new Font(22));
	    peces_t.setFont(new Font(22));
	    nieve_t.setFont(new Font(22));
	    eventos.setFont(new Font(18));
	}
	
	private void afegirNumerosCaselles() {
	    int contador = 1;
	    for (int fila = 0; fila < ROWS; fila++) {
	        for (int col = 0; col < COLUMNS; col++) {
	            Label label = new Label(String.valueOf(contador));
	            label.setStyle("-fx-text-fill: gray; -fx-font-size: 14px;");
	            GridPane.setRowIndex(label, fila);
	            GridPane.setColumnIndex(label, col);
	            GridPane.setHalignment(label, HPos.RIGHT);   // Alineat a la dreta
	            GridPane.setValignment(label, VPos.BOTTOM);  // Alineat a baix
	            GridPane.setMargin(label, new Insets(0, 4, 2, 0)); // Una mica separat del cantó
	            tablero.getChildren().add(label);
	            contador++;
	        }
	    }
	}

	// Assigna de manera aleatòria esdeveniments a diferents caselles del tauler
	private void generarEventosAleatorios() {
	    Random rand = new Random();
	    String[] eventos = {"oso", "agujero", "trineo", "interrogante", "pez", "agujeroHielo", "nieve"};
	    int totalEventos = 25; 

	    int colocados = 0;
	    while (colocados < totalEventos) {
	        int fila = rand.nextInt(ROWS);
	        int columna = rand.nextInt(COLUMNS);

	        // Evitem la casella inicial i final
	        if ((fila == 0 && columna == 0) || (fila == 9 && columna == 4)) continue;

	        // Evitem sobreescriure un esdeveniment ja col·locat
	        if (mapaEventos[fila][columna] != null) continue;

	        // Triem un esdeveniment aleatori
	        String evento = eventos[rand.nextInt(eventos.length)];
	        mapaEventos[fila][columna] = evento;

	        // Mostrem la icona al tauler
	        String ruta = "/resources/" + evento + ".png";
	        URL recurso = getClass().getResource(ruta);
	        if (recurso != null) {
	            mostrarIconoEvento(recurso.toExternalForm(), fila, columna);
	        }

	        colocados++;
	    }
	}


	// Mou el jugador P1 un nombre determinat de caselles
	private void moveP1(int steps) {
	    // Suma les caselles al comptador de posició
	    p1Position += steps;

	    // Si supera o arriba a la darrera casella, es col·loca a la final i mostra guanyador
	    if (p1Position >= 49) {
	        p1Position = 49;

	        // Deixem que el canvi visual es mostri primer
	        javafx.application.Platform.runLater(() -> {
	            mostrarGuanyador();
	        });
	    }

	    // Calcula la fila i columna corresponent a la nova posició
	    int row = p1Position / COLUMNS;
	    int col = p1Position % COLUMNS;

	    // Mou la fitxa blava (P1) a la nova posició
	    GridPane.setRowIndex(P1, row);
	    GridPane.setColumnIndex(P1, col);

	    // Si hi ha un avatar personalitzat, també el mou a la mateixa posició
	    if (avatarSeleccionado != null && avatarView != null) {
	        GridPane.setRowIndex(avatarView, row); //coloca en fila
	        GridPane.setColumnIndex(avatarView, col);//coloca en columna
	    }

	    // Activa l'esdeveniment si hi ha un esdeveniment en aquesta casella
	    activarEventoEn(row, col);
	}

	// Mostra una alerta quan el jugador arriba a la casella final
	private void mostrarGuanyador() {
	    Alert alert = new Alert(Alert.AlertType.INFORMATION);
	    alert.setTitle("🎉 Partida finalitzada");
	    alert.setHeaderText("Has arribat al final del tauler!");
	    alert.setContentText("Felicitats " + controlador.Sessio.getNickname() + ", has guanyat la partida!");
	    alert.showAndWait();
	}

	// Executa l'esdeveniment de la casella si n'hi ha un assignat
	private void activarEventoEn(int fila, int columna) {
	    String tipo = mapaEventos[fila][columna];
	    if (tipo == null) return; // Si no hi ha cap esdeveniment, surt

	    // Executa el mètode corresponent segons el tipus d'esdeveniment
	    switch (tipo) {
	        case "oso": EventosJuego.eventoOso(this); break;
	        case "agujero": EventosJuego.eventoAgujero(this); break;
	        case "trineo": EventosJuego.eventoTrineo(this); break;
	        case "interrogante": EventosJuego.eventoInterrogante(this); break;
	        case "pez": EventosJuego.eventoPez(this); break;
	        case "agujeroHielo": EventosJuego.eventoAgujeroHielo(this); break;
	        case "nieve": EventosJuego.eventoNieve(this); break;
	    }
	}

	// Reinicia la posició del jugador i de l'avatar al principi del tauler
	public void tornarAlInici() {
	    p1Position = 0;
	    GridPane.setRowIndex(P1, 0);
	    GridPane.setColumnIndex(P1, 0);

	    if (avatarView != null) {
	        GridPane.setRowIndex(avatarView, 0);
	        GridPane.setColumnIndex(avatarView, 0);
	    }
	}

	// Mostra una icona (imatge) a una casella específica del tauler
	private void mostrarIconoEvento(String ruta, int fila, int columna) {
	    try {
	        Image image = new Image(ruta);
	        ImageView icono = new ImageView(image);
	        icono.setFitWidth(P1.getRadius() * 2);     // Amplada igual al diàmetre del cercle del jugador
	        icono.setFitHeight(P1.getRadius() * 2);    // Alçada igual al diàmetre del cercle
	        icono.setPreserveRatio(true);              // Manté la proporció de la imatge
	        GridPane.setHalignment(icono, HPos.CENTER);
	        GridPane.setValignment(icono, VPos.CENTER);
	        tablero.add(icono, columna, fila);         // Afegeix la imatge al tauler
	    } catch (Exception e) {
	        e.printStackTrace();                       // Mostra l'error si no es pot carregar la imatge
	    }
	}


    // --- GESTIÓ DE PARTIDA ---

    // Guarda l'estat actual de la partida (posició + inventari) a la base de dades
    @FXML private void handleGuardarPartida() {
        GestorPartides.guardar(con, this, idJugador, LocalDateTime.now());
    }

    // Tirada de dau normal (1-6), si no ha perdut el torn
    @FXML private void handleDado(ActionEvent event) {
        if (turnoBloqueado) {
            eventos.setText("❌ Has perdut el torn!");
            turnoBloqueado = false;
            return;
        }
        int result = new Random().nextInt(6) + 1;
        dadoResultText.setText("Ha salido: " + result);
        moveP1(result);

        GestorPartides.guardar(con, this, idJugador, LocalDateTime.now());
    }


    // Usa un dau ràpid (5-10), si en té
    @FXML private void handleRapido() {
        if (dadosRapidos > 0) {
            dadosRapidos--;
            rapido_t.setText("Dado rápido: " + dadosRapidos);
            int avan = new Random().nextInt(6) + 5;
            dadoResultText.setText("Rápido: avanzas " + avan + " casillas");
            moveP1(avan);
        } else {
            mostrarInfo("No tienes dados rápidos.");
        }
    }

    // Usa un dau lent (1-3), si en té
    @FXML private void handleLento() {
        if (dadosLentos > 0) {
            dadosLentos--;
            lento_t.setText("Dado lento: " + dadosLentos);
            int avan = new Random().nextInt(3) + 1;
            dadoResultText.setText("Lento: avanzas " + avan + " casillas");
            moveP1(avan);
        } else {
            mostrarInfo("No tienes dados lentos.");
        }
    }
    @FXML
    private void handleCambiarAvatar() {
        ChoiceDialog<String> dialog = new ChoiceDialog<>("avatar1", "avatar1", "avatar2", "avatar3", "avatar4");
        dialog.setTitle("Selecciona un Avatar");
        dialog.setHeaderText("Canvia l'avatar del jugador");
        dialog.setContentText("Tria un avatar:");

        dialog.showAndWait().ifPresent(nomAvatar -> {
            try {
                String ruta = "/avatars/" + nomAvatar + ".png";
                Image novaImatge = new Image(getClass().getResourceAsStream(ruta));
                if (novaImatge.isError()) {
                    mostrarInfo("No s'ha trobat l'avatar.");
                    return;
                }

                // Eliminar l'avatar anterior si existeix
                if (avatarView != null) {
                    tablero.getChildren().remove(avatarView);//nodes
                }

                // Amaguem la bola P1
                P1.setVisible(false);

                // Crear nova imatge
                avatarSeleccionado = novaImatge;
                avatarView = new ImageView(novaImatge);
                avatarView.setPreserveRatio(true);
                avatarView.setFitWidth(P1.getRadius() * 4);  // Doble de gran
                avatarView.setFitHeight(P1.getRadius() * 4);

                // Col·locar a la mateixa posició
                int fila = p1Position / COLUMNS;
                int columna = p1Position % COLUMNS;
                GridPane.setRowIndex(avatarView, fila);
                GridPane.setColumnIndex(avatarView, columna);
                GridPane.setHalignment(avatarView, HPos.CENTER);
                GridPane.setValignment(avatarView, VPos.CENTER);

                tablero.getChildren().add(avatarView);
            } catch (Exception e) {
                e.printStackTrace();
                mostrarInfo("Error carregant l'avatar.");
            }
        });
    }

    // Mètodes buits pendents d'implementar
    @FXML private void handleNewGame() {}
    @FXML private void handleLoadGame() {}
    @FXML private void handleQuitGame() {}
    @FXML private void handlePeces() {}
    @FXML private void handleNieve() {}

    // Reinicia el tauler, jugador, esdeveniments i inventari
    @FXML
    private void handleReiniciar() {
        tablero.getChildren().removeIf(node -> node instanceof ImageView);
        p1Position = 0;
        mapaEventos = new String[ROWS][COLUMNS];
        GridPane.setRowIndex(P1, 0);
        GridPane.setColumnIndex(P1, 0);
        P1.setVisible(true);
        avatarSeleccionado = null;
        avatarView = null;
        dadoResultText.setText("Ha salido: ");
        dadosRapidos = dadosLentos = contadorRapido = contadorLento = contadorPeces = 0;
        rapido_t.setText("Dado rápido: 0");
        lento_t.setText("Dado lento: 0");
        peces_t.setText("Peces: 0");
        nieve_t.setText("Bolas de nieve: 0");
        eventos.setText("¡Nueva partida iniciada!");
        generarEventosAleatorios();
    }

    // --- FUNCIONS AUXILIARS I INVENTARI ---

    public void avançarCaselles(int quantitat) { moveP1(quantitat); }
    public void afegirDadoRapido() { contadorRapido++; rapido_t.setText("Dado rápido: " + contadorRapido); }
    public void afegirDadoLento() { contadorLento++; lento_t.setText("Dado lento: " + contadorLento); }
    public void afegirPez() {
        if (contadorPeces < 2) {
            contadorPeces++;
            peces_t.setText("Peces: " + contadorPeces);
        } else {
            mostrarInfo("Ya tienes el máximo de peces (2).");
        }
    }
    public void bloquejarTorn() { turnoBloqueado = true; }

    // Mostra un missatge emergent amb informació genèrica
    private void mostrarInfo(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // Afegeix un dau lent a l'inventari
    public void incrementarDadoLento() {
        if ((dadosRapidos + dadosLentos) < 3) {
            dadosLentos++;
            lento_t.setText("Dado lento: " + dadosLentos);
        } else {
            mostrarInfo("Inventario lleno: no puedes tener más de 3 dados.");
        }
    }

    // Afegeix un dau ràpid a l'inventari
    public void incrementarDadoRapido() {
        if ((dadosRapidos + dadosLentos) < 3) {
            dadosRapidos++;
            rapido_t.setText("Dado rápido: " + dadosRapidos);
        } else {
            mostrarInfo("Inventario lleno: no puedes tener más de 3 dados.");
        }
    }

    // Afegeix una bola de neu a l'inventari
    public void incrementarBolaNieve() {
        if (contadorBolasNieve < 6) {
            contadorBolasNieve++;
            nieve_t.setText("Bolas de nieve: " + contadorBolasNieve);
        } else {
            mostrarInfo("Ya tienes el máximo de bolas de nieve (6).");
        }
    }

    // --- GUARDAR I CARREGAR ESTAT DE PARTIDA ---

    public static void guardar(Connection con, pantallaJuegoController controlador, int idJugador, LocalDateTime timestamp) {
        int posicio = controlador.getP1Position();
        String inventari = "R:" + controlador.getDadosRapidos() +
                ",L:" + controlador.getDadosLentos() +
                ",P:" + controlador.getPeces() +
                ",N:" + controlador.getBolasNieve(); 
        String estatTauler = controlador.serialitzarMapaEventos();
        GestorPartides.guardarPartida(con, idJugador, posicio, inventari, estatTauler);
    }

    public static void carregar(Connection con, pantallaJuegoController controlador, int idJugador, int idPartida) {
        try {
            ResultSet rs = GestorPartides.carregarPerId(con, idJugador, idPartida);
            if (rs.next()) {
                controlador.setP1Position(rs.getInt("POSICION_ACTUAL"));

                String inventari = rs.getString("INVENTARIO");
                if (inventari != null) {
                    for (String part : inventari.split(",")) {
                        String[] parell = part.split(":");
                        switch (parell[0]) {
                        case "R": controlador.setDadosRapidos(Integer.parseInt(parell[1])); break;
                        case "L": controlador.setDadosLentos(Integer.parseInt(parell[1])); break;
                        case "P": controlador.setPeces(Integer.parseInt(parell[1])); break;
                        case "N": controlador.setBolasNieve(Integer.parseInt(parell[1])); break;
                        }
                    }
                }

                controlador.actualitzarPosicioJugador();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public String serialitzarMapaEventos() {
        StringBuilder sb = new StringBuilder();
        for (int fila = 0; fila < mapaEventos.length; fila++) {
            for (int col = 0; col < mapaEventos[0].length; col++) {
                String valor = mapaEventos[fila][col];
                if (valor != null) {
                    sb.append(fila).append(":").append(col).append("=").append(valor).append(";");
                }
            }
        }
        return sb.toString();
    }
    
    public void deserialitzarMapaEventos(String dades) {
        mapaEventos = new String[ROWS][COLUMNS]; // Reseteja

        String[] entries = dades.split(";");
        for (String entry : entries) {
            if (entry.trim().isEmpty()) continue;
            String[] parts = entry.split("=");
            String[] coord = parts[0].split(":");

            int fila = Integer.parseInt(coord[0]);
            int col = Integer.parseInt(coord[1]);
            String tipus = parts[1];

            mapaEventos[fila][col] = tipus;

            // Mostrem la icona al tauler
            String ruta = "/resources/" + tipus + ".png";
            URL recurso = getClass().getResource(ruta);
            if (recurso != null) {
                mostrarIconoEvento(recurso.toExternalForm(), fila, col);
            }
        }
    }

    // --- GETTERS I SETTERS ---

    public int getP1Position() { return p1Position; }
    public void setP1Position(int pos) { p1Position = pos; }
    public String[][] getMapaEventos() { return mapaEventos; }
    public void setMapaEventos(String[][] mapa) { mapaEventos = mapa; }
    public int getDadosRapidos() { return dadosRapidos; }
    public void setDadosRapidos(int val) { dadosRapidos = val; }
    public int getDadosLentos() { return dadosLentos; }
    public void setDadosLentos(int val) { dadosLentos = val; }
    public int getPeces() { return contadorPeces; }
    public void setPeces(int val) { contadorPeces = val; }
    public void actualitzarPosicioJugador() { moveP1(0); }
    public int getBolasNieve() { return contadorBolasNieve; }
    public void setBolasNieve(int val) { contadorBolasNieve = val; nieve_t.setText("Bolas de nieve: " + contadorBolasNieve); }
    public int getIdPartidaActual() {
        return idPartidaActual;
    }

    public void setIdPartidaActual(int idPartidaActual) {
        this.idPartidaActual = idPartidaActual;
    }

}
