package testpackage.model.core;

import testpackage.model.enumeration.Roles;

public class Compte {
	private String id_C ;
	private String nom_utilisateur ;
	private String mot_de_passe ;
	private Roles roles ;
	static int comptageA=0 ;
	static int comptageS=0 ;
	static int comptageM =0 ;
	public Compte(String nom_utilisateur, String mot_de_passe, Roles roles )throws NullPointerException {
		this.nom_utilisateur = nom_utilisateur;
		this.mot_de_passe = mot_de_passe;
		this.roles = roles;
		// this.id_C = implementsId();
	}

	public String getId_C() {
		return id_C;
	}

	public void setId_C(String id_C) {
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

	// @Override
	// public String implementsId() {
	// 	if (roles==Roles.Administrateur){
	// 		comptageA++;
	// 		return String.format("%03d", comptageA)+"A";
	// 	} else if (roles==Roles.Secretaire) {
	// 		comptageS++;
	// 		return String.format("%03d", comptageS)+"S";
	// 	}
	// 	else {
	// 		comptageM++ ;
	// 		return String.format("%03d", comptageM)+"M";
	// 	}
	// }

	// public static void main(String[] args) {
	// 	Compte compte=new Compte("","",Roles.Administrateur);
	// 	System.out.println(compte.getId_C());
	// 	System.out.println(compte.getId_C());
	// 	Compte compte1=new Compte("","",Roles.Administrateur);
	// 	System.out.println(compte1.getId_C());
	// }
}
