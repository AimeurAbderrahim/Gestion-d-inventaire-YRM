package stateMachin.EmpC;

import db.configuration.ConfigDatabase;
import db.java.EmplacementDatabase;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import testpackage.model.core.Emplacement;

public class EditEmplacementController {

    @FXML private TextField idField;
    @FXML private TextField typeSalleField;
    @FXML private TextField superficieField;
    @FXML private TextField bureauField;
    @FXML private ComboBox<String> nomServiceCombo;

    private Emplacement originalEmplacement;

    public void setEditMode(Emplacement emplacement) {
        this.originalEmplacement = emplacement;

        idField.setText(emplacement.getId_emplacement());
        idField.setDisable(true); // ID non modifiable
        typeSalleField.setText(emplacement.getType_salle());
        superficieField.setText(String.valueOf(emplacement.getSuperficie()));
        bureauField.setText(String.valueOf(emplacement.getBureau()));
        nomServiceCombo.setValue(emplacement.getNom_service());
    }

    @FXML
    private void onCancel() {
        ((Stage) idField.getScene().getWindow()).close();
    }

    @FXML
    private void onValidate() {
        String typeSalle = typeSalleField.getText();
        String superficieStr = superficieField.getText();
        String bureauStr = bureauField.getText();
        String nomService = nomServiceCombo.getValue();

        if (typeSalle.isEmpty() || superficieStr.isEmpty() || bureauStr.isEmpty() || nomService == null) {
            showAlert("Veuillez remplir tous les champs.");
            return;
        }

        try {
            double superficie = Double.parseDouble(superficieStr);
            int bureau = Integer.parseInt(bureauStr);

            Emplacement updated = new Emplacement(
                    originalEmplacement.getId_emplacement(),
                    typeSalle,
                    superficie,
                    bureau,
                    nomService
            );

            ConfigDatabase db = new ConfigDatabase();
            EmplacementDatabase emplacementDB = new EmplacementDatabase(db, null, null);
            emplacementDB.update(originalEmplacement, updated);
            db.closeConnection();

            close();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur lors de la modification : " + e.getMessage());
        }
    }

    @FXML
    private void onDelete() {
        try {
            ConfigDatabase db = new ConfigDatabase();
            EmplacementDatabase emplacementDB = new EmplacementDatabase(db, null, null);
            emplacementDB.remove(originalEmplacement);
            db.closeConnection();
            close();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur lors de la suppression : " + e.getMessage());
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(msg);
        alert.showAndWait();
    }

    private void close() {
        ((Stage) idField.getScene().getWindow()).close();
    }
}
