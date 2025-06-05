// TODO: still under developement is not finished
package testpackage.model.utils;

import testpackage.model.enumeration.TypeSalle;
import testpackage.model.enumeration.Services;
import testpackage.model.enumeration.Roles;
import testpackage.model.enumeration.Categorie;

public class ConvertEnum {

	// TypeSalle convert
	public static String convertTypeSalleToString(TypeSalle type){
		switch(type){
			case REUNION : return "REUNION";
			case RECHERCHE : return "RECHERCHE";
			case TECHNIQUE : return "TECHNIQUE";
			case BUREAUX : return "BUREAUX";
			case SUPPORT : return "SUPPORT";
			default : return "UNREACHABLE";
		}
	}

	public static TypeSalle convertStringToTypeSalle(String type){
		switch(type){
			case "REUNION" : return TypeSalle.REUNION;
			case "RECHERCHE" : return TypeSalle.RECHERCHE;
			case "TECHNIQUE": return TypeSalle.TECHNIQUE;
			case "BUREAUX": return TypeSalle.BUREAUX;
			case "SUPPORT": return TypeSalle.SUPPORT;
			default : return null;
		}
	}
	// Services
	public static String convertServicesToString(Services type){
		switch(type){
			case VICE_DOYENS : return "VICE DOYENS"; 
			case SECRETARIAT : return "SECRETARIAT"; 
			case CHEF_DE_DEPARTEMENT : return "CHEF DE DEPARTEMENT"; 
			case BIBLIOTHEQUE : return "BIBLIOTHEQUE";
			default : return "UNREACHABLE";
		}
	}

	public static Services convertStringToServices(String type){
		switch(type){
			case "VICE DOYENS" : return Services.VICE_DOYENS; 
			case "SECRETARIAT" : return Services.SECRETARIAT; 
			case "CHEF DE DEPARTEMENT" : return Services.CHEF_DE_DEPARTEMENT; 
			case "BIBLIOTHEQUE" : return Services.BIBLIOTHEQUE;
			default : return null;
		}
	}
	// roles
	public static String convertRolesToString(Roles type){
		switch(type){
			case ADMINISTRATEUR : 	return "ADMINISTRATEUR";
			case SECRETAIRE : 	return "SECRETAIRE";
			case MAGASINIER : 	return "MAGASINIER";
			case CLIENT : 		return "CLIENT";
			default : return "UNREACHABLE";
		}
	}

	public static Roles convertStringToRoles(String type){
		switch(type){
			case "ADMINISTRATEUR" : return Roles.ADMINISTRATEUR;
			case "SECRETAIRE" : return Roles.SECRETAIRE;
			case "MAGASINIER" : return Roles.MAGASINIER;
			case "CLIENT" : return Roles.CLIENT;
			default : return null;
		}
	}
	// categorie
	public static String convertCategorieToString(Categorie type){
		switch(type){
			case INFORMATIQUE : return "INFORMATIQUE";
			case BUREAUTIQUE : return "BUREAUTIQUE";
			case RESEAU : return "RESEAU";
			case TELECOMMUNICATION : return "TELECOMMUNICATION";
			case STOCKAGE : return "STOCKAGE";
			case AUTRE : return "AUTRE";
			default : return "UNREACHABLE";
		}
	}

	public static Categorie convertStringToCategorie(String type){
		switch(type){
			case "INFORMATIQUE" : return Categorie.INFORMATIQUE;
			case "BUREAUTIQUE" : return Categorie.BUREAUTIQUE;
			case "RESEAU" : return Categorie.RESEAU;
			case "TELECOMMUNICATION" : return Categorie.TELECOMMUNICATION;
			case "STOCKAGE" : return Categorie.STOCKAGE;
			case "AUTRE" : return Categorie.AUTRE;
			default : return null;
		}
	}
	
	
}
