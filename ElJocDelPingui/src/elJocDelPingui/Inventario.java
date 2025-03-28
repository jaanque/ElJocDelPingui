package elJocDelPingui;


//CLASSE INVENTARI 
public class Inventario{

	private int bolesNeu; 	//Boles de neu del Jugador.  
	private int daus;	  	//Daus del Jugador. 
	private int peixos;		//Peixos Jugador. 
	
//CONSTRUCTOR INVETARI 
public Inventario() {
	this.bolesNeu = 0 ;  //Boles de Inici.
	this.daus = 0; 		//Daus Inici.										//COMENÇEM CADA INVENTARI AMB 0 OBJECTES DE CADA 
	this.peixos = 0; 	//Peixos per inici.
	}

	//GETTERS 
	public int getbolesNeu() {return bolesNeu;}
	public int daus() {return daus;}
	public int peixos() {return peixos;}
	
	//SETTERS 
	public void setbolesNeu(int bolesNeu) {this.bolesNeu = bolesNeu;} 
	public void setdaus(int daus) {this.daus = daus;}
	public void setPeixos(int Peixos) {this.peixos = peixos;}  
}
