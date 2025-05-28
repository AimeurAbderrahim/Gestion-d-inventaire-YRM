package stateMachin;

import db.configuration.ConfigDatabase;
import db.java.BonDatabase;
import db.java.FournisseurDatabase;
import db.java.ProduitModeleDatabase;
import db.java.InventorierDatabase;
import db.java.ProduitArticleDatabase;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import testpackage.model.core.Bon;
import testpackage.model.core.Fournisseur;
import testpackage.model.core.ProduitModel;
import testpackage.model.core.Inventorier;
import testpackage.model.core.ProduitArticle;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BonDetailsController {
    private static final Logger LOGGER = Logger.getLogger(BonDetailsController.class.getName());
    
    @FXML private Label codeLabel;
    @FXML private Label dateLabel;
    @FXML private Label typeLabel;
    @FXML private Label validLabel;
    @FXML private Label rcLabel;
    @FXML private Label nomLabel;
    @FXML private Label adresseLabel;
    @FXML private Label telephoneLabel;
    @FXML private Label designationLabel;
    @FXML private Label categorieLabel;
    @FXML private Label quantiteLabel;
    @FXML private Button acceptButton;
    @FXML private Button rejectButton;

    private Stage dialogStage;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private Bon currentBon;
    private BoneController parentController;

    public void setParentController(BoneController controller) {
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

            // Load and set fournisseur information
            if (bon.getId_f() != null) {
                ConfigDatabase configDB = new ConfigDatabase();
                FournisseurDatabase fournisseurDB = new FournisseurDatabase(configDB, null, null);
                Fournisseur fournisseur = fournisseurDB.findById(bon.getId_f());
                
                if (fournisseur != null) {
                    rcLabel.setText(fournisseur.getRC());
                    nomLabel.setText(fournisseur.getNom());
                    adresseLabel.setText(fournisseur.getAdresse());
                    telephoneLabel.setText(fournisseur.getNumero_tlph());
                } else {
                    LOGGER.warning("Fournisseur not found for ID: " + bon.getId_f());
                    setFournisseurLabelsNotFound();
                }
            } else {
                LOGGER.warning("No fournisseur ID associated with this bon");
                setFournisseurLabelsNotFound();
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
            setFournisseurLabelsError();
            setProductLabelsError();
        }
    }

    @FXML
    private void onAccept() {
        try {
            ConfigDatabase db = new ConfigDatabase();
            BonDatabase bonDB = new BonDatabase(db, null, null);
            
            // First update the bon status
            currentBon.setValid(true);
            bonDB.update(currentBon, currentBon);
            
            // Then update the product article quantity
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
                    int nouvelleQuantite = existant.getQuantite_global() + inventory.getQuantite();
                    existant.setQuantite_global(nouvelleQuantite);
                    articleDB.update(existant, existant);
                } else {
                    // Create new product article if it doesn't exist
                    ProduitModel produit = new ProduitModeleDatabase(db, null, null).findById(inventory.getId_modele());
                    if (produit != null) {
                        ProduitArticle article = new ProduitArticle();
                        article.setNom_article("Article " + produit.getDesignation());
                        article.setQuantite_global(inventory.getQuantite());
                        article.setDate_dachat(LocalDate.now());
                        article.setDate_peremption(LocalDate.now().plusYears(1));
                        article.setId_modele(inventory.getId_modele());
                        articleDB.add(article);
                    }
                }
            }
            
            validLabel.setText("Validé");
            acceptButton.setVisible(false);
            rejectButton.setVisible(false);
            
            if (parentController != null) {
                parentController.refreshTable();
            }
            
            showInfo("Succès", "Bon accepté avec succès et stock mis à jour");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error accepting bon", e);
            showError("Erreur", "Impossible d'accepter le bon: " + e.getMessage());
        }
    }

    @FXML
    private void onReject() {
        try {
            ConfigDatabase db = new ConfigDatabase();
            BonDatabase bonDB = new BonDatabase(db, null, null);
            
            currentBon.setValid(false);
            bonDB.update(currentBon, currentBon);
            
            validLabel.setText("Rejeté");
            acceptButton.setVisible(false);
            rejectButton.setVisible(false);
            
            if (parentController != null) {
                parentController.refreshTable();
            }
            
            showInfo("Succès", "Bon rejeté avec succès");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error rejecting bon", e);
            showError("Erreur", "Impossible de rejeter le bon: " + e.getMessage());
        }
    }

    private void setFournisseurLabelsNotFound() {
        rcLabel.setText("Non trouvé");
        nomLabel.setText("Non trouvé");
        adresseLabel.setText("Non trouvé");
        telephoneLabel.setText("Non trouvé");
    }

    private void setFournisseurLabelsError() {
        rcLabel.setText("Erreur de chargement");
        nomLabel.setText("Erreur de chargement");
        adresseLabel.setText("Erreur de chargement");
        telephoneLabel.setText("Erreur de chargement");
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

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void onClose() {
        if (dialogStage != null) {
            dialogStage.close();
        }
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