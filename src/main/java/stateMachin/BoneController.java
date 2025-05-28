package stateMachin;

import db.configuration.ConfigDatabase;
import db.java.BonDatabase;
import db.java.ProduitArticleDatabase;
import db.java.ProduitModeleDatabase;
import db.java.FournisseurDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import testpackage.model.core.Bon;
import testpackage.model.core.ProduitArticle;
import testpackage.model.core.ProduitModel;
import testpackage.model.core.Fournisseur;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.Map;
import java.util.HashMap;

public class BoneController extends BaseController {
    private static final Logger LOGGER = Logger.getLogger(BoneController.class.getName());
    private boolean initialized = false;

    @FXML private TableView<Bon> bonTable;
    @FXML private TableColumn<Bon, String> codeColumn;
    @FXML private TableColumn<Bon, Integer> quantityColumn;
    @FXML private TableColumn<Bon, String> fournisseurColumn;
    @FXML private TableColumn<Bon, LocalDate> dateColumn;
    @FXML private TableColumn<Bon, String> statusColumn;

    private final ObservableList<Bon> bonData = FXCollections.observableArrayList();
    private BonDatabase bonDB;
    private Map<String, String> fournisseurNames = new HashMap<>();

    @FXML
    private void initialize() {
        if (initialized) return;
        initialized = true;

        try {
            LOGGER.info("Initializing BoneController");
            
            // Initialize database
            ConfigDatabase configDB = new ConfigDatabase();
            bonDB = new BonDatabase(configDB, null, null);
            
            // Load fournisseurs
            FournisseurDatabase fournisseurDB = new FournisseurDatabase(configDB, null, null);
            List<Fournisseur> fournisseurs = fournisseurDB.findAll();
            for (Fournisseur f : fournisseurs) {
                fournisseurNames.put(f.getId_f(), f.getNom());
            }
            
            // Initialize table columns
            codeColumn.setCellValueFactory(new PropertyValueFactory<>("idBon"));
            quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantite"));
            fournisseurColumn.setCellValueFactory(new PropertyValueFactory<>("id_f"));
            dateColumn.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getDateBon().toLocalDate()));
            statusColumn.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().isValid() ? "Accepté" : "En attente"));
            
            // Custom cell factory for fournisseur column to show name instead of ID
            fournisseurColumn.setCellFactory(column -> new TableCell<>() {
                @Override
                protected void updateItem(String id, boolean empty) {
                    super.updateItem(id, empty);
                    if (empty || id == null) {
                        setText(null);
                    } else {
                        setText(fournisseurNames.getOrDefault(id, id));
                    }
                }
            });

            // Custom cell factory for quantity column
            quantityColumn.setCellFactory(column -> new TableCell<>() {
                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                        return;
                    }
                    
                    Bon bon = getTableView().getItems().get(getIndex());
                    if (bon == null) {
                        setText("N/A");
                        return;
                    }
                    
                    setText(String.valueOf(bon.getQuantite()));
                }
            });

            // Custom cell factory for status column with colors
            statusColumn.setCellFactory(column -> new TableCell<>() {
                @Override
                protected void updateItem(String status, boolean empty) {
                    super.updateItem(status, empty);
                    if (empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(status);
                        if ("Accepté".equals(status)) {
                            setStyle("-fx-text-fill: green;");
                        } else if ("Rejeté".equals(status)) {
                            setStyle("-fx-text-fill: red;");
                        } else {
                            setStyle("-fx-text-fill: orange;");
                        }
                    }
                }
            });

            // Add double-click handler
            bonTable.setRowFactory(tv -> {
                TableRow<Bon> row = new TableRow<>() {
                    @Override
                    protected void updateItem(Bon bon, boolean empty) {
                        super.updateItem(bon, empty);
                        if (empty || bon == null) {
                            getStyleClass().removeAll("accepted", "rejected");
                        } else {
                            if (bon.isValid()) {
                                getStyleClass().add("accepted");
                                getStyleClass().remove("rejected");
                            } else {
                                getStyleClass().add("rejected");
                                getStyleClass().remove("accepted");
                            }
                        }
                    }
                };
                row.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2 && !row.isEmpty()) {
                        showBonDetails(row.getItem());
                    }
                });
                return row;
            });

            // Set table data
            bonTable.setItems(bonData);
            
            // Load initial data
            loadBonData();
            
            LOGGER.info("BoneController initialized successfully");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error initializing BoneController", e);
            showError("Error", "Failed to initialize the form: " + e.getMessage());
        }
    }

    private void loadBonData() {
        try {
            LOGGER.info("Loading bon data");
            List<Bon> bons = bonDB.filterByType(true); // true for bon de reception
            if (bons != null) {
                bonData.clear();
                bonData.addAll(bons);
                LOGGER.info("Loaded " + bons.size() + " bons");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading bon data", e);
            showError("Error", "Failed to load bons: " + e.getMessage());
        }
    }

    @Override
    public void onEnter() {
        super.onEnter();
        if (initialized) {
            loadBonData(); // Refresh data when entering the screen
        }
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML private void ProduitButtonSwitch(ActionEvent event) {
        stateMachine.changeScene(EnumScenes.Products, event);
    }

    @FXML private void FournisseurButtonSwitch(ActionEvent event) {
        stateMachine.changeScene(EnumScenes.Fournisur, event);
    }

    @FXML private void BonsButtonSwitch(ActionEvent event) {
        stateMachine.changeScene(EnumScenes.Bone, event);
    }

    @FXML private void EmplacementButtonSwitch(ActionEvent event) {
        stateMachine.changeScene(EnumScenes.Location, event);
    }

    @FXML private void BondeSortieButtonSwitch(ActionEvent event) {
        stateMachine.changeScene(EnumScenes.Bondesortie, event);
    }

    @FXML private void SettingsButton(ActionEvent event) {
        try {
            WelcomeController welcome = (WelcomeController) stateMachine.controllers.get(EnumScenes.Welcome);
            if (welcome != null) {
                welcome.SettingsButtonPopUp(event);
            } else {
                System.err.println("WelcomeController non trouvé");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshTable() {
        loadBonData();
    }

    @FXML
    private void ajouter(ActionEvent event){
        try {
            System.out.println("Opening popup for adding a bon de recetpion");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/stateMachin/pages/popUps/BonePopUp.fxml"));
            Parent popupRoot = loader.load();

            // Get the controller and set the parent reference
            BonPopUpController controller = loader.getController();
            controller.setParentController(this);

            Stage popupStage = new Stage();
            popupStage.initStyle(StageStyle.TRANSPARENT);
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("ajouter");
            popupStage.setScene(new Scene(popupRoot, Color.TRANSPARENT));
            popupStage.centerOnScreen();
            popupStage.showAndWait();

        } catch (Exception e) {
            System.err.println("Error opening ajouter popup: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showBonDetails(Bon bon) {
        try {
            LOGGER.info("Showing details for bon: " + bon.getIdBon());
            
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/stateMachin/pages/popUps/BonDetailsPopUp.fxml"));
            Parent page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Détails du Bon");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            BonDetailsController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setParentController(this);
            controller.setBon(bon);

            dialogStage.showAndWait();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error showing bon details", e);
            showError("Erreur", "Impossible d'afficher les détails: " + e.getMessage());
        }
    }
}
