package elJocDelPingui;

//CLASE
public class Jugador {

	private String nom; 			//Nom del jugador .
	private String color;			//Color del jugador.
	private int posicio;			//Posició del jugador.
	private Inventario Inventario;  //Inventario del jugador. 
	
//CONSTRUCTOR CLASE 
public Jugador (String nom, String color) {
	this.nom = nom ;			//Fica el nom.
	this.color = color;			//Fica el color. 
	this.posicio = posicio;		//Fica posicio inicial del jugador.	
	
}
	
public void actualitzarInventari (Inventario Inventario) {//--------|
	this.Inventario.setbolesNeu(Inventario.getbolesNeu()); //		|
	this.Inventario.setPeixos(Inventario.getpeixos()); //			|--> ACTUALITZA INVENTARIO JUGADOR
	this.Inventario.setdaus(Inventario.getdaus()); //---------------|

}
//GETTERS

	public String getnom() {return nom;}		
	public String getcolor() {return color;}		
	public int getposicio() {return posicio;}
	public Inventario getInventario() {return Inventario;}
	
	public Object getSueloQuebradizo() {return null;}
	
//SETTERS
    	
	public void setnom (String nom) {this.nom = nom;}
	public void setcolor (String color) {this.color = color;}
	public void setposicio (int posicio) {this.posicio = posicio;}
	public void setInventario (Inventario Inventario) {this.Inventario = Inventario;}

	public void setSueloQuebradizo(Object sueloQuebradizo) {
		
	}

	
		
		
}
	
	
