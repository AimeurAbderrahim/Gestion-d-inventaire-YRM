package stateMachin;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

public class FournisurController extends BaseController {
    private boolean initialized = false;

    // Default constructor for FXML loader
    public FournisurController() {
        super();
    }

    // Constructor for manual instantiation
    public FournisurController(ControllerStateMachine stateMachine) {
        super(stateMachine);
    }

    @FXML
    private void initialize() {
        if (initialized) return;

        System.out.println("Initializing FournisurController");


                initialized = true;
    }





    @Override
    public void onEnter() {
        super.onEnter();
        // Refresh data when entering this screen
        refreshFournisurData();
    }

    private void refreshFournisurData() {
        System.out.println("Refreshing fournisur data");
    }
}