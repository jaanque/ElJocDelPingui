package elJocDelPingui;

//CLASE
public class SueloQuebradizo {

	private int posicio;
	private String Jugador;
	
	
	//CONSTRUCTOR CLASE 
	public SueloQuebradizo (int posicio, String Jugador) {
	
		this.posicio = posicio;
		
}

	public void actualizarJugador (Jugador Jugador) {
		this.Jugador.setSueloQuebradizo(Jugador.getSueloQuebradizo()); 
	}
		
	//GETTERS
	
		public int getposicio() {return posicio;}
		
		
		
	//SETTERS

		public void setposicio (int posicio) {this.posicio = posicio;}
	
	
}
