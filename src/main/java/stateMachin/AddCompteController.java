package stateMachin;

import db.configuration.ConfigDatabase;
import db.java.CompteDatabase;
import db.java.EmplacementDatabase;
import db.java.PersonneDatabase;
import db.utils.CreateAccount;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import testpackage.model.core.Compte;
import testpackage.model.core.Emplacement;
import testpackage.model.core.Personne;
import testpackage.model.enumeration.Roles;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class AddCompteController {

	@FXML private TextField nomDutilisateurField;
	@FXML private TextField motDePasseField;
	@FXML private ComboBox<String> roleComboBox;
	@FXML private Button saveButton;
	@FXML private TextField nomField;
	@FXML private TextField prenomField;
	@FXML private TextField dateNaissanceField;
	@FXML private TextField emailField;
	@FXML private TextField adresseField;
	@FXML private TextField numeroTelField;
	@FXML private TextField emplacementCodeField;

	private boolean isEditMode = false;
	private Compte originalCompte;

	public void setEditMode(Compte compte) {
		this.originalCompte = compte;
		this.isEditMode = true;

		nomDutilisateurField.setText(compte.getNom_utilisateur());
		motDePasseField.setText(compte.getMot_de_passe());
		roleComboBox.setValue(compte.getRoles().toString());
	}

	@FXML
	public void saveAccount() {
		String nomUtilisateur = nomDutilisateurField.getText();
		String mdp = motDePasseField.getText();
		String selectedRole = roleComboBox.getValue();

		// get text field for Person
		String nom = nomField.getText();
		String prenom = prenomField.getText();
		String dateNaissance = dateNaissanceField.getText();
		String email = emailField.getText();
		String adresse = adresseField.getText();
		String telephone = numeroTelField.getText();
		String emplacementCode = emplacementCodeField.getText();

		// always check if the user entred empty fields , that's an error !!!
		if (nomUtilisateur.isEmpty() || mdp.isEmpty() || selectedRole == null ||
				nom.isEmpty() || prenom.isEmpty() || dateNaissance.isEmpty() ||
				email.isEmpty() || emplacementCode.isEmpty()) {
			// transulate it to french :,))
			showAlert("Tous les champs obligatoires doivent être remplis");
			return;
				}

		// if the user entred wrong date format
		LocalDate naissanceDate;
		try {
			naissanceDate = LocalDate.parse(dateNaissance);
		} catch (DateTimeParseException e) {
			showAlert("Format de date invalide. Utilisez AAAA-MM-JJ");
			return;
		}

		ConfigDatabase db = null;
		try {
			db = new ConfigDatabase();
			db.getConnection();
			EmplacementDatabase emplacementDB = new EmplacementDatabase(db, null, null);
			// Chek if the emplacement exists in database 
			// database must have all emplaement and thier informations 
			// if not that means the user entred a wrong emplacement or he want to break system logic
			Emplacement emplacement = emplacementDB.findById(emplacementCode);

			if (emplacement == null) {
				showAlert("Code emplacement introuvable");
				return;
			}
			Roles role = Roles.valueOf(selectedRole);
			CompteDatabase compteDB = new CompteDatabase(db, null, null);

			// edit popup
			if (isEditMode && originalCompte != null) {
				Compte updated = new Compte(nomUtilisateur, mdp, role);
				compteDB.update(originalCompte, updated);
			} else {
				// Check if the user already exists or not
				if (userAlreadyExists(nomUtilisateur, compteDB)) {
					showAlert("Nom d'utilisateur déjà utilisé");
					return;
				}

				// create new user
				CreateAccount account = new CreateAccount(nomUtilisateur, mdp, role);
				account.SignUp();

				// Create the person
				Personne personne = new Personne();
				personne.setNom(nom);
				personne.setPrenom(prenom);
				personne.setLocalDate(naissanceDate);
				personne.setEmail(email);
				personne.setAdresse(adresse);
				personne.setNumero_tel_personne(telephone);
				personne.setId_emplacement(emplacementCode);

				// that might be dangerouse but i find this way to get id
				// TODO: replace with a safe method to get id of the current user
				Compte newCompte = compteDB.findAll().stream()
					.filter(c -> c.getNom_utilisateur().equals(nomUtilisateur))
					.findFirst()
					.orElseThrow(() -> new RuntimeException("Compte non trouvé après création"));

				// add id Account to the person 
				personne.setId_c(newCompte.getId_c());

				// add person to the database
				PersonneDatabase personneDB = new PersonneDatabase(db, null, null);
				personneDB.add(personne);
			}

			close();
		} catch (Exception e) {
			showAlert("Erreur: " + e.getMessage());
			e.printStackTrace();
		}
		if(db != null) {
			try{
				db.closeConnection();
			}catch(Exception err){
				throw new RuntimeException("Error close connection");
			}
		}
	}

	@FXML
	public void close() {
		((Stage) saveButton.getScene().getWindow()).close();
	}

	private void showAlert(String msg) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setHeaderText(msg);
		alert.showAndWait();
	}

	// find other safe way to check if the user already exists or not
	private boolean userAlreadyExists(String username, CompteDatabase db) throws Exception {
		return db.findAll().stream()
			.anyMatch(c -> c.getNom_utilisateur().equalsIgnoreCase(username));
	}

	@FXML
	public void deleteAccount() {
		if (!isEditMode || originalCompte == null) {
			showAlert("Aucun compte sélectionné pour suppression.");
			return;
		}

		Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
		confirmation.setTitle("Confirmation");
		confirmation.setHeaderText("Êtes-vous sûr de vouloir supprimer ce compte ?");
		confirmation.setContentText("Cette action est irréversible.");

		confirmation.showAndWait().ifPresent(response -> {
			if (response == ButtonType.OK) {
				try {
					ConfigDatabase db = new ConfigDatabase();
					CompteDatabase compteDB = new CompteDatabase(db, null, null);

					System.out.println("RAYDEN");
					compteDB.remove(originalCompte);
					db.closeConnection();
					close();

				} catch (Exception e) {
					showAlert("Erreur lors de la suppression : " + e.getMessage());
					e.printStackTrace();
				}
			}
		});
	}
}
