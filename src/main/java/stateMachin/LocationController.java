package stateMachin;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class LocationController extends BaseController {
    @FXML private Label titleLabel;
    @FXML private ListView<String> locationListView;
    @FXML private Button detailsButton;
    @FXML private Button popupButton;
    @FXML private Button productButton;
    @FXML private Button boneButton;

    private boolean initialized = false;

    public LocationController(ControllerStateMachine stateMachine) {
        super(stateMachine);
    }

    @Override
    protected void loadFXML() {
        root = FXMLLoaderUtil.loadFXML("/stateMachin/pages/LocationView.fxml", this);
        initialize();
    }

    private void initialize() {
        if (initialized) return;

        // Setup button actions
        productButton.setOnAction(e -> goToProduct());
        boneButton.setOnAction(e -> goToBone());

        // Load items
        locationListView.getItems().addAll("Location A", "Location B", "Location C");

        initialized = true;
    }





    // Navigate to product screen
    private void goToProduct() {
        ProductController productController =
                (ProductController) stateMachine.getController(ProductController.class);
        stateMachine.changeState(productController);
    }

    // Navigate to bone screen
    private void goToBone() {
        BoneController boneController =
                (BoneController) stateMachine.getController(BoneController.class);
        stateMachine.changeState(boneController);
    }

    @Override
    public void onEnter() {
        super.onEnter();
        // Refresh data if needed when entering this state
        if (locationListView != null && locationListView.getItems().isEmpty()) {
            locationListView.getItems().addAll("Location A", "Location B", "Location C");
        }
    }

    // Example method implementation
    public void method(String type) {
        System.out.println("Location method called with: " + type);
    }
}