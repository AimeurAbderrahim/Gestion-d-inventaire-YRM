package testpackage.model.core;

import java.time.LocalDateTime;

public class Bon {
	private String id_bon;
	private LocalDateTime dateBon;
	private boolean type;
	private boolean valid;
	private int quantite;
	private String referenceId;

	private String id_f;
	private String id_emplacement;

	public Bon() {
		this.type = false;
		this.valid = false;
		this.quantite = 0;
	}

	public Bon(String id_bon, LocalDateTime dateBon, boolean type, boolean valid, String id) {
		this.id_bon = id_bon;
		this.dateBon = dateBon;
		if (type == false) {
			this.id_emplacement = id;
			this.id_f = null;
		} else {
			this.id_f = id;
			this.id_emplacement = null;
		}
		this.valid = valid;
		this.quantite = 0;
	}

	public void setValid(boolean value) {
		this.valid = value;
	}

	public boolean isValid() {
		return this.valid;
	}

	public void setId_f(String id) {
		this.id_f = id;
	}

	public void setId_emplacement(String id) {
		this.id_emplacement = id;
	}

	public String getId_emplacement() {
		return this.id_emplacement;
	}

	public String getId_f() {
		return this.id_f;
	}

	public void setIdBon(String id) {
		this.id_bon = id;
	}

	public String getIdBon() {
		return this.id_bon;
	}

	public void setDateBon(LocalDateTime date) {
		this.dateBon = date;
	}

	public LocalDateTime getDateBon() {
		return this.dateBon;
	}

	public void setType(boolean type) {
		this.type = type;
	}

	public boolean isBonReception() {
		return this.type;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public String getReferenceId() {
		if (referenceId != null) {
			return referenceId;
		}
		// Fallback to old behavior
		if (this.type) {
			return String.format("02%s", this.id_f);
		}
		return this.id_emplacement;
	}

	public void setQuantite(int quantite) {
		this.quantite = quantite;
	}

	public int getQuantite() {
		return this.quantite;
	}

	@Override
	public String toString() {
		return "Bon{" +
				"id_bon='" + id_bon + '\'' +
				", dateBon=" + dateBon +
				", type=" + type +
				", valid=" + valid +
				", id_f='" + id_f + '\'' +
				", id_emplacement='" + id_emplacement + '\'' +
				'}';
	}
}