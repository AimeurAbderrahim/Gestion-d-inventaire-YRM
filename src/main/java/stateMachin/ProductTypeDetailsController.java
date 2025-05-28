package stateMachin;

import db.java.ProduitModeleDatabase;
import db.java.ProduitArticleDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import testpackage.model.core.ProduitModel;
import testpackage.model.core.ProduitArticle;

import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

public class ProductTypeDetailsController {
    private static final Logger LOGGER = Logger.getLogger(ProductTypeDetailsController.class.getName());

    @FXML private Label idLabel;
    @FXML private Label designationLabel;
    @FXML private Label categoryLabel;
    @FXML private Label typeLabel;
    @FXML private Label bonReceptionLabel;
    @FXML private TableView<ProduitArticle> productsTable;
    @FXML private TableColumn<ProduitArticle, String> productIdColumn;
    @FXML private TableColumn<ProduitArticle, String> productNameColumn;
    @FXML private TableColumn<ProduitArticle, Integer> quantityColumn;
    @FXML private TableColumn<ProduitArticle, String> purchaseDateColumn;
    @FXML private TableColumn<ProduitArticle, String> expiryDateColumn;
    @FXML private TableColumn<ProduitArticle, Void> actionsColumn;
    @FXML private TextField searchField;

    private ProduitModel productType;
    private ProduitModeleDatabase productTypeDB;
    private ProduitArticleDatabase productDB;
    private ObservableList<ProduitArticle> productsList;

    @FXML
    public void initialize() {
        try {
            LOGGER.info("Initializing ProductTypeDetailsController");
            
            // Initialize databases
            productTypeDB = new ProduitModeleDatabase(null, null);
            productDB = new ProduitArticleDatabase(null, null);
            
            // Initialize table columns
            productIdColumn.setCellValueFactory(new PropertyValueFactory<>("id_article"));
            productNameColumn.setCellValueFactory(new PropertyValueFactory<>("nom_article"));
            quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantite_global"));
            purchaseDateColumn.setCellValueFactory(new PropertyValueFactory<>("date_dachat"));
            expiryDateColumn.setCellValueFactory(new PropertyValueFactory<>("date_peremption"));
            
            // Add action buttons to each row
            setupActionsColumn();
            
            // Add search functionality
            setupSearch();
            
            LOGGER.info("ProductTypeDetailsController initialized successfully");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error initializing ProductTypeDetailsController", e);
            showError("Error", "Failed to initialize the details view: " + e.getMessage());
        }
    }

    private void setupActionsColumn() {
        actionsColumn.setCellFactory(column -> new TableCell<>() {
            private final Button editButton = new Button("Modifier");
            private final Button deleteButton = new Button("Supprimer");
            private final HBox buttonsBox = new HBox(5, editButton, deleteButton);

            {
                editButton.getStyleClass().add("button-primary");
                deleteButton.getStyleClass().add("button-danger");
                
                editButton.setOnAction(event -> {
                    ProduitArticle product = getTableRow().getItem();
                    if (product != null) {
                        handleEditProduct(product);
                    }
                });
                
                deleteButton.setOnAction(event -> {
                    ProduitArticle product = getTableRow().getItem();
                    if (product != null) {
                        handleDeleteProduct(product);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttonsBox);
            }
        });
    }

    private void setupSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                productsTable.setItems(productsList);
            } else {
                ObservableList<ProduitArticle> filteredList = FXCollections.observableArrayList();
                for (ProduitArticle product : productsList) {
                    if (matchesSearch(product, newValue.toLowerCase())) {
                        filteredList.add(product);
                    }
                }
                productsTable.setItems(filteredList);
            }
        });
    }

    private boolean matchesSearch(ProduitArticle product, String searchText) {
        return product.getId_article().toLowerCase().contains(searchText) ||
               product.getNom_article().toLowerCase().contains(searchText) ||
               String.valueOf(product.getQuantite_global()).contains(searchText);
    }

    public void setProductType(ProduitModel productType) {
        try {
            LOGGER.info("Setting up details for product type: " + productType.getId_modele());
            this.productType = productType;
            
            // Set labels
            idLabel.setText(productType.getId_modele());
            designationLabel.setText(productType.getDesignation());
            categoryLabel.setText(productType.getCategorie().toString());
            typeLabel.setText(productType.isType_produit() ? "Oui" : "Non");
            bonReceptionLabel.setText(productType.getId_bon());
            
            // Load products
            loadProducts();
            
            LOGGER.info("Product type details loaded successfully");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error setting up product type details", e);
            showError("Error", "Failed to load product type details: " + e.getMessage());
        }
    }

    private void loadProducts() {
        try {
            List<ProduitArticle> products = productDB.findByModelId(productType.getId_modele());
            productsList = FXCollections.observableArrayList(products);
            productsTable.setItems(productsList);
            LOGGER.info("Loaded " + products.size() + " products for type: " + productType.getId_modele());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading products", e);
            showError("Error", "Failed to load products: " + e.getMessage());
        }
    }

    @FXML
    private void handleAddProduct() {
        try {
            LOGGER.info("Opening add product dialog");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/stateMachin/pages/popUps/addProductPopup.fxml"));
            Parent root = loader.load();
            
            AddProductController controller = loader.getController();
            controller.setModelType(productType);
            
            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            
            stage.showAndWait();
            loadProducts(); // Refresh after adding
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error opening add product dialog", e);
            showError("Error", "Failed to open add dialog: " + e.getMessage());
        }
    }

    private void handleEditProduct(ProduitArticle product) {
        try {
            LOGGER.info("Opening edit dialog for product: " + product.getId_article());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/stateMachin/pages/popUps/addProductPopup.fxml"));
            Parent root = loader.load();
            
            AddProductController controller = loader.getController();
            controller.setArticleForEdit(product);
            
            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            
            stage.showAndWait();
            loadProducts(); // Refresh after editing
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error opening edit dialog", e);
            showError("Error", "Failed to open edit dialog: " + e.getMessage());
        }
    }

    private void handleDeleteProduct(ProduitArticle product) {
        try {
            LOGGER.info("Attempting to delete product: " + product.getId_article());
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmer la suppression");
            confirmation.setHeaderText(null);
            confirmation.setContentText("Voulez-vous vraiment supprimer ce produit ?");

            if (confirmation.showAndWait().orElse(null) == ButtonType.OK) {
                productDB.remove(product);
                productsList.remove(product);
                LOGGER.info("Successfully deleted product: " + product.getId_article());
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting product", e);
            showError("Error", "Failed to delete product: " + e.getMessage());
        }
    }

    @FXML
    private void handleEditType() {
        try {
            LOGGER.info("Opening edit dialog for product type: " + productType.getId_modele());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/stateMachin/pages/popUps/addProductTypePopup.fxml"));
            Parent root = loader.load();
            
            AddProductTypeController controller = loader.getController();
            controller.setProductForEdit(productType);
            
            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            
            stage.showAndWait();
            
            // Refresh the details view
            setProductType(productTypeDB.findById(productType.getId_modele()));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error opening edit type dialog", e);
            showError("Error", "Failed to open edit dialog: " + e.getMessage());
        }
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) idLabel.getScene().getWindow();
        stage.close();
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 