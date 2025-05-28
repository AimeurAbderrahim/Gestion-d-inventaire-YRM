package stateMachin;

import db.configuration.ConfigDatabase;
import db.java.ProduitArticleDatabase;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import testpackage.model.core.ProduitArticle;

import java.time.LocalDate;

public class EditProductController {
    @FXML private TextField idField;
    @FXML private TextField nomField;
    @FXML private TextField quantiteField;
    @FXML private DatePicker datePeremptionPicker;
    @FXML private DatePicker dateAchatPicker;
    @FXML private TextField modeleField;

    private ProduitArticle product;
    private Stage stage;

    public void setProduct(ProduitArticle product) {
        this.product = product;
        populateFields();
    }

    private void populateFields() {
        if (product != null) {
            idField.setText(product.getId_article());
            nomField.setText(product.getNom_article());
            quantiteField.setText(String.valueOf(product.getQuantite_global()));
            datePeremptionPicker.setValue(product.getDate_peremption());
            dateAchatPicker.setValue(product.getDate_dachat());
            modeleField.setText(product.getId_modele());
        }
    }

    @FXML
    private void handleSave() {
        try {
            ProduitArticle updatedProduct = new ProduitArticle();
            updatedProduct.setId_article(product.getId_article());
            updatedProduct.setNom_article(nomField.getText());
            updatedProduct.setQuantite_global(Integer.parseInt(quantiteField.getText()));
            updatedProduct.setDate_peremption(datePeremptionPicker.getValue());
            updatedProduct.setDate_dachat(dateAchatPicker.getValue());
            updatedProduct.setId_modele(modeleField.getText());

            ConfigDatabase db = new ConfigDatabase();
            db.getConnection();
            ProduitArticleDatabase productDB = new ProduitArticleDatabase(db, null, null);
            productDB.update(product, updatedProduct);
            db.closeConnection();

            closeWindow();
        } catch (Exception e) {
            e.printStackTrace();
            // Show error alert
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) nomField.getScene().getWindow();
        stage.close();
    }
} 