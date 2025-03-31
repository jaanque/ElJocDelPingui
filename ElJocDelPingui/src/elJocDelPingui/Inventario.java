package elJocDelPingui;


//CLASSE INVENTARI 
public class Inventario{

	private int bolesNeu; 	//Boles de neu del Jugador.  
	private int daus;	  	//Daus del Jugador. 
	private int peixos;		//Peixos Jugador. 
	
//CONSTRUCTOR INVETARI 
public Inventario(int bolesNeu,int daus,int peixos) {
	this.bolesNeu = bolesNeu ;  //Boles de Inici.
	this.daus = daus; 		//Daus Inici.										//COMENÇEM CADA INVENTARI AMB 0 OBJECTES DE CADA 
	this.peixos = peixos; 	//Peixos per inici.
	}

	//GETTERS 
	public int getbolesNeu() {return bolesNeu;}
	public int getdaus() {return daus;}
	public int getpeixos() {return peixos;}
	
	//SETTERS 
	public void setbolesNeu(int bolesNeu) {this.bolesNeu = bolesNeu;} 
	public void setdaus(int daus) {this.daus = daus;}
	public void setPeixos(int Peixos) {this.peixos = peixos;}  
}
