package modelo;

public class Evento {

public class Motos {
	//CLASE 
	private int posicio;   		//Posició en el tauler
	private Trineo Trineo;	//AGAFEM TRINEU 
	//CONSTRUCTOR 
	public Motos(int posicio) {
		this.posicio = posicio;  //Posió de casilla.
	}
	
	//UBICACIÓ TRINEO 
	
	public void Trineo (Trineo Trineo) {
		this.Trineo.setposiciotrineo(Trineo.getposiciotrineo()); 
	}
	//GETTERS

	public int getposicio() {return posicio;}
	public Trineo getTrineo() {return Trineo;}

	//SETTERS
		
	public void setposicio (int posicio) {this.posicio = posicio;}
	public void setTrineo (Trineo Trineo) {this.Trineo = Trineo;}
	}

}
