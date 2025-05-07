package stateMachin;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class ProductController extends BaseController {
    @FXML private Label titleLabel;
    @FXML private ListView<String> productListView;
    @FXML private Button detailsButton;
    @FXML private Button popupButton;
    @FXML private Button locationButton;

    private boolean initialized = false;

    public ProductController(ControllerStateMachine stateMachine) {
        super(stateMachine);
    }

    @Override
    protected void loadFXML() {
        root = FXMLLoaderUtil.loadFXML("/testpackage/gestiondinventaireyrm/pages/ProductView.fxml", this);
        initialize();
    }

    private void initialize() {
        if (initialized) return;

        // Setup button actions
        detailsButton.setOnAction(e -> showDetails());
        popupButton.setOnAction(e -> showPopUp());
        locationButton.setOnAction(e -> goToLocation());

        // Load items asynchronously
        Platform.runLater(() -> {
            productListView.getItems().addAll("Product 1", "Product 2", "Product 3");
        });

        initialized = true;
    }

    // Show details view
    private void showDetails() {
        DetailsController detailsController = (DetailsController) stateMachine.getController(DetailsController.class);
        stateMachine.changeState(detailsController);
    }

    // Show popup view
    private void showPopUp() {
        PopUpController popUpController = (PopUpController) stateMachine.getController(PopUpController.class);
        stateMachine.changeState(popUpController);
    }

    // Navigate to location screen
    private void goToLocation() {
        // Use the cached controller if available
        LocationController locationController =
                (LocationController) stateMachine.getController(LocationController.class);
        stateMachine.changeState(locationController);
    }

    @Override
    public void onEnter() {
        super.onEnter();
        // Refresh data if needed when entering this state
        if (productListView != null && productListView.getItems().isEmpty()) {
            Platform.runLater(() -> {
                productListView.getItems().addAll("Product 1", "Product 2", "Product 3");
            });
        }
    }
}//