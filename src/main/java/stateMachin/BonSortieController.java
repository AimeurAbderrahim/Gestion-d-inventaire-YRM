package stateMachin;

import db.configuration.ConfigDatabase;
import db.java.BonDatabase;
import db.java.EmplacementDatabase;
import db.java.ProduitModeleDatabase;
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
import testpackage.model.core.Emplacement;
import testpackage.model.core.ProduitModel;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BonSortieController extends BaseController {
    private static final Logger LOGGER = Logger.getLogger(BonSortieController.class.getName());
    private boolean initialized = false;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private BonDatabase bonDB;
    private final ObservableList<Bon> bonData = FXCollections.observableArrayList();

    @FXML private TableView<Bon> bonTable;
    @FXML private TableColumn<Bon, String> codeColumn;
    @FXML private TableColumn<Bon, Integer> quantityColumn;
    @FXML private TableColumn<Bon, String> emplacementColumn;
    @FXML private TableColumn<Bon, String> dateColumn;
    @FXML private TableColumn<Bon, String> statusColumn;
    @FXML private TextField searchField;

    public BonSortieController() {
        super();
    }

    public BonSortieController(ControllerStateMachine stateMachine) {
        super(stateMachine);
    }

    @FXML
    private void initialize() {
        if (initialized) return;
        initialized = true;

        LOGGER.info("Initializing BonSortieController");

        try {
            ConfigDatabase db = new ConfigDatabase();
            bonDB = new BonDatabase(db, null, null);

            // Initialize table columns
            codeColumn.setCellValueFactory(new PropertyValueFactory<>("idBon"));
            quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantite"));
            dateColumn.setCellValueFactory(cellData -> 
                javafx.beans.binding.Bindings.createStringBinding(
                    () -> cellData.getValue().getDateBon().format(dateFormatter)
                )
            );
            
            // Custom cell factory for emplacement
            emplacementColumn.setCellValueFactory(cellData -> {
                Bon bon = cellData.getValue();
                return javafx.beans.binding.Bindings.createStringBinding(() -> {
                    try {
                        EmplacementDatabase empDB = new EmplacementDatabase(new ConfigDatabase(), null, null);
                        Emplacement emp = empDB.findById(bon.getId_emplacement());
                        return emp != null ? emp.getNom_service() : "N/A";
                    } catch (Exception e) {
                        LOGGER.log(Level.WARNING, "Error loading emplacement", e);
                        return "Error";
                    }
                });
            });

            // Custom cell factory for status with colors
            statusColumn.setCellValueFactory(cellData -> 
                javafx.beans.binding.Bindings.createStringBinding(
                    () -> cellData.getValue().isValid() ? "Validé" : "En attente"
                )
            );
            statusColumn.setCellFactory(column -> new TableCell<Bon, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item);
                        if ("Validé".equals(item)) {
                            setStyle("-fx-text-fill: green;");
                        } else {
                            setStyle("-fx-text-fill: orange;");
                        }
                    }
                }
            });

            // Add search functionality
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == null || newValue.isEmpty()) {
                    bonTable.setItems(bonData);
                } else {
                    ObservableList<Bon> filteredList = FXCollections.observableArrayList();
                    for (Bon bon : bonData) {
                        if (matchesSearch(bon, newValue.toLowerCase())) {
                            filteredList.add(bon);
                        }
                    }
                    bonTable.setItems(filteredList);
                }
            });

            // Set table items
            bonTable.setItems(bonData);

            // Add double-click handler
            bonTable.setRowFactory(tv -> {
                TableRow<Bon> row = new TableRow<>();
                row.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2 && !row.isEmpty()) {
                        showBonDetails(row.getItem());
                    }
                });
                return row;
            });

            // Load initial data
            loadBonData();
            
            LOGGER.info("BonSortieController initialized successfully");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error initializing controller", e);
            showError("Error", "Failed to initialize the view: " + e.getMessage());
        }
    }

    private boolean matchesSearch(Bon bon, String searchText) {
        return bon.getIdBon().toLowerCase().contains(searchText) ||
               bon.getDateBon().format(dateFormatter).toLowerCase().contains(searchText) ||
               bon.getId_emplacement().toLowerCase().contains(searchText) ||
               String.valueOf(bon.getQuantite()).contains(searchText) ||
               (bon.isValid() ? "validé" : "en attente").contains(searchText);
    }

    private void loadBonData() {
        try {
            LOGGER.info("Loading bon data");
            List<Bon> bons = bonDB.filterByType(false); // false for Bon de Sortie
            bonData.clear();
            if (bons != null) {
                bonData.addAll(bons);
            }
            bonTable.setItems(bonData);
            LOGGER.info("Loaded " + (bons != null ? bons.size() : 0) + " bons");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading bons", e);
            showError("Error", "Failed to load bons: " + e.getMessage());
        }
    }

    @FXML
    private void ajouter(ActionEvent event) {
        try {
            LOGGER.info("Opening popup for adding a bon de sortie");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/stateMachin/pages/popUps/BonSortiePopUp.fxml"));
            Parent popupRoot = loader.load();

            // Get the controller and set the parent reference
            BonSortiePopUpController controller = loader.getController();
            controller.setParentController(this);

            Stage popupStage = new Stage();
            popupStage.initStyle(StageStyle.TRANSPARENT);
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Ajouter Bon de Sortie");
            popupStage.setScene(new Scene(popupRoot, Color.TRANSPARENT));
            popupStage.centerOnScreen();
            popupStage.showAndWait();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error opening ajouter popup", e);
            showError("Error", "Failed to open add dialog: " + e.getMessage());
        }
    }

    private void showBonDetails(Bon bon) {
        try {
            LOGGER.info("Showing details for bon: " + bon.getIdBon());
            
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/stateMachin/pages/popUps/BonSortieDetailsPopUp.fxml"));
            Parent page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Détails du Bon de Sortie");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            BonSortieDetailsController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setParentController(this);
            controller.setBon(bon);

            dialogStage.showAndWait();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error showing bon details", e);
            showError("Erreur", "Impossible d'afficher les détails: " + e.getMessage());
        }
    }

    @Override
    public void onEnter() {
        super.onEnter();
        if (initialized) {
            loadBonData();
        }
    }

    public void refreshTable() {
        loadBonData();
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

    @FXML private void BondeReceptionButtonSwitch(ActionEvent event) {
        stateMachine.changeScene(EnumScenes.Bone, event);
    }
} 