package testpackage.model.core;

public class Emplacement {
	private String id_emplacement ;
	private TypeSalle typeSalle ;
	private double superficie ;
	private int bureau ;
	private Services service ;

	public Emplacement(String id_emplacement, TypeSalle typeSalle, double superficie, int bureau, Services service) throws NullPointerException {
		this.id_emplacement = id_emplacement;
		this.typeSalle = typeSalle;
		this.superficie = superficie;
		this.bureau = bureau;
		this.service = service;
	}

	public String getId_emplacement() {
		return id_emplacement;
	}

	public void setId_emplacement(String id_emplacement) {
		this.id_emplacement = id_emplacement;
	}

	public TypeSalle getTypeSalle() {
		return typeSalle;
	}

	public void setTypeSalle(TypeSalle typeSalle) {
		this.typeSalle = typeSalle;
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

	public Services getService() {
		return service;
	}

	public void setService(Services service) {
		this.service = service;
	}

}
