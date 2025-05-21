package stateMachin;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

public class BoneController extends BaseController {
     private boolean initialized = false;

    // Default constructor for FXML loader
    public BoneController() {
        super();
    }

    // Constructor for manual instantiation
    public BoneController(ControllerStateMachine stateMachine) {
        super(stateMachine);
    }

    @FXML
    private void initialize() {
        if (initialized) return;

        System.out.println("Initializing BoneController");


        initialized = true;
    }



    @Override
    public void onEnter() {
        super.onEnter();
        // Refresh data when entering this screen
        refreshBoneData();
    }

    private void refreshBoneData() {
        System.out.println("Refreshing bone data");
        // Load/reload bone data into the table
    }
}