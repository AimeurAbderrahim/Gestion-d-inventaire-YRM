package stateMachin;

import db.java.ProduitModeleDatabase;
import db.java.BonDatabase;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import testpackage.model.core.ProduitModel;
import testpackage.model.core.Bon;
import testpackage.model.enumeration.Categorie;

import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

public class AddProductTypeController {
    private static final Logger LOGGER = Logger.getLogger(AddProductTypeController.class.getName());

    @FXML private TextField designationField;
    @FXML private ComboBox<Categorie> categoryComboBox;
    @FXML private CheckBox consumableCheckBox;
    @FXML private ComboBox<String> bonReceptionComboBox;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    private ProduitModeleDatabase productDB;
    private BonDatabase bonDB;
    private ProduitModel productToEdit;
    private boolean isEditMode = false;

    @FXML
    public void initialize() {
        try {
            LOGGER.info("Initializing AddProductTypeController");
            
            // Initialize databases
            productDB = new ProduitModeleDatabase(null, null);
            bonDB = new BonDatabase(null, null);
            
            // Initialize category combo box
            categoryComboBox.getItems().addAll(Categorie.values());
            
            // Initialize bon reception combo box
            loadBonReceptions();
            
            // Set up button handlers
            saveButton.setOnAction(event -> handleSave());
            cancelButton.setOnAction(event -> handleCancel());
            
            LOGGER.info("AddProductTypeController initialized successfully");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error initializing AddProductTypeController", e);
            showError("Error", "Failed to initialize the form: " + e.getMessage());
        }
    }

    private void loadBonReceptions() {
        try {
            List<Bon> bons = bonDB.findAll();
            bonReceptionComboBox.setItems(FXCollections.observableArrayList(
                bons.stream()
                    .map(Bon::getIdBon)
                    .toList()
            ));
            LOGGER.info("Loaded " + bons.size() + " bon receptions");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading bon receptions", e);
            showError("Error", "Failed to load bon receptions: " + e.getMessage());
        }
    }

    public void setProductForEdit(ProduitModel product) {
        try {
            LOGGER.info("Setting up form for editing product type: " + product.getId_modele());
            this.productToEdit = product;
            this.isEditMode = true;
            
            // Populate fields with existing data
            designationField.setText(product.getDesignation());
            categoryComboBox.setValue(product.getCategorie());
            consumableCheckBox.setSelected(product.isType_produit());
            bonReceptionComboBox.setValue(product.getId_bon());
            
            LOGGER.info("Form populated with product data successfully");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error setting up edit form", e);
            showError("Error", "Failed to load product data: " + e.getMessage());
        }
    }

    @FXML
    private void handleSave() {
        try {
            if (!validateForm()) {
                return;
            }

            ProduitModel product = isEditMode ? productToEdit : new ProduitModel();
            product.setDesignation(designationField.getText().trim());
            product.setCategorie(categoryComboBox.getValue());
            product.setType_produit(consumableCheckBox.isSelected());
            product.setId_bon(bonReceptionComboBox.getValue());

            if (isEditMode) {
                LOGGER.info("Updating product type: " + product.getId_modele());
                productDB.update(productToEdit, product);
                LOGGER.info("Product type updated successfully");
            } else {
                LOGGER.info("Creating new product type");
                product.setId_modele(productDB.generatedIdPK());
                productDB.add(product);
                LOGGER.info("New product type created successfully");
            }

            closeWindow();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error saving product type", e);
            showError("Error", "Failed to save product type: " + e.getMessage());
        }
    }

    private boolean validateForm() {
        StringBuilder errors = new StringBuilder();

        if (designationField.getText().trim().isEmpty()) {
            errors.append("- La désignation est requise\n");
        }

        if (categoryComboBox.getValue() == null) {
            errors.append("- La catégorie est requise\n");
        }

        if (bonReceptionComboBox.getValue() == null) {
            errors.append("- Le bon de réception est requis\n");
        }

        if (errors.length() > 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation");
            alert.setHeaderText("Veuillez corriger les erreurs suivantes:");
            alert.setContentText(errors.toString());
            alert.showAndWait();
            return false;
        }

        return true;
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 