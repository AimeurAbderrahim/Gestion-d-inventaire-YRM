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
			default : return "unreachable";
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
			case RECHERCHES : return "RECHERCHES ";
			default : return "unreachable";
		}
	}

	public static Services convertStringToServices(String type){
		switch(type){
			case "RECHERCHES" : return Services.RECHERCHES;
			default : return null;
		}
	}
}
