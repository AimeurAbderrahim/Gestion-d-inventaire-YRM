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
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
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

public class BonPopUpController {
    private static final Logger LOGGER = Logger.getLogger(BonPopUpController.class.getName());

    @FXML private ComboBox<String> produitTypeCombo;
    @FXML private TextField quantite;
    @FXML private DatePicker dateField;
    @FXML private ComboBox<String> emplacementCombo;
    @FXML private Button closeButton;

    private BoneController parentController;
    private final ObservableList<String> emplacementNames = FXCollections.observableArrayList();
    private final ObservableList<String> produitTypes = FXCollections.observableArrayList();
    private Map<String, String> emplacementNameToId = new HashMap<>();
    private Map<String, ProduitModel> produitTypeToModel = new HashMap<>();

    @FXML
    private void initialize() {
        // Add window drag functionality
        Node root = closeButton.getScene().getRoot();
        root.setOnMousePressed(event -> {
            root.setUserData(new double[]{event.getSceneX(), event.getSceneY()});
        });
        root.setOnMouseDragged(event -> {
            double[] start = (double[]) root.getUserData();
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.setX(event.getScreenX() - start[0]);
            stage.setY(event.getScreenY() - start[1]);
        });

        // Add shadow effect
        root.setEffect(new DropShadow(10, Color.gray(0, 0.2)));

        loadData();
    }

    private void loadData() {
        try {
            ConfigDatabase db = new ConfigDatabase();
            
            // Load emplacements
            EmplacementDatabase emplacementDB = new EmplacementDatabase(db, null, null);
            List<Emplacement> emplacements = emplacementDB.findAll();
            emplacementNames.clear();
            emplacementNameToId.clear();
            for (Emplacement e : emplacements) {
                emplacementNames.add(e.getNom());
                emplacementNameToId.put(e.getNom(), e.getId_emplacement());
            }
            emplacementCombo.setItems(emplacementNames);

            // Load product types
            ProduitModeleDatabase produitDB = new ProduitModeleDatabase(db, null, null);
            List<ProduitModel> produits = produitDB.findAll();
            produitTypes.clear();
            produitTypeToModel.clear();
            for (ProduitModel p : produits) {
                produitTypes.add(p.getDesignation());
                produitTypeToModel.put(p.getDesignation(), p);
            }
            produitTypeCombo.setItems(produitTypes);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading data", e);
            showError("Error", "Failed to load data: " + e.getMessage());
        }
    }

    public void setParentController(BoneController controller) {
        this.parentController = controller;
    }

    @FXML
    private void onCancel() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void Confirm() {
        try {
            // Validate inputs
            if (dateField.getValue() == null) {
                showError("Error", "Please select a date");
                return;
            }
            if (emplacementCombo.getValue() == null) {
                showError("Error", "Please select a fournisseur");
                return;
            }
            if (produitTypeCombo.getValue() == null) {
                showError("Error", "Please select a product type");
                return;
            }
            if (quantite.getText().isEmpty()) {
                showError("Error", "Please enter a quantity");
                return;
            }

            // Create new Bon
            ConfigDatabase db = new ConfigDatabase();
            BonDatabase bonDB = new BonDatabase(db, null, null);
            
            Bon newBon = new Bon();
            newBon.setDateBon(LocalDateTime.now());
            newBon.setBonReception(true);
            newBon.setValid(false);
            newBon.setId_f(emplacementNameToId.get(emplacementCombo.getValue()));
            newBon.setQuantite(Integer.parseInt(quantite.getText()));

            // Generate unique ID
            String id = "R" + System.currentTimeMillis();
            newBon.setIdBon(id);

            // Save to database
            bonDB.add(newBon);

            // Create Inventorier record
            InventorierDatabase inventorierDB = new InventorierDatabase(db, null, null);
            Inventorier inventorier = new Inventorier();
            inventorier.setId_bon(id);
            inventorier.setId_type(produitTypeToModel.get(produitTypeCombo.getValue()).getId_type());
            inventorierDB.add(inventorier);

            // Refresh parent and close
            if (parentController != null) {
                parentController.refreshTable();
            }
            onCancel();

        } catch (NumberFormatException e) {
            showError("Error", "Invalid quantity format");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error saving bon", e);
            showError("Error", "Failed to save: " + e.getMessage());
        }
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
