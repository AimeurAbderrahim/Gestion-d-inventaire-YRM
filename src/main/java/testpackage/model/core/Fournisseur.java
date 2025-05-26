package testpackage.model.core;

import testpackage.model.errors.NotNullException;

public class Fournisseur {
	private String id_f;
	private String nom;
	private String adresse;
	private String numero_tlph;
	private String email;
	private String NIF;
	private String NIS;
	private String RC;

	public Fournisseur() {}

	public Fournisseur(String nom, String adresse, String numero_tlph, String email, String NIF, String NIS, String RC) throws NotNullException {
		if ((numero_tlph == null && email == null) ||
				nom == null || NIF == null || NIS == null || RC == null) {
			throw new NotNullException("Manque d'information Fournisseur");
		}
		this.nom = nom;
		this.adresse = adresse;
		this.numero_tlph = numero_tlph;
		this.email = email;
		this.NIF = NIF;
		this.NIS = NIS;
		this.RC = RC;
	}

	public String getId_f() {
		return id_f;
	}

	public void setId_f(String id_f) {
		this.id_f = id_f;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public String getNumero_tlph() {
		return numero_tlph;
	}

	public void setNumero_tlph(String numero_tlph) {
		this.numero_tlph = numero_tlph;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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
