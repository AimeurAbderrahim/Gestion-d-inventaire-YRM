package stateMachin;

import db.configuration.ConfigDatabase;
import db.java.BonDatabase;
import db.java.EmplacementDatabase;
import db.java.ProduitModeleDatabase;
import db.java.ProduitArticleDatabase;
import db.java.InventorierDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import testpackage.model.core.Bon;
import testpackage.model.core.Emplacement;
import testpackage.model.core.ProduitModel;
import testpackage.model.core.ProduitArticle;
import testpackage.model.core.Inventorier;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BonSortiePopUpController {
    private static final Logger LOGGER = Logger.getLogger(BonSortiePopUpController.class.getName());

    @FXML private ComboBox<String> produitTypeCombo;
    @FXML private TextField quantite;
    @FXML private DatePicker dateField;
    @FXML private ComboBox<String> emplacementCombo;

    private Bon currentBon;
    private BonSortieController parentController;
    private final ObservableList<String> emplacementNames = FXCollections.observableArrayList();
    private final ObservableList<String> produitTypes = FXCollections.observableArrayList();
    private Map<String, String> emplacementNameToId = new HashMap<>();
    private Map<String, ProduitModel> produitTypeToModel = new HashMap<>();

    public void setParentController(BonSortieController controller) {
        this.parentController = controller;
    }

    @FXML
    public void initialize() {
        try {
            // Load emplacements
            ConfigDatabase db = new ConfigDatabase();
            EmplacementDatabase emplacementDB = new EmplacementDatabase(db, null, null);
            List<Emplacement> emplacements = emplacementDB.findAll();
            
            // Map emplacement names to IDs and populate combo box
            emplacements.forEach(e -> {
                String displayName = e.getNom_service() + " (" + e.getId_emplacement() + ")";
                emplacementNames.add(displayName);
                emplacementNameToId.put(displayName, e.getId_emplacement());
            });
            emplacementCombo.setItems(emplacementNames);

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
            LOGGER.log(Level.SEVERE, "Error initializing controller", e);
            showError("Erreur", "Impossible de charger les données: " + e.getMessage());
        }
    }

    public void setBon(Bon bon) {
        this.currentBon = bon;
        if (bon != null) {
            dateField.setValue(bon.getDateBon().toLocalDate());
            
            // Find and select the correct emplacement
            emplacementNames.stream()
                .filter(name -> emplacementNameToId.get(name).equals(bon.getId_emplacement()))
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
                showError("Erreur", "Veuillez sélectionner un emplacement");
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
            
            // Get selected product model
            ProduitModel selectedModel = produitTypeToModel.get(produitTypeCombo.getValue());
            
            // Check stock availability
            ProduitArticleDatabase articleDB = new ProduitArticleDatabase(db, null, null);
            List<ProduitArticle> articles = articleDB.findAll();
            ProduitArticle existant = articles.stream()
                .filter(a -> a.getId_modele().equals(selectedModel.getId_modele()))
                .findFirst()
                .orElse(null);

            if (existant == null) {
                showError("Erreur", "Article non trouvé dans le stock");
                return;
            }

            int quantiteValue = Integer.parseInt(quantite.getText());
            if (existant.getQuantite_global() < quantiteValue) {
                showError("Erreur", "Stock insuffisant. Disponible: " + existant.getQuantite_global());
                return;
            }

            // Generate bon ID
            BonDatabase bonDB = new BonDatabase(db, null, null);
            long idx = bonDB.countAll() + 1; // Add 1 to ensure we don't get ID 0
            String bonId = "S" + selectedModel.getId_modele() + String.format("%03d", idx);

            // Create new bon
            Bon bon = new Bon();
            bon.setIdBon(bonId);
            bon.setDateBon(LocalDateTime.from(dateField.getValue().atStartOfDay()));
            bon.setType(false); // sortie
            bon.setValid(false);
            bon.setId_emplacement(emplacementNameToId.get(emplacementCombo.getValue()));
            bon.setQuantite(quantiteValue);
            bon.setReferenceId(selectedModel.getId_modele());

            // Add bon to database FIRST
            bonDB.add(bon);
            
            // THEN create Inventorier record
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
            LOGGER.log(Level.SEVERE, "Error creating bon", e);
            showError("Erreur", "Impossible d'enregistrer : " + e.getMessage());
            
            // Cleanup in case of error
            try {
                ConfigDatabase db = new ConfigDatabase();
                BonDatabase bonDB = new BonDatabase(db, null, null);
                bonDB.remove(currentBon);
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "Error cleaning up after failed creation", ex);
            }
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