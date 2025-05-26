package testpackage.model.core;

import java.time.LocalDate;

public class Personne {
	private String id_p;
	private String nom;
	private String prenom;
	private LocalDate date_naissance;
	private String email;
	private String adresse;
	private String numero_tlph;
	private boolean avoir_compte;
	private String id_emplacement;
	private String id_c; // clé étrangère vers Compte

	public Personne() {}

	public Personne(String nom, String prenom, LocalDate date_naissance, String email, String adresse,
					String numero_tlph, boolean avoir_compte, String id_emplacement, String id_c) {
		this.nom = nom;
		this.prenom = prenom;
		this.date_naissance = date_naissance;
		this.email = email;
		this.adresse = adresse;
		this.numero_tlph = numero_tlph;
		this.avoir_compte = avoir_compte;
		this.id_emplacement = id_emplacement;
		this.id_c = id_c;
	}

	public String getId_p() { return id_p; }
	public void setId_p(String id_p) { this.id_p = id_p; }

	public String getNom() { return nom; }
	public void setNom(String nom) { this.nom = nom; }

	public String getPrenom() { return prenom; }
	public void setPrenom(String prenom) { this.prenom = prenom; }

	public LocalDate getDate_naissance() { return date_naissance; }
	public void setDate_naissance(LocalDate date_naissance) { this.date_naissance = date_naissance; }

	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }

	public String getAdresse() { return adresse; }
	public void setAdresse(String adresse) { this.adresse = adresse; }

	public String getNumero_tlph() { return numero_tlph; }
	public void setNumero_tlph(String numero_tlph) { this.numero_tlph = numero_tlph; }

	public boolean isAvoir_compte() { return avoir_compte; }
	public void setAvoir_compte(boolean avoir_compte) { this.avoir_compte = avoir_compte; }

	public String getId_emplacement() { return id_emplacement; }
	public void setId_emplacement(String id_emplacement) { this.id_emplacement = id_emplacement; }

	public String getId_c() { return id_c; }
	public void setId_c(String id_c) { this.id_c = id_c; }
}
