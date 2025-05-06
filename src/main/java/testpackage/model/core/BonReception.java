package testpackage.model.core;

import Fournisseurs.Fournisseur;
import Operations.ImplementId;
import Produit.ProduitModel;

import java.time.LocalDateTime;

public class BonReception implements ImplementId {
	private String id_bonR ;
	private LocalDateTime dateReception;
	private boolean etat ;

	public BonReception() {
		this.id_bonR = implementsId();
	}
	public String getId_bonR() {
		return id_bonR;
	}

	public void setId_bonR(String id_bonR) {
		this.id_bonR = id_bonR;
	}

	public LocalDateTime getDateReception() {
		return dateReception;
	}

	public void setDateReception(LocalDateTime dateReception) {
		this.dateReception = dateReception;
	}

	public boolean isEtat() {
		return etat;
	}

	public void setEtat(boolean etat) {
		this.etat = etat;
		if (etat){
			this.dateReception=LocalDateTime.now();
		}
	}

	@Override
	public String implementsId() {
		return "R";
	}
}
