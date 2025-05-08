package stateMachin;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class FournisurController extends BaseController {
    @FXML private Label titleLabel;
    @FXML private ListView<String> supplierListView;
    @FXML private Button detailsButton;
    @FXML private Button popupButton;
    @FXML private Button boneButton;

    private boolean initialized = false;

    public FournisurController(ControllerStateMachine stateMachine) {
        super(stateMachine);
    }

    @Override
    protected void loadFXML() {
        root = FXMLLoaderUtil.loadFXML("/stateMachin/pages/FournisurView.fxml", this);
        initialize();
    }

    private void initialize() {
        if (initialized) return;

        // Setup button actions
        boneButton.setOnAction(e -> goToBone());

        // Load items
        supplierListView.getItems().addAll("Supplier 1", "Supplier 2", "Supplier 3");

        initialized = true;
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
        if (supplierListView != null && supplierListView.getItems().isEmpty()) {
            supplierListView.getItems().addAll("Supplier 1", "Supplier 2", "Supplier 3");
        }
    }

    // Example method implementation
    public void method(String type) {
        System.out.println("Fournisur method called with: " + type);
    }
}