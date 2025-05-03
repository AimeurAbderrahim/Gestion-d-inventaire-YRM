package testpackage.model.core;

import Bons.BonReception;
import Exceptions.MinimumException;
import Operations.ImplementId;

import java.util.ArrayList;

public class ProduitModel implements ImplementId {
    private String id_Model ;
    private boolean type=false ;
    private String designation ;
    private Categorie categorie;
    BonReception bonReception ;

    static int comptageC=0 ;
    static int comptageI=0 ;


    public ProduitModel( boolean type, String designation, Produit.Categorie categorie, BonReception bonReception) throws NullPointerException {
        this.type = type;
        this.designation = designation;
        this.categorie = categorie;
        this.bonReception = bonReception;
        this.id_Model = implementsId();
    }

    public String getId_Model() {
        return id_Model;
    }

    public void setId_Model(String id_Model) {
        this.id_Model = id_Model;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
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

    public BonReception getBonReception() {
        return bonReception;
    }

    public void setBonReception(BonReception bonReception) {
        this.bonReception = bonReception;
    }

    //    @Override
//    public Object implementsId() {
//        if (this.categorie == Categorie.INFORMATIQUE){
//            return Categorie.INFORMATIQUE
//        } else if (this.categorie == Categorie.ACCESSOIRES) {
//
//        } else if (this.categorie == Categorie.MOBILIER) {
//
//        } else if (this.categorie == Categorie.MULTIMEDIA) {
//
//        } else if (this.categorie == Categorie.PEDAGOGIQUE) {
//
//        } else if (this.categorie == Categorie.RESEAU) {
//
//        }
//    }
    @Override
    public String implementsId() {
        if (type){
            comptageC++ ;
            return "C"+String.format("%03d",comptageC)+this.categorie.name().charAt(0)+bonReception.getId_bonR().substring(1,3);
        }
        else {
            comptageI++ ;
            return "I"+String.format("%03d",comptageI)+this.categorie.name().charAt(0)+bonReception.getId_bonR().substring(1,3);
        }
    }
}
