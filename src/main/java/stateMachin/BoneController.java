package stateMachin;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class BoneController extends BaseController {
    @FXML private Label titleLabel;
    @FXML private ListView<String> boneListView;
    @FXML private Button detailsButton;
    @FXML private Button popupButton;
    @FXML private Button locationButton;
    @FXML private Button fournisurButton;

    private boolean initialized = false;

    public BoneController(ControllerStateMachine stateMachine) {
        super(stateMachine);
    }

    @Override
    protected void loadFXML() {
        root = FXMLLoaderUtil.loadFXML("/stateMachin/pages/BoneView.fxml", this);
        initialize();
    }

    private void initialize() {
        if (initialized) return;

        // Setup button actions
        locationButton.setOnAction(e -> goToLocation());
        fournisurButton.setOnAction(e -> goToFournisur());

        // Load items
        boneListView.getItems().addAll("Bone 1", "Bone 2", "Bone 3");

        initialized = true;
    }



    // Navigate to location screen
    private void goToLocation() {
        LocationController locationController =
                (LocationController) stateMachine.getController(LocationController.class);
        stateMachine.changeState(locationController);
    }

    // Navigate to fournisur screen
    private void goToFournisur() {
        FournisurController fournisurController =
                (FournisurController) stateMachine.getController(FournisurController.class);
        stateMachine.changeState(fournisurController);
    }

    @Override
    public void onEnter() {
        super.onEnter();
        // Refresh data if needed when entering this state
        if (boneListView != null && boneListView.getItems().isEmpty()) {
            boneListView.getItems().addAll("Bone 1", "Bone 2", "Bone 3");
        }
    }

    // Example method implementation
    public void method(String type) {
        System.out.println("Bone method called with: " + type);
    }
}