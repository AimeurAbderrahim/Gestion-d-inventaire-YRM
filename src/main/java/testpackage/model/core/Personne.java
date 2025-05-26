
package testpackage.model.core;

import testpackage.model.core.Emplacement;
import testpackage.model.errors.NotNullException;

import java.time.LocalDate;

public class Personne {
	private String id_p ;
	private String nom ;
	private String prenom ;
	private LocalDate date_naissance_P ;
	private String adresse ;
	private String mail_P ;
	private String numero_tel_personne ;
	private boolean hasAccount;

	private String id_emplacement;
	private String id_c;

	public Personne(){}

	public Personne(String nom, String prenom, LocalDate date_naissance_P, String adresse, String email, String numero_tel_personne, boolean hasAccount, String emplacement , String id_c) throws NotNullException {
		if(
				(numero_tel_personne	==	null 	&& 	email	==	null) 	|| 
				nom			==	null 					|| 
				adresse			==	null 					|| 
				date_naissance_P	==	null
		){
			throw new NotNullException("Manque d'information Fournisseur");
		}
		this.nom = nom;
		this.prenom = prenom;
		this.date_naissance_P = date_naissance_P;
		this.adresse = adresse;
		this.mail_P = email;
		this.numero_tel_personne = numero_tel_personne;
		this.hasAccount = hasAccount;
		this.id_emplacement = emplacement;
		this.id_c = id_c ;
	}


	public void setAccount(boolean value){
		this.hasAccount = value;
	}

	public boolean hasAccount(){
		return this.hasAccount;
	}

	public String getId_emplacement(){
		return this.id_emplacement;
	}
	public void setId_emplacement(String id){
		this.id_emplacement = id;
	}
	public String getId_c(){
		return this.id_c;
	}
	public void setId_c(String id){
		this.id_c = id;
	}

	public String getId_p() {
		return id_p;
	}

	public void setId_p(String id) {
		this.id_p = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public LocalDate getLocalDate() {
		return date_naissance_P;
	}

	public void setLocalDate(LocalDate date_naissance_P) {
		this.date_naissance_P = date_naissance_P;
	}

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public String getEmail() {
		return mail_P;
	}

	public void setEmail(String email) {
		this.mail_P = email;
	}

	public String getNumero_tel_personne() {
		return numero_tel_personne;
	}

	public void setNumero_tel_personne(String numero_tel_personne) {
		this.numero_tel_personne = numero_tel_personne;
	}
}

