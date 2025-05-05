package modelo;

public class Trineo {
	
	//CLASE 
	private int posiciotrineo;   		//Posició en el tauler
		
	//CONSTRUCTOR 
	public Trineo(int posiciotrineo) {
		this.posiciotrineo = posiciotrineo;  //Posió de trineo
	}

	//GETTERS

	public int getposiciotrineo() {return posiciotrineo;}

	//SETTERS
	public void setposiciotrineo (int posiciotrineo) {this.posiciotrineo = posiciotrineo;}
	
}
