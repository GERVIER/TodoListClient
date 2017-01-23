package model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Tache {

	public String tacheID;
	public String texte;
	public String priorite;
	public String etat;
	public Date dateFin;
	public Date dateCreation;
	public SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");

	
	public Tache(){}
	
	public Tache(String tacheID, String texte, String priorite, String etat, Date dateFin, Date dateCreation){
		this.tacheID = tacheID;
		this.texte = texte;
		this.priorite = priorite;
		this.etat = etat;
		this.dateFin = dateFin;
		this.dateCreation = dateCreation;
	}
	
	public String toString(){
		String str = "Txt : " + this.texte + "\n"
				+ " Priorite : " + this.priorite + "\n"
				+ " Etat : " + this.etat + "\n"
				+ " Date Fin : " + this.dateFin + "\n"
				+ " Date Creation : " + this.dateCreation;
		return str;
	}
	
}
