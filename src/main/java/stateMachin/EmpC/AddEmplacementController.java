package stateMachin.EmpC;

import db.configuration.ConfigDatabase;
import db.java.EmplacementDatabase;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import testpackage.model.core.Emplacement;

public class AddEmplacementController {

    @FXML private TextField idField;
    @FXML private TextField typeSalleField;
    @FXML private TextField superficieField;
    @FXML private TextField bureauField;
    @FXML private ComboBox<String> nomServiceCombo;

    private boolean editMode = false;
    private Emplacement original;

    public void setEditMode(Emplacement emplacement) {
        this.editMode = true;
        this.original = emplacement;

        idField.setText(emplacement.getId_emplacement());
        idField.setDisable(true);
        typeSalleField.setText(emplacement.getType_salle());
        superficieField.setText(String.valueOf(emplacement.getSuperficie()));
        bureauField.setText(String.valueOf(emplacement.getBureau()));
        nomServiceCombo.setValue(emplacement.getNom_service());
    }

    @FXML
    private void onValidate() {
        String id = idField.getText().trim();
        String type = typeSalleField.getText();
        String supText = superficieField.getText();
        String bureauText = bureauField.getText();
        String nomService = nomServiceCombo.getValue();

        if (id.isEmpty() || type.isEmpty() || supText.isEmpty() || bureauText.isEmpty() || nomService == null) {
            showAlert("Veuillez remplir tous les champs.");
            return;
        }

        try {
            double superficie = Double.parseDouble(supText);
            int bureau = Integer.parseInt(bureauText);

            EmplacementDatabase db = new EmplacementDatabase(new ConfigDatabase(), null, null);

            if (editMode && original != null) {
                Emplacement updated = new Emplacement(id, type, superficie, bureau, nomService);
                db.update(original, updated);
            } else {
                Emplacement nouveau = new Emplacement(id, type, superficie, bureau, nomService);
                db.add(nouveau);
            }

            close();
        } catch (NumberFormatException e) {
            showAlert("Format invalide pour superficie ou bureau.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur lors de l'ajout : " + e.getMessage());
        }
    }

    @FXML
    private void onCancel() {
        close();
    }

    private void close() {
        Stage stage = (Stage) typeSalleField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(message);
        alert.showAndWait();
    }
}
