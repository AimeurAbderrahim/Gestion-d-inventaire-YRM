package testpackage.model.core;

import testpackage.model.errors.NotNullException;

import java.util.List;

public class Fournisseur {
	private String id_f ;
	private String nom_f ;
	private String adresse ;
	private String num_tel ;
	private String mail_f ;
	private String NIF;
	private String NIS;
	private String RC;

	public Fournisseur(){}
	public Fournisseur(String nom_f, String adresse, String num_tel, String mail_f, String NIF, String NIS, String RC) throws NotNullException {
		if(
				(num_tel	==	null && mail_f	==	null) 	|| 
				nom_f		==	null 				|| 
				NIF		==	null 				|| 
				NIS		==	null 				|| 
				RC		==	null
		){
			throw new NotNullException("Manque d'information Fournisseur");
		}
		this.nom_f = nom_f;
		this.adresse = adresse;
		this.num_tel = num_tel;
		this.mail_f = mail_f;
		this.NIF = NIF;
		this.NIS = NIS;
		this.RC = RC;
		// for test
		// this.id_f = implementsId();
	}


	public String getId_f() {
		return id_f;
	}

	// NOTE: this method is useless
	public void setId_f(String id_f) {
		this.id_f = id_f;
	}

	public String getNom_f() {
		return nom_f;
	}

	public void setNom_f(String nom_f) {
		this.nom_f = nom_f;
	}

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public String getNum_tel() {
		return num_tel;
	}

	public void setNum_tel(String num_tel) {
		this.num_tel = num_tel;
	}

	public String getMail_f() {
		return mail_f;
	}

	public void setMail_f(String mail_f) {
		this.mail_f = mail_f;
	}

	public String getNIF() {
		return NIF;
	}

	public void setNIF(String NIF) {
		this.NIF = NIF;
	}

	public String getNIS() {
		return NIS;
	}

	public void setNIS(String NIS) {
		this.NIS = NIS;
	}

	public String getRC() {
		return RC;
	}

	public void setRC(String RC) {
		this.RC = RC;
	}
}
