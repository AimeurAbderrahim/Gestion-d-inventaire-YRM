package testpackage.model.core;

public class Inventorier {
    private String id_modele;
    private String id_bon;
    private int quantite;

    public Inventorier(){}
    public Inventorier(String id_bon , String id_modele , int quantite){
        this.id_bon = id_bon;
        this.id_modele = id_modele;
        this.quantite = quantite;
    }

    public String getId_modele() {
        return id_modele;
    }

    public void setId_modele(String id_modele) {
        this.id_modele = id_modele;
    }

    public String getId_bon() {
        return id_bon;
    }

    public void setId_bon(String id_bon) {
        this.id_bon = id_bon;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }
}
