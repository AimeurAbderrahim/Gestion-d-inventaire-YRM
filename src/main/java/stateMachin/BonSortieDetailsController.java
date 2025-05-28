package stateMachin;

import db.configuration.ConfigDatabase;
import db.java.BonDatabase;
import db.java.EmplacementDatabase;
import db.java.ProduitModeleDatabase;
import db.java.InventorierDatabase;
import db.java.ProduitArticleDatabase;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import testpackage.model.core.Bon;
import testpackage.model.core.Emplacement;
import testpackage.model.core.ProduitModel;
import testpackage.model.core.Inventorier;
import testpackage.model.core.ProduitArticle;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BonSortieDetailsController {
    private static final Logger LOGGER = Logger.getLogger(BonSortieDetailsController.class.getName());
    
    @FXML private Label codeLabel;
    @FXML private Label dateLabel;
    @FXML private Label typeLabel;
    @FXML private Label validLabel;
    @FXML private Label emplacementLabel;
    @FXML private Label designationLabel;
    @FXML private Label categorieLabel;
    @FXML private Label quantiteLabel;
    @FXML private Button acceptButton;
    @FXML private Button rejectButton;

    private Stage dialogStage;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private Bon currentBon;
    private BonSortieController parentController;

    public void setParentController(BonSortieController controller) {
        this.parentController = controller;
    }

    public void setBon(Bon bon) {
        this.currentBon = bon;
        try {
            // Set bon information
            codeLabel.setText(bon.getIdBon());
            dateLabel.setText(bon.getDateBon().format(dateFormatter));
            typeLabel.setText(bon.isBonReception() ? "Reception" : "Sortie");
            validLabel.setText(bon.isValid() ? "Validé" : "En attente");
            quantiteLabel.setText(String.valueOf(bon.getQuantite()));

            // Update button visibility based on bon status
            boolean isValidated = bon.isValid();
            acceptButton.setVisible(!isValidated);
            rejectButton.setVisible(!isValidated);

            // Load and set emplacement information
            if (bon.getId_emplacement() != null) {
                ConfigDatabase configDB = new ConfigDatabase();
                EmplacementDatabase emplacementDB = new EmplacementDatabase(configDB, null, null);
                Emplacement emplacement = emplacementDB.findById(bon.getId_emplacement());
                
                if (emplacement != null) {
                    emplacementLabel.setText(emplacement.getNom_service());
                } else {
                    LOGGER.warning("Emplacement not found for ID: " + bon.getId_emplacement());
                    emplacementLabel.setText("Non trouvé");
                }
            } else {
                LOGGER.warning("No emplacement ID associated with this bon");
                emplacementLabel.setText("Non trouvé");
            }

            // Load and set product information from Inventorier and ProduitModel
            try {
                ConfigDatabase configDB = new ConfigDatabase();
                InventorierDatabase inventorierDB = new InventorierDatabase(configDB, null, null);
                ProduitModeleDatabase produitDB = new ProduitModeleDatabase(configDB, null, null);
                
                // Find the inventory record for this bon
                List<Inventorier> inventoryRecords = inventorierDB.findAll();
                Inventorier inventory = inventoryRecords.stream()
                    .filter(i -> i.getId_bon().equals(bon.getIdBon()))
                    .findFirst()
                    .orElse(null);
                
                if (inventory != null) {
                    // Get the product model details
                    ProduitModel produit = produitDB.findById(inventory.getId_modele());
                    if (produit != null) {
                        designationLabel.setText(produit.getDesignation());
                        categorieLabel.setText(produit.getCategorie().toString());
                        quantiteLabel.setText(String.valueOf(inventory.getQuantite()));
                    } else {
                        LOGGER.warning("Product model not found for ID: " + inventory.getId_modele());
                        setProductLabelsNotFound();
                    }
                } else {
                    LOGGER.warning("Inventory record not found for bon ID: " + bon.getIdBon());
                    setProductLabelsNotFound();
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error loading product details", e);
                setProductLabelsError();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading bon details", e);
            setProductLabelsError();
        }
    }

    @FXML
    private void onAccept() {
        try {
            ConfigDatabase db = new ConfigDatabase();
            BonDatabase bonDB = new BonDatabase(db, null, null);
            
            // First check stock availability
            ProduitArticleDatabase articleDB = new ProduitArticleDatabase(db, null, null);
            InventorierDatabase inventorierDB = new InventorierDatabase(db, null, null);
            
            // Find the inventory record for this bon
            List<Inventorier> inventoryRecords = inventorierDB.findAll();
            Inventorier inventory = inventoryRecords.stream()
                .filter(i -> i.getId_bon().equals(currentBon.getIdBon()))
                .findFirst()
                .orElse(null);
            
            if (inventory != null) {
                // Find and update the product article quantity
                List<ProduitArticle> articles = articleDB.findAll();
                ProduitArticle existant = articles.stream()
                    .filter(a -> a.getId_modele().equals(inventory.getId_modele()))
                    .findFirst()
                    .orElse(null);

                if (existant != null) {
                    // Decrease stock for bon de sortie
                    int nouvelleQuantite = existant.getQuantite_global() - inventory.getQuantite();
                    if (nouvelleQuantite < 0) {
                        showError("Erreur", "Stock insuffisant pour cette sortie");
                        return;
                    }
                    existant.setQuantite_global(nouvelleQuantite);
                    articleDB.update(existant, existant);
                    
                    // Update bon status
                    currentBon.setValid(true);
                    bonDB.update(currentBon, currentBon);
                    
                    // Update UI
                    validLabel.setText("Validé");
                    acceptButton.setVisible(false);
                    rejectButton.setVisible(false);
                    
                    // Refresh parent table
                    if (parentController != null) {
                        parentController.refreshTable();
                    }
                    
                    showInfo("Succès", "Bon validé et stock mis à jour");
                } else {
                    showError("Erreur", "Article non trouvé dans le stock");
                }
            } else {
                showError("Erreur", "Enregistrement d'inventaire non trouvé");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error accepting bon", e);
            showError("Erreur", "Impossible d'accepter le bon: " + e.getMessage());
        }
    }

    @FXML
    private void onReject() {
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
            BonDatabase bonDB = new BonDatabase(db, null, null);
            bonDB.remove(currentBon);
            
            if (parentController != null) {
                parentController.refreshTable();
            }
            
            showInfo("Succès", "Bon supprimé avec succès");
            onClose();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error rejecting bon", e);
            showError("Erreur", "Impossible de rejeter le bon: " + e.getMessage());
        }
    }

    @FXML
    private void onClose() {
        if (dialogStage != null) {
            dialogStage.close();
        }
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    private void setProductLabelsNotFound() {
        designationLabel.setText("Non trouvé");
        categorieLabel.setText("Non trouvé");
        quantiteLabel.setText("Non trouvé");
    }

    private void setProductLabelsError() {
        designationLabel.setText("Erreur de chargement");
        categorieLabel.setText("Erreur de chargement");
        quantiteLabel.setText("Erreur de chargement");
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