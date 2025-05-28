package stateMachin;

import db.configuration.ConfigDatabase;
import db.java.BonDatabase;
import db.java.FournisseurDatabase;
import db.java.ProduitArticleDatabase;
import db.java.ProduitModeleDatabase;
import db.java.InventorierDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import testpackage.model.core.Bon;
import testpackage.model.core.Fournisseur;
import testpackage.model.core.ProduitArticle;
import testpackage.model.core.ProduitModel;
import testpackage.model.core.Inventorier;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

public class BonPopUpController {

    @FXML private ComboBox<String> produitTypeCombo;
    @FXML private TextField quantite;
    @FXML private DatePicker dateField;
    @FXML private ComboBox<String> emplacementCombo;

    private Bon currentBon;
    private BoneController parentController;
    private final ObservableList<String> fournisseurNames = FXCollections.observableArrayList();
    private final ObservableList<String> produitTypes = FXCollections.observableArrayList();
    private Map<String, String> fournisseurNameToId = new HashMap<>();
    private Map<String, ProduitModel> produitTypeToModel = new HashMap<>();

    public void setParentController(BoneController controller) {
        this.parentController = controller;
    }

    @FXML
    public void initialize() {
        try {
            // Load fournisseurs
            ConfigDatabase db = new ConfigDatabase();
            FournisseurDatabase fournisseurDB = new FournisseurDatabase(db, null, null);
            List<Fournisseur> fournisseurs = fournisseurDB.findAll();

            // Map fournisseur names to IDs and populate combo box
            fournisseurs.forEach(f -> {
                String displayName = f.getNom() + " (" + f.getRC() + ")";
                fournisseurNames.add(displayName);
                fournisseurNameToId.put(displayName, f.getId_f());
            });
            emplacementCombo.setItems(fournisseurNames);

            // Load product types
            ProduitModeleDatabase produitDB = new ProduitModeleDatabase(db, null, null);
            List<ProduitModel> produitModels = produitDB.findAll();

            // Map product names and populate combo box
            produitModels.forEach(p -> {
                String displayName = p.getDesignation() + " (" + p.getCategorie() + ")";
                produitTypes.add(displayName);
                produitTypeToModel.put(displayName, p);
            });
            produitTypeCombo.setItems(produitTypes);

        } catch (Exception e) {
            showError("Erreur", "Impossible de charger les données: " + e.getMessage());
        }
    }

    public void setBon(Bon bon) {
        this.currentBon = bon;
        if (bon != null) {
            dateField.setValue(bon.getDateBon().toLocalDate());

            // Find and select the correct fournisseur
            fournisseurNames.stream()
                    .filter(name -> fournisseurNameToId.get(name).equals(bon.getId_f()))
                    .findFirst()
                    .ifPresent(name -> emplacementCombo.setValue(name));
        }
    }

    @FXML
    private void Confirm() {
        try {
            if (dateField.getValue() == null) {
                showError("Erreur", "Veuillez sélectionner une date");
                return;
            }
            if (emplacementCombo.getValue() == null) {
                showError("Erreur", "Veuillez sélectionner un fournisseur");
                return;
            }
            if (produitTypeCombo.getValue() == null) {
                showError("Erreur", "Veuillez sélectionner un type de produit");
                return;
            }
            if (quantite.getText().isEmpty()) {
                showError("Erreur", "Veuillez entrer une quantité");
                return;
            }

            ConfigDatabase db = new ConfigDatabase();

            // Get selected product model first
            ProduitModel selectedModel = produitTypeToModel.get(produitTypeCombo.getValue());

            // Create new bon
            Bon bon = new Bon();
            bon.setDateBon(LocalDateTime.from(dateField.getValue().atStartOfDay()));
            bon.setType(true); // réception
            bon.setValid(false);
            bon.setId_f(fournisseurNameToId.get(emplacementCombo.getValue()));

            // Set the quantity
            int quantiteValue = Integer.parseInt(quantite.getText());
            bon.setQuantite(quantiteValue);

            // Set the product model ID as reference
            bon.setReferenceId(selectedModel.getId_modele());

            // Generate bon ID
            BonDatabase bonDB = new BonDatabase(db, null, null);
            long idx = bonDB.countAll();
            String bonId = "R" + selectedModel.getId_modele() + String.format("03%d", idx);
            bon.setIdBon(bonId);

            // Add bon to database
            bonDB.add(bon);

            // Create Inventorier record
            InventorierDatabase inventorierDB = new InventorierDatabase(db, null, null);
            Inventorier inventorier = new Inventorier();
            inventorier.setId_bon(bonId);
            inventorier.setId_modele(selectedModel.getId_modele());
            inventorier.setQuantite(quantiteValue);
            inventorierDB.add(inventorier);

            // Refresh the parent controller's table
            if (parentController != null) {
                parentController.refreshTable();
            }

            showInfo("Succès", "Bon créé avec succès. Le stock sera mis à jour après validation.");
            close();
        } catch (NumberFormatException e) {
            showError("Erreur", "La quantité doit être un nombre valide");
        } catch (Exception e) {
            showError("Erreur", "Impossible d'enregistrer : " + e.getMessage());
            System.err.println("Impossible d'enregistrer : " + e.getMessage());
        }
    }

    @FXML
    private void onDelete() {
        if (currentBon != null) {
            try {
                ConfigDatabase db = new ConfigDatabase();

                // Delete Inventorier record first (due to foreign key constraint)
                InventorierDatabase inventorierDB = new InventorierDatabase(db, null, null);
                List<Inventorier> inventoryRecords = inventorierDB.findAll();
                Inventorier toDelete = inventoryRecords.stream()
                        .filter(i -> i.getId_bon().equals(currentBon.getIdBon()))
                        .findFirst()
                        .orElse(null);

                if (toDelete != null) {
                    inventorierDB.remove(toDelete);
                }

                // Then delete the bon
                BonDatabase bondb = new BonDatabase(db, null, null);
                bondb.remove(currentBon);
                close();
            } catch (SQLException e) {
                showError("Erreur", "Suppression impossible: " + e.getMessage());
            } catch (Exception e) {
                showError("Erreur", "Problème : " + e.getMessage());
            }
        }
    }

    @FXML
    private void onCancel() {
        close();
    }

    private void close() {
        Stage stage = (Stage) emplacementCombo.getScene().getWindow();
        stage.close();
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showInfo(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}