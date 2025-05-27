package testpackage.model.core;

import testpackage.model.core.BonSortie;
import testpackage.model.errors.MinimumException;
import testpackage.model.errors.NotNullException;

import java.time.LocalDate;

public class ProduitArticle {
	private String id_article ;
	private String nom;
	private int quantite_global ;
	private LocalDate date_peremption ;
	private LocalDate date_achat ;

	private String id_modele;

	public ProduitArticle(){}

	public ProduitArticle(String id_article, String nom , int quantite_global , LocalDate date_peremption, LocalDate date_achat , String id_modele) throws NotNullException, MinimumException {
		if (date_achat==null || id_article==null){
			throw new NotNullException("Manque information Article");
		}
		this.id_article = id_article;
		this.nom = nom;
		this.quantite_global = quantite_global;
		this.date_peremption = date_peremption;
		this.date_achat = date_achat;
	}

	public int getQuantite_global(){
		return this.quantite_global;
	}

	public void setQuantite_global(int q){
		this.quantite_global = q;
	}

	public String getNom_article(){
		return this.nom;
	}

	public void setNom_article(String nom){
		this.nom = nom;
	}

	public String getId_article() {
		return id_article;
	}

	public void setId_article(String id_article) {
		this.id_article = id_article;
	}

	public LocalDate getDate_peremption() {
		return date_peremption;
	}

	public void setDate_peremption(LocalDate date_peremption) {
		this.date_peremption = date_peremption;
	}

	public LocalDate getDate_dachat() {
		return date_achat;
	}

	public void setDate_dachat(LocalDate date_achat) {
		this.date_achat = date_achat;
	}

	public String getId_modele(){
		return this.id_modele;
	}

	public void setId_modele(String id){
		this.id_modele = id;
	}

	@Override
	public String toString() {
		return "ProduitArticle{" +
				"id_article='" + id_article + '\'' +
				", nom='" + nom + '\'' +
				", quantite_global=" + quantite_global +
				", date_peremption=" + date_peremption +
				", date_achat=" + date_achat +
				", id_modele='" + id_modele + '\'' +
				'}';
	}
}
