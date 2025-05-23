package temp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import stateMachin.BaseController;
import stateMachin.ControllerStateMachine;
import stateMachin.LoginController;

public class LocationController extends BaseController {
    @FXML private AnchorPane mainPane;
    @FXML private TableView<?> locationTable;
    @FXML private Button addButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;
    @FXML private Button backButton;

    private boolean initialized = false;

    public LocationController(ControllerStateMachine stateMachine) {
        super(stateMachine);
    }



    private void initialize() {
        if (initialized) return;

        System.out.println("Initializing LocationController");


        initialized = true;
    }

    @Override
    public void onEnter() {
        super.onEnter();
        System.out.println("Entered Location screen");
        // Load or refresh location data
        refreshLocationData();
    }

    private void refreshLocationData() {
        // Here you would load location data from your data source
        // and populate the tableView
        System.out.println("Refreshing location data");
    }
    @Override
    @FXML
    public void ProduitButtonSwitch(ActionEvent event) {} // no button switch here but we need to declare empty block because this is not abstract class
    @Override
    @FXML
    public void FournisseurButtonSwitch(ActionEvent event) {} // no button switch here but we need to declare empty block because this is not abstract class
    @Override
    @FXML
    public void BonsButtonSwitch(ActionEvent event) {} // no button switch here but we need to declare empty block because this is not abstract class
    @Override
    @FXML
    public void EmplacementButtonSwitch(ActionEvent event) {} // no button switch here but we need to declare empty block because this is not abstract class
}
