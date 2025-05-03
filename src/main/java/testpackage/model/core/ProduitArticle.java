package testpackage.model.core;

import Bons.BonSortie;
import Exceptions.MinimumException;
import Exceptions.NotNullException;
import Operations.ImplementId;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ProduitArticle implements ImplementId {
	private String id_article ;
	private LocalDate date_peremption ;
	private LocalDateTime date_achat ;
	static int comptageA=0 ;

	public ProduitArticle(String id_article, LocalDate date_peremption, LocalDate date_achat, int quantite, ProduitModel produitModel, BonSortie bonSortie) throws NotNullException, MinimumException {
		if (date_achat==null || id_article==null){
			throw new NotNullException("Manque information Article");
		}
		this.id_article = id_article;
		this.date_peremption = date_peremption;
		this.date_achat = date_achat.atStartOfDay();
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

	public LocalDateTime getDate_achat() {
		return date_achat;
	}

	public void setDate_achat(LocalDateTime date_achat) {
		this.date_achat = date_achat;
	}

	@Override
	public String implementsId() {
		comptageA++ ;
		return String.format("%04d",comptageA);
	}
}
