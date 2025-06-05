package testpackage.model.core;

import java.time.LocalDate;

public class BonReception {
	private String id_bon;
	private LocalDate date_reception;
	private String id_fournisseur;

	public BonReception() {}

	public BonReception(String id_bon, LocalDate date_reception, String id_fournisseur) {
		this.id_bon = id_bon;
		this.date_reception = date_reception;
		this.id_fournisseur = id_fournisseur;
	}

	public String getId_bon() {
		return id_bon;
	}

	public void setId_bon(String id_bon) {
		this.id_bon = id_bon;
	}

	public LocalDate getDate_reception() {
		return date_reception;
	}

	public void setDate_reception(LocalDate date_reception) {
		this.date_reception = date_reception;
	}

	public String getId_fournisseur() {
		return id_fournisseur;
	}

	public void setId_fournisseur(String id_fournisseur) {
		this.id_fournisseur = id_fournisseur;
	}
}
