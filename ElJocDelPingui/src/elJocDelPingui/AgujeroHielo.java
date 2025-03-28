package elJocDelPingui;

public class AgujeroHielo {
//CLASE 
private int posicio;   		//Posició en el tauler
private boolean forat; 		//Forat obert o tancat.
	
//CONSTRUCTOR 
public AgujeroHielo(int posicio) {
	this.posicio = posicio;  //Posió de casilla del forat.
	this.forat = true;   //Forat obert  si false Forat tancat
}

//GETTERS

public int getposicio() {return posicio;}
public boolean isforat()  {return forat;}

//SETTERS
	
public void setposicio (int posicio) {this.posicio = posicio;}
public void setforat (boolean  forat) {this.forat = forat;}

}
