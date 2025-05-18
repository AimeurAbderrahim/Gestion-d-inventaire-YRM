package temp;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import stateMachin.BaseController;
import stateMachin.ControllerStateMachine;

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



    private void initialize() {
        if (initialized) return;

        // Setup button actions
     //   locationButton.setOnAction(e -> goToLocation());
//        fournisurButton.setOnAction(e -> goToFournisur());
//
//        // Load items
//        boneListView.getItems().addAll("Bone 1", "Bone 2", "Bone 3");

        initialized = true;
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