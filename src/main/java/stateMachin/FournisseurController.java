package stateMachin;

import db.configuration.ConfigDatabase;
import db.java.FournisseurDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import testpackage.model.core.Fournisseur;

import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

public class FournisseurController extends BaseController {
    private static final Logger LOGGER = Logger.getLogger(FournisseurController.class.getName());

    @FXML private TableView<Fournisseur> fournisseurTable;
    @FXML private TableColumn<Fournisseur, String> idColumn;
    @FXML private TableColumn<Fournisseur, String> nomColumn;
    @FXML private TableColumn<Fournisseur, String> adresseColumn;
    @FXML private TableColumn<Fournisseur, String> telephoneColumn;
    @FXML private TableColumn<Fournisseur, String> emailColumn;
    @FXML private TableColumn<Fournisseur, String> nifColumn;
    @FXML private TableColumn<Fournisseur, String> nisColumn;
    @FXML private TableColumn<Fournisseur, String> rcColumn;
    @FXML private TextField searchField;

    private final ObservableList<Fournisseur> fournisseurData = FXCollections.observableArrayList();
    private FournisseurDatabase fournisseurDB;

    @FXML
    public void initialize() {
        try {
            LOGGER.info("Initializing FournisseurController");
            
            // Initialize database
            ConfigDatabase configDB = new ConfigDatabase();
            fournisseurDB = new FournisseurDatabase(configDB, "fournisseur", "id_f");
            
            // Initialize table columns
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id_f"));
            nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
            adresseColumn.setCellValueFactory(new PropertyValueFactory<>("adresse"));
            telephoneColumn.setCellValueFactory(new PropertyValueFactory<>("numero_tlph"));
            emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
            nifColumn.setCellValueFactory(new PropertyValueFactory<>("NIF"));
            nisColumn.setCellValueFactory(new PropertyValueFactory<>("NIS"));
            rcColumn.setCellValueFactory(new PropertyValueFactory<>("RC"));
            
            // Load data
            loadFournisseurs();
            
            // Add search functionality
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == null || newValue.isEmpty()) {
                    fournisseurTable.setItems(fournisseurData);
                } else {
                    ObservableList<Fournisseur> filteredList = FXCollections.observableArrayList();
                    for (Fournisseur fournisseur : fournisseurData) {
                        if (matchesSearch(fournisseur, newValue.toLowerCase())) {
                            filteredList.add(fournisseur);
                        }
                    }
                    fournisseurTable.setItems(filteredList);
                }
            });
            
            LOGGER.info("FournisseurController initialized successfully");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error initializing FournisseurController", e);
            showError("Error", "Failed to initialize the fournisseurs view: " + e.getMessage());
        }
    }

    protected void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean matchesSearch(Fournisseur fournisseur, String searchText) {
        return fournisseur.getId_f().toLowerCase().contains(searchText) ||
               fournisseur.getNom().toLowerCase().contains(searchText) ||
               fournisseur.getAdresse().toLowerCase().contains(searchText) ||
               fournisseur.getNumero_tlph().toLowerCase().contains(searchText) ||
               fournisseur.getEmail().toLowerCase().contains(searchText) ||
               fournisseur.getNIF().toLowerCase().contains(searchText) ||
               fournisseur.getNIS().toLowerCase().contains(searchText) ||
               fournisseur.getRC().toLowerCase().contains(searchText);
    }

    private void loadFournisseurs() {
        try {
            List<Fournisseur> fournisseurs = fournisseurDB.findAll();
            fournisseurData.clear();
            fournisseurData.addAll(fournisseurs);
            fournisseurTable.setItems(fournisseurData);
            LOGGER.info("Loaded " + fournisseurs.size() + " fournisseurs");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading fournisseurs", e);
            showError("Error", "Failed to load fournisseurs: " + e.getMessage());
        }
    }
} 