package elJocDelPingui;

//CLASE 
public class FocaCPU {
	
	private int posicio; 	//Posició inicial de la FOCA
	private int velocitat;  //Velocitat de la FOCA 

	
//CONSTRUCTOR 
public FocaCPU() {	
	this.posicio = 0; 		//La posició inicial és 0.
	this.velocitat = 1; 	//Velocitat inical. 
	}

//GETTERS 
	public int getposicio() {return posicio;}
	public int getvelocitat() {return velocitat;}

//SETTERS
	public void setposicio (int posicio) {this.posicio= posicio;}
	public void setvelocitat(int velocitat) {this.velocitat = velocitat;}
}