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
	static int comptageP ;

	public Personne(String nom, String prenom, LocalDate date_naissance_P, String adresse, String email, String numero_tel_personne, Emplacement emplacement) throws NotNullException {
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
		// this.id_p = implementsId();
	}

	public String getId_p() {
		return id_p;
	}

	public void setId_p(String id_p) {
		this.id_p = id_p;
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

	// @Override
	// public String implementsId() {
	// 	return String.format("%04d",comptageP);
	// }
}
