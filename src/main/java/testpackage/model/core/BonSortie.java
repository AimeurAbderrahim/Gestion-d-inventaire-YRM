package testpackage.model.core;

import java.time.LocalDateTime;

public class BonSortie {
	private String id_BonS ;
	private LocalDateTime date_sortie ;
	private boolean etat ;
	public BonSortie() {
		// this.id_BonS = implementsId();
	}

	public String getId_BonS() {
		return id_BonS;
	}

	public void setId_BonS(String id_BonS) {
		this.id_BonS = id_BonS;
	}

	public LocalDateTime getDate_sortie() {
		return date_sortie;
	}

	public void setDate_sortie(LocalDateTime date_sortie) {
		this.date_sortie = date_sortie;
	}

	public boolean isEtat() {
		return etat;
	}

	public void setEtat(boolean etat) {
		this.etat = etat;
		if (etat){
			this.date_sortie=LocalDateTime.now();
		}
	}

	// @Override
	// public String implementsId() {
	// 	return "S";
	// }
}
