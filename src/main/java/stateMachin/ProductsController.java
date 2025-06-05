package stateMachin;

import db.configuration.ConfigDatabase;
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
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import testpackage.model.core.ProduitModel;
import testpackage.model.enumeration.Categorie;

import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

public class ProductsController extends BaseController {
    private static final Logger LOGGER = Logger.getLogger(ProductsController.class.getName());

    private boolean initialized = false;

    @FXML private TableView<ProduitModel> productTypeTable;
    @FXML private TableColumn<ProduitModel, String> idColumn;
    @FXML private TableColumn<ProduitModel, String> designationColumn;
    @FXML private TableColumn<ProduitModel, Categorie> categoryColumn;
    @FXML private TableColumn<ProduitModel, Boolean> typeColumn;
    @FXML private TableColumn<ProduitModel, String> bonReceptionColumn;
    @FXML private TableColumn<ProduitModel, Void> actionsColumn;
    @FXML private TextField searchField;

    private final ObservableList<ProduitModel> productTypeData = FXCollections.observableArrayList();
    private ProduitModeleDatabase productDB;

    @FXML
    public void initialize() {
        if (initialized) {
            return;
        }
        initialized = true;

        try {
            LOGGER.info("Initializing ProductsController");
            
            // Initialize database
            productDB = new ProduitModeleDatabase(null, null);
            
            // Initialize table columns
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id_modele"));
            designationColumn.setCellValueFactory(new PropertyValueFactory<>("designation"));
            categoryColumn.setCellValueFactory(new PropertyValueFactory<>("categorie"));
            typeColumn.setCellValueFactory(new PropertyValueFactory<>("type_produit"));
            bonReceptionColumn.setCellValueFactory(new PropertyValueFactory<>("id_bon"));
            
            // Custom cell factory for boolean type column
            typeColumn.setCellFactory(column -> new TableCell<>() {
                @Override
                protected void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item ? "Oui" : "Non");
                    }
                }
            });

            // Add action buttons to each row
            actionsColumn.setCellFactory(column -> new TableCell<>() {
                private final Button editButton = new Button("Modifier");
                private final Button deleteButton = new Button("Supprimer");
                private final HBox buttonsBox = new HBox(5, editButton, deleteButton);

                {
                    editButton.getStyleClass().add("button-primary");
                    deleteButton.getStyleClass().add("button-danger");
                    
                    editButton.setOnAction(event -> {
                        ProduitModel product = getTableRow().getItem();
                        if (product != null) {
                            handleEdit(product);
                        }
                    });
                    
                    deleteButton.setOnAction(event -> {
                        ProduitModel product = getTableRow().getItem();
                        if (product != null) {
                            handleDelete(product);
                        }
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(buttonsBox);
                    }
                }
            });

            // Add double-click handler for showing details
            productTypeTable.setRowFactory(tv -> {
                TableRow<ProduitModel> row = new TableRow<>();
                row.setOnMouseClicked(event -> {
                    if (!row.isEmpty() && event.getClickCount() == 2) {
                        showProductDetails(row.getItem());
                    }
                });
                return row;
            });

            // Load data
            loadProductTypes();
            
            // Add search functionality
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == null || newValue.isEmpty()) {
                    productTypeTable.setItems(productTypeData);
                } else {
                    ObservableList<ProduitModel> filteredList = FXCollections.observableArrayList();
                    for (ProduitModel product : productTypeData) {
                        if (matchesSearch(product, newValue.toLowerCase())) {
                            filteredList.add(product);
                        }
                    }
                    productTypeTable.setItems(filteredList);
                }
            });

            LOGGER.info("ProductsController initialized successfully");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error initializing ProductsController", e);
            showError("Error", "Failed to initialize the product types view: " + e.getMessage());
        }
    }

    private boolean matchesSearch(ProduitModel product, String searchText) {
        return product.getId_modele().toLowerCase().contains(searchText) ||
               product.getDesignation().toLowerCase().contains(searchText) ||
               product.getCategorie().toString().toLowerCase().contains(searchText) ||
               product.getId_bon().toLowerCase().contains(searchText);
    }

    private void loadProductTypes() {
        try {
            productTypeData.clear();
            productTypeData.addAll(productDB.findAll());
            productTypeTable.setItems(productTypeData);
            LOGGER.info("Loaded " + productTypeData.size() + " product types");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading product types", e);
            showError("Error", "Failed to load product types: " + e.getMessage());
        }
    }

    @FXML
    private void handleAddProductType() {
        try {
            LOGGER.info("Opening add product type dialog");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/stateMachin/pages/popUps/addProductTypePopup.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            
            stage.showAndWait();
            loadProductTypes(); // Refresh the table after adding
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error opening add product type dialog", e);
            showError("Error", "Failed to open add dialog: " + e.getMessage());
        }
    }

    private void handleEdit(ProduitModel product) {
        try {
            LOGGER.info("Opening edit dialog for product type: " + product.getId_modele());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/stateMachin/pages/popUps/addProductTypePopup.fxml"));
            Parent root = loader.load();
            
            AddProductTypeController controller = loader.getController();
            controller.setProductForEdit(product);
            
            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            
            stage.showAndWait();
            loadProductTypes(); // Refresh the table after editing
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error opening edit dialog", e);
            showError("Error", "Failed to open edit dialog: " + e.getMessage());
        }
    }

    private void handleDelete(ProduitModel product) {
        try {
            LOGGER.info("Attempting to delete product type: " + product.getId_modele());
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmer la suppression");
            confirmation.setHeaderText(null);
            confirmation.setContentText("Voulez-vous vraiment supprimer ce type de produit ?");

            if (confirmation.showAndWait().orElse(null) == ButtonType.OK) {
                productDB.remove(product);
                productTypeData.remove(product);
                LOGGER.info("Successfully deleted product type: " + product.getId_modele());
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting product type", e);
            showError("Error", "Failed to delete product type: " + e.getMessage());
        }
    }

    private void showProductDetails(ProduitModel productType) {
        try {
            LOGGER.info("Opening details for product type: " + productType.getId_modele());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/stateMachin/pages/popUps/ProductTypeDetailsPopUp.fxml"));
            Parent root = loader.load();

            ProductTypeDetailsController controller = loader.getController();
            controller.setProductType(productType);

            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
            stage.showAndWait();

            loadProductTypes(); // Refresh in case any changes were made
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error showing product details", e);
            showError("Error", "Failed to show product details: " + e.getMessage());
        }
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @Override
    public void onEnter() {
        super.onEnter();
        loadProductTypes();
    }
    @FXML
    private void logOut(ActionEvent event) {
        stateMachine.changeScene(EnumScenes.Login, event);
    }

    @FXML
    private void SettingsButton(ActionEvent event) {
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

    // Navigation buttons
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
}