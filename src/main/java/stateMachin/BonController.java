package stateMachin;

import db.configuration.ConfigDatabase;
import db.java.BonDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import testpackage.model.core.Bon;

import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

public class BonController extends BaseController {
    private static final Logger LOGGER = Logger.getLogger(BonController.class.getName());

    @FXML private TableView<Bon> bonTable;
    @FXML private TableColumn<Bon, String> idColumn;
    @FXML private TableColumn<Bon, String> dateColumn;
    @FXML private TableColumn<Bon, Boolean> typeColumn;
    @FXML private TableColumn<Bon, Boolean> validColumn;
    @FXML private TableColumn<Bon, String> emplacementColumn;
    @FXML private TableColumn<Bon, String> fournisseurColumn;
    @FXML private TableColumn<Bon, Integer> quantiteColumn;
    @FXML private TextField searchField;

    private final ObservableList<Bon> bonData = FXCollections.observableArrayList();
    private BonDatabase bonDB;

    @FXML
    public void initialize() {
        try {
            LOGGER.info("Initializing BonController");
            
            // Initialize database
            ConfigDatabase configDB = new ConfigDatabase();
            bonDB = new BonDatabase(configDB, "bon", "id_bon");
            
            // Initialize table columns
            idColumn.setCellValueFactory(new PropertyValueFactory<>("idBon"));
            dateColumn.setCellValueFactory(new PropertyValueFactory<>("bonDate"));
            typeColumn.setCellValueFactory(new PropertyValueFactory<>("bonType"));
            validColumn.setCellValueFactory(new PropertyValueFactory<>("isValid"));
            emplacementColumn.setCellValueFactory(new PropertyValueFactory<>("idEmplacement"));
            fournisseurColumn.setCellValueFactory(new PropertyValueFactory<>("idF"));
            quantiteColumn.setCellValueFactory(new PropertyValueFactory<>("quantite"));
            
            // Load data
            loadBons();
            
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
            
            LOGGER.info("BonController initialized successfully");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error initializing BonController", e);
            showError("Error", "Failed to initialize the bons view: " + e.getMessage());
        }
    }

    protected void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean matchesSearch(Bon bon, String searchText) {
        return bon.getIdBon().toLowerCase().contains(searchText) ||
               bon.getDateBon().toString().toLowerCase().contains(searchText) ||
               bon.getId_emplacement().toLowerCase().contains(searchText) ||
               (bon.getId_f() != null && bon.getId_f().toLowerCase().contains(searchText)) ||
               String.valueOf(bon.getQuantite()).contains(searchText);
    }

    private void loadBons() {
        try {
            List<Bon> bons = bonDB.findAll();
            bonData.clear();
            bonData.addAll(bons);
            bonTable.setItems(bonData);
            LOGGER.info("Loaded " + bons.size() + " bons");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading bons", e);
            showError("Error", "Failed to load bons: " + e.getMessage());
        }
    }
} 