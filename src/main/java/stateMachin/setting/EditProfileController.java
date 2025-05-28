package stateMachin.setting;

import db.configuration.ConfigDatabase;
import db.errors.OperationFailedException;
import db.java.CompteDatabase;
import db.java.PersonneDatabase;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import stateMachin.Session;
import testpackage.model.core.Compte;
import testpackage.model.core.Personne;
import java.util.List;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class EditProfileController {
	@FXML private TextField usernameField;
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
				.filter(p -> p.getId_c().equals(currentUser.getId_c()))
				.findFirst()
				.orElse(null);
		} catch (Exception e) {
			showAlert("Error loading personal data");
		}
	}

	private void populateFields() {
		if (currentUser != null) {
			usernameField.setText(currentUser.getNom_utilisateur());
		}
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

			// Update Personne data
			currentPersonne.setNom(nomField.getText());
			currentPersonne.setPrenom(prenomField.getText());
			currentPersonne.setEmail(emailField.getText());
			currentPersonne.setAdresse(adresseField.getText());
			currentPersonne.setNumero_tlph(telephoneField.getText());

			// Handle date parsing
			try {
				LocalDate birthDate = LocalDate.parse(dateNaissanceField.getText());
				currentPersonne.setDate_naissance(birthDate);
			} catch (DateTimeParseException e) {
				showAlert("Invalid date format. Use YYYY-MM-DD");
				return;
			}

			// Update database
			ConfigDatabase db = null;
			try {
				db = new ConfigDatabase();

				String newUsername = usernameField.getText().trim();
				if (!newUsername.equals(currentUser.getNom_utilisateur())) {
					CompteDatabase compteDB = new CompteDatabase(db, null, null);
					List<Compte> comptes = null;
					try{

						comptes = compteDB.search(newUsername);
						if(comptes == null || comptes.isEmpty()){
							Compte updatedCompte = new Compte();
							updatedCompte.setId_c(currentUser.getId_c());
							updatedCompte.setNom_utilisateur(newUsername);
							updatedCompte.setMot_de_passe(currentUser.getMot_de_passe());
							updatedCompte.setRoles(currentUser.getRoles());
							compteDB.update(currentUser, updatedCompte);
							currentUser = updatedCompte;
							Session.login(currentUser); // Update session
						}else{
							showAlert("username already exists ...");
						}
					}catch(OperationFailedException error){
						showAlert("cannot find the username ...");
					}
				}


				PersonneDatabase personneDB = new PersonneDatabase(db, null, null);
				personneDB.update(currentPersonne, currentPersonne);
				showAlert("Profile updated successfully!");
				close();
			} catch (Exception e) {
				showAlert("Error updating profile: " + e.getMessage());
			}

		} catch (Exception e) {
			showAlert("Error updating profile: " + e.getMessage());
		}
	}

	@FXML
	private void close() {
		((Stage) usernameField.getScene().getWindow()).close();
	}

	private void showAlert(String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setHeaderText(message);
		alert.showAndWait();
	}
}
