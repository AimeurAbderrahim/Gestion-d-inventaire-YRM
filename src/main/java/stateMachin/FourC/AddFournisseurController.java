package stateMachin.FourC;

import db.configuration.ConfigDatabase;
import db.java.FournisseurDatabase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import testpackage.model.core.Fournisseur;

public class AddFournisseurController {

    @FXML private TextField nomField;
    @FXML private TextField adresseField;
    @FXML private TextField numeroField;
    @FXML private TextField emailField;
    @FXML private TextField nifField;
    @FXML private TextField nisField;
    @FXML private TextField rcField;
    @FXML private Button closePopup;

    @FXML
    private void AjouterFournisseurDbAction(ActionEvent event) {
        try {
            Fournisseur fournisseur = new Fournisseur(
                    nomField.getText(),
                    adresseField.getText(),
                    numeroField.getText(),
                    emailField.getText(),
                    nifField.getText(),
                    nisField.getText(),
                    rcField.getText()
            );

            ConfigDatabase db = new ConfigDatabase();
            db.getConnection();

            FournisseurDatabase fournisseurDB = new FournisseurDatabase(db, null, null);
            fournisseurDB.add(fournisseur);

            db.closeConnection();
            close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void close() {
        ((Stage) closePopup.getScene().getWindow()).close();
    }
}
