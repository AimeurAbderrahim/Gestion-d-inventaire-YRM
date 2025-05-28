package testpackage.model.core;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Personne {

	private final StringProperty id_p = new SimpleStringProperty();
	private final StringProperty nom = new SimpleStringProperty();
	private final StringProperty prenom = new SimpleStringProperty();
	private final ObjectProperty<LocalDate> date_naissance = new SimpleObjectProperty<>();
	private final StringProperty email = new SimpleStringProperty();
	private final StringProperty adresse = new SimpleStringProperty();
	private final StringProperty numero_tlph = new SimpleStringProperty();
	private final BooleanProperty avoir_compte = new SimpleBooleanProperty();
	private final StringProperty id_emplacement = new SimpleStringProperty();
	private final StringProperty id_c = new SimpleStringProperty();

	public Personne() {}

	public Personne(String id_p, String nom, String prenom, String email, boolean avoirCompte) {
		setId_p(id_p);
		setNom(nom);
		setPrenom(prenom);
		setEmail(email);
		setAvoir_compte(avoirCompte);
	}

	// ---- JavaFX Properties (pour TableView) ----
	public StringProperty idProperty() { return id_p; }
	public StringProperty nomProperty() { return nom; }
	public StringProperty prenomProperty() { return prenom; }
	public StringProperty emailProperty() { return email; }
	public BooleanProperty avoirCompteProperty() { return avoir_compte; }

	// ---- Getters et Setters classiques (pour DAO) ----
	public String getId_p() { return id_p.get(); }
	public void setId_p(String id) { this.id_p.set(id); }

	public String getNom() { return nom.get(); }
	public void setNom(String n) { this.nom.set(n); }

	public String getPrenom() { return prenom.get(); }
	public void setPrenom(String p) { this.prenom.set(p); }

	public LocalDate getDate_naissance() { return date_naissance.get(); }
	public void setDate_naissance(LocalDate d) { this.date_naissance.set(d); }

	public String getEmail() { return email.get(); }
	public void setEmail(String e) { this.email.set(e); }

	public String getAdresse() { return adresse.get(); }
	public void setAdresse(String a) { this.adresse.set(a); }

	public String getNumero_tlph() { return numero_tlph.get(); }
	public void setNumero_tlph(String n) { this.numero_tlph.set(n); }

	public boolean isAvoir_compte() { return avoir_compte.get(); }
	public void setAvoir_compte(boolean b) { this.avoir_compte.set(b); }

	public String getId_emplacement() { return id_emplacement.get(); }
	public void setId_emplacement(String id) { this.id_emplacement.set(id); }

	public String getId_c() { return id_c.get(); }
	public void setId_c(String id) { this.id_c.set(id); }
}
