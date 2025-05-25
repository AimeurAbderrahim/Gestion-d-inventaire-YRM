package stateMachin;

import db.configuration.ConfigDatabase;
import db.java.CompteDatabase;
import db.utils.CreateAccount;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import testpackage.model.core.Compte;
import testpackage.model.enumeration.Roles;

public class AddCompteController {

    @FXML private TextField nomDutilisateurField;
    @FXML private TextField motDePasseField;
    @FXML private ComboBox<String> roleComboBox;
    @FXML private Button saveButton;

    private boolean isEditMode = false;
    private Compte originalCompte;

    public void setEditMode(Compte compte) {
        this.originalCompte = compte;
        this.isEditMode = true;

        nomDutilisateurField.setText(compte.getNom_utilisateur());
        motDePasseField.setText(compte.getMot_de_passe());
        roleComboBox.setValue(compte.getRoles().toString()); // assuming enum names match comboBox values
    }

    @FXML
    public void saveAccount() {
        String nom = nomDutilisateurField.getText();
        String mdp = motDePasseField.getText();
        String selectedRole = roleComboBox.getValue();

        if (nom.isEmpty() || mdp.isEmpty() || selectedRole == null) {
            showAlert("Tous les champs sont requis.");
            return;
        }

        Roles role;
        try {
            role = Roles.valueOf(selectedRole);
        } catch (IllegalArgumentException e) {
            showAlert("Rôle invalide.");
            return;
        }

        try {
            ConfigDatabase db = new ConfigDatabase();
            db.getConnection();
            CompteDatabase compteDB = new CompteDatabase(db, null, null);

            if (isEditMode && originalCompte != null) {
                Compte updated = new Compte(nom, mdp, role);
                compteDB.update(originalCompte, updated);
            } else {
                if (userAlreadyExists(nom, compteDB)) {
                    showAlert("Un utilisateur avec ce nom existe déjà.");
                    return;
                }
                CreateAccount account = new CreateAccount(nom, mdp, role);
                account.SignUp();
            }

            db.closeConnection();
            close();

        } catch (Exception e) {
            showAlert("Erreur: " + e.getMessage());
            e.printStackTrace();
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
                    db.getConnection();
                    CompteDatabase compteDB = new CompteDatabase(db, null, null);

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
