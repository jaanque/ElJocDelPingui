package elJocDelPingui;

//CLASE
public class SueloQuebradizo {

	private int posicio;
	private Pingüino Jugador;
	
	
	//CONSTRUCTOR CLASE 
	public SueloQuebradizo (int posicio, Pingüino Jugador) {
	
		this.posicio = posicio;
		this.Jugador = Jugador;
}

	public void actualizarJugador (Pingüino nuevoJugador) {
		this.Jugador.setSueloQuebradizo(nuevoJugador.getSueloQuebradizo()); 
	}
		
	//GETTERS
	
		public int getposicio() {return posicio;}
		
		
		
	//SETTERS

		public void setposicio (int posicio) {this.posicio = posicio;}
	
	
}
