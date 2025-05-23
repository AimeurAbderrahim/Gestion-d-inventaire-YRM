package testpackage.model.core;

import testpackage.model.enumeration.Roles;

public class Compte {
	private String id_C ;
	private String nom_utilisateur ;
	private String mot_de_passe ;
	private Roles roles ;

	public Compte(){}
	public Compte(String nom_utilisateur, String mot_de_passe, Roles roles )throws NullPointerException {
		this.nom_utilisateur = nom_utilisateur;
		this.mot_de_passe = mot_de_passe;
		this.roles = roles;
	}

	public String getId_c() {
		return id_C;
	}

	public void setId_c(String id_C) {
		this.id_C = id_C;
	}

	public String getNom_utilisateur() {
		return nom_utilisateur;
	}

	public void setNom_utilisateur(String nom_utilisateur) {
		this.nom_utilisateur = nom_utilisateur;
	}

	public String getMot_de_passe() {
		return mot_de_passe;
	}

	public void setMot_de_passe(String mot_de_passe) {
		this.mot_de_passe = mot_de_passe;
	}

	public Roles getRoles() {
		return roles;
	}

	public void setRoles(Roles roles) {
		this.roles = roles;
	}
}
