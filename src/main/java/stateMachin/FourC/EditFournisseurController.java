package stateMachin.FourC;

import db.configuration.ConfigDatabase;
import db.java.FournisseurDatabase;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import testpackage.model.core.Fournisseur;

public class EditFournisseurController {

    @FXML private TextField nomField;
    @FXML private TextField adresseField;
    @FXML private TextField numeroField;
    @FXML private TextField emailField;
    @FXML private TextField nifField;
    @FXML private TextField nisField;
    @FXML private TextField rcField;

    private Fournisseur original;

    public void setFournisseur(Fournisseur f) {
        this.original = f;

        nomField.setText(f.getNom());
        adresseField.setText(f.getAdresse());
        numeroField.setText(f.getNumero_tlph());
        emailField.setText(f.getEmail());
        nifField.setText(f.getNIF());
        nisField.setText(f.getNIS());
        rcField.setText(f.getRC());
    }

    @FXML
    private void updateFournisseur() {
        try {
            Fournisseur updated = new Fournisseur();
            updated.setId_f(original.getId_f());
            updated.setNom(nomField.getText());
            updated.setAdresse(adresseField.getText());
            updated.setNumero_tlph(numeroField.getText());
            updated.setEmail(emailField.getText());
            updated.setNIF(nifField.getText());
            updated.setNIS(nisField.getText());
            updated.setRC(rcField.getText());

            ConfigDatabase db = new ConfigDatabase();
            db.getConnection();
            FournisseurDatabase dao = new FournisseurDatabase(db, null, null);
            dao.update(original, updated);
            db.closeConnection();
            close();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur lors de la modification.");
        }
    }

    @FXML
    private void deleteFournisseur() {
        try {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Supprimer ce fournisseur ?", ButtonType.YES, ButtonType.NO);
            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    try {
                        ConfigDatabase db = new ConfigDatabase();
                        db.getConnection();
                        FournisseurDatabase dao = new FournisseurDatabase(db, null, null);
                        dao.remove(original);
                        db.closeConnection();
                        close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        showAlert("Erreur lors de la suppression.");
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur inconnue.");
        }
    }

    private void close() {
        ((Stage) nomField.getScene().getWindow()).close();
    }
    @FXML
    private void cancel() {
        ((Stage) nomField.getScene().getWindow()).close();
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(msg);
        alert.showAndWait();
    }
}
