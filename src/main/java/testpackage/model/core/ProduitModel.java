
package testpackage.model.core;

import testpackage.model.core.BonReception;
import testpackage.model.errors.MinimumException;
import testpackage.model.enumeration.Categorie;

import java.util.ArrayList;

public class ProduitModel {
	private String id_Model ;
	private boolean type=false ;
	private String designation ;
	private Categorie categorie;
	private String bonReception ;

	public ProduitModel(){}
	public ProduitModel( boolean type, String designation, Categorie categorie, String bonReception) throws NullPointerException {
		this.type = type;
		this.designation = designation;
		this.categorie = categorie;
		this.bonReception = bonReception;
	}

	public String getId_modele() {
		return id_Model;
	}

	public void setId_modele(String id_Model) {
		this.id_Model = id_Model;
	}

	public boolean isType_produit() {
		return type;
	}

	public void setType_produit(boolean type) {
		this.type = type;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public Categorie getCategorie() {
		return categorie;
	}

	public void setCategorie(Categorie categorie) {
		this.categorie = categorie;
	}

	public String getId_bon() {
		return bonReception;
	}

	public void setId_bon(String bonReception) {
		this.bonReception = bonReception;
	}
}
