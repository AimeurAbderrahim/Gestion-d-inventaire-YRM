package testpackage.model.core;

public class Emplacement {
	private String id_emplacement;
	private String type_salle;
	private double superficie;
	private int bureau;
	private String nom_service;

	public Emplacement() {}

	public Emplacement(String id_emplacement, String type_salle, double superficie, int bureau, String nom_service) {
		this.id_emplacement = id_emplacement;
		this.type_salle = type_salle;
		this.superficie = superficie;
		this.bureau = bureau;
		this.nom_service = nom_service;
	}

	public String getId_emplacement() {
		return id_emplacement;
	}

	public void setId_emplacement(String id_emplacement) {
		this.id_emplacement = id_emplacement;
	}

	public String getType_salle() {
		return type_salle;
	}

	public void setType_salle(String type_salle) {
		this.type_salle = type_salle;
	}

	public double getSuperficie() {
		return superficie;
	}

	public void setSuperficie(double superficie) {
		this.superficie = superficie;
	}

	public int getBureau() {
		return bureau;
	}

	public void setBureau(int bureau) {
		this.bureau = bureau;
	}

	public String getNom_service() {
		return nom_service;
	}

	public void setNom_service(String nom_service) {
		this.nom_service = nom_service;
	}
}
