package stateMachin.setting;

import db.configuration.ConfigDatabase;
import db.java.PersonneDatabase;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import testpackage.model.core.Compte;
import testpackage.model.core.Personne;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class ajouterPersonneControler {

	@FXML private TextField nomField;
	@FXML private TextField prenomField;
	@FXML private TextField emailField;
	@FXML private TextField dateNaissanceField;
	@FXML private TextField adresseField;
	@FXML private TextField telephoneField;

	private Compte currentUser;
	private Personne currentPersonne;

	public void setCurrentUser(Compte user) {
		this.currentUser = user;
		loadPersonneData();
		populateFields();
	}

	private void loadPersonneData() {
		try {
			ConfigDatabase db = new ConfigDatabase();
			PersonneDatabase personneDB = new PersonneDatabase(db, null, null);
			currentPersonne = personneDB.findAll().stream()
					.filter(p -> currentUser.getId_c() != null && currentUser.getId_c().equals(p.getId_c()))
					.findFirst()
					.orElse(null);

		} catch (Exception e) {
			showAlert("Error loading personal data");
		}
	}

	private void populateFields() {
		if (currentPersonne != null) {
			nomField.setText(currentPersonne.getNom());
			prenomField.setText(currentPersonne.getPrenom());
			emailField.setText(currentPersonne.getEmail());
			dateNaissanceField.setText(currentPersonne.getDate_naissance().toString());
			adresseField.setText(currentPersonne.getAdresse());
			telephoneField.setText(currentPersonne.getNumero_tlph());
		}
	}

	@FXML
	private void saveChanges() {
		try {
			// Validate required fields
			if (nomField.getText().isEmpty() || prenomField.getText().isEmpty()) {
				showAlert("First and last name are required");
				return;
			}
			Personne newp = new Personne();
			// Update Personne data
			newp.setNom(nomField.getText());
			newp.setPrenom(prenomField.getText());
			newp.setEmail(emailField.getText());
			newp.setAdresse(adresseField.getText());
			newp.setNumero_tlph(telephoneField.getText());

			// Handle date parsing
			try {
				LocalDate birthDate = LocalDate.parse(dateNaissanceField.getText());
				newp.setDate_naissance(birthDate);
			} catch (DateTimeParseException e) {
				showAlert("Invalid date format. Use YYYY-MM-DD");
				return;
			}

			// Update database
			ConfigDatabase db = null;
			try {
				db = new ConfigDatabase();
				PersonneDatabase personneDB = new PersonneDatabase(db, null, null);
				if(newp != null) {
					personneDB.add(newp);
					showAlert("Profile created successfully!");
				}else{
					showAlert("cannot create person because currentPerson is null !");
				}
				close();
			} catch (Exception e) {
				showAlert("Error creating profile: " + e.getMessage());
			}

		} catch (Exception e) {
			showAlert("Error updating profile: " + e.getMessage());
		}
	}

	@FXML
	private void close() {
		((Stage) nomField.getScene().getWindow()).close();
	}

	private void showAlert(String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setHeaderText(message);
		alert.showAndWait();
	}
}
