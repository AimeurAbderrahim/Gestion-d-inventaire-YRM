package stateMachin;

import db.configuration.ConfigDatabase;
import db.java.EmplacementDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import testpackage.model.core.Emplacement;

import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

public class EmplacementController extends BaseController {
    private static final Logger LOGGER = Logger.getLogger(EmplacementController.class.getName());

    @FXML private TableView<Emplacement> emplacementTable;
    @FXML private TableColumn<Emplacement, String> idColumn;
    @FXML private TableColumn<Emplacement, String> typeSalleColumn;
    @FXML private TableColumn<Emplacement, Double> superficieColumn;
    @FXML private TableColumn<Emplacement, Integer> bureauColumn;
    @FXML private TableColumn<Emplacement, String> serviceColumn;
    @FXML private TextField searchField;

    private final ObservableList<Emplacement> emplacementData = FXCollections.observableArrayList();
    private EmplacementDatabase emplacementDB;

    @FXML
    public void initialize() {
        try {
            LOGGER.info("Initializing EmplacementController");
            
            // Initialize database
            ConfigDatabase configDB = new ConfigDatabase();
            emplacementDB = new EmplacementDatabase(configDB, "emplacement", "id_emplacement");
            
            // Initialize table columns
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id_emplacement"));
            typeSalleColumn.setCellValueFactory(new PropertyValueFactory<>("type_salle"));
            superficieColumn.setCellValueFactory(new PropertyValueFactory<>("superficie"));
            bureauColumn.setCellValueFactory(new PropertyValueFactory<>("bureau"));
            serviceColumn.setCellValueFactory(new PropertyValueFactory<>("nom_service"));
            
            // Load data
            loadEmplacements();
            
            // Add search functionality
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == null || newValue.isEmpty()) {
                    emplacementTable.setItems(emplacementData);
                } else {
                    ObservableList<Emplacement> filteredList = FXCollections.observableArrayList();
                    for (Emplacement emplacement : emplacementData) {
                        if (matchesSearch(emplacement, newValue.toLowerCase())) {
                            filteredList.add(emplacement);
                        }
                    }
                    emplacementTable.setItems(filteredList);
                }
            });
            
            LOGGER.info("EmplacementController initialized successfully");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error initializing EmplacementController", e);
            showError("Error", "Failed to initialize the emplacements view: " + e.getMessage());
        }
    }

    protected void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean matchesSearch(Emplacement emplacement, String searchText) {
        return emplacement.getId_emplacement().toLowerCase().contains(searchText) ||
               emplacement.getType_salle().toLowerCase().contains(searchText) ||
               String.valueOf(emplacement.getSuperficie()).contains(searchText) ||
               String.valueOf(emplacement.getBureau()).contains(searchText) ||
               emplacement.getNom_service().toLowerCase().contains(searchText);
    }

    private void loadEmplacements() {
        try {
            List<Emplacement> emplacements = emplacementDB.findAll();
            emplacementData.clear();
            emplacementData.addAll(emplacements);
            emplacementTable.setItems(emplacementData);
            LOGGER.info("Loaded " + emplacements.size() + " emplacements");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading emplacements", e);
            showError("Error", "Failed to load emplacements: " + e.getMessage());
        }
    }
} 