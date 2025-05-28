package stateMachin;

import db.configuration.ConfigDatabase;
import db.java.ProduitArticleDatabase;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import testpackage.model.core.ProduitArticle;
import testpackage.model.core.ProduitModel;

import java.time.LocalDate;

public class AddProductController {
    @FXML private TextField nomField;
    @FXML private TextField quantiteField;
    @FXML private DatePicker datePeremptionPicker;
    @FXML private DatePicker dateAchatPicker;
    @FXML private TextField modeleField;

    private ProduitModel productModel;
    private ProduitArticle productToEdit;
    private boolean isEditMode = false;

    public void setModelType(ProduitModel model) {
        this.productModel = model;
        modeleField.setText(model.getId_modele());
        modeleField.setEditable(false);
    }

    public void setArticleForEdit(ProduitArticle article) {
        this.productToEdit = article;
        this.isEditMode = true;
        
        nomField.setText(article.getNom_article());
        quantiteField.setText(String.valueOf(article.getQuantite_global()));
        datePeremptionPicker.setValue(article.getDate_peremption());
        dateAchatPicker.setValue(article.getDate_dachat());
        modeleField.setText(article.getId_modele());
        modeleField.setEditable(false);
    }

    @FXML
    private void handleAdd() {
        try {
            ProduitArticle product = isEditMode ? productToEdit : new ProduitArticle();
            
            ConfigDatabase db = new ConfigDatabase();
            db.getConnection();
            ProduitArticleDatabase productDB = new ProduitArticleDatabase(db, null, null);
            
            if (!isEditMode) {
                // Generate new ID for new products only
                String newId = productDB.generatedIdPK();
                product.setId_article(newId);
            }
            
            product.setNom_article(nomField.getText());
            product.setQuantite_global(Integer.parseInt(quantiteField.getText()));
            product.setDate_peremption(datePeremptionPicker.getValue());
            product.setDate_dachat(dateAchatPicker.getValue());
            product.setId_modele(modeleField.getText());

            if (isEditMode) {
                // Create a copy of the original product for update
                ProduitArticle originalProduct = new ProduitArticle();
                originalProduct.setId_article(productToEdit.getId_article());
                productDB.update(originalProduct, product);
            } else {
                productDB.add(product);
            }
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