package temp;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
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

    // Login controller doesn't have to implements Switch abstract buttons
    @Override
    @FXML
    protected void ProduitButtonSwitch(ActionEvent event) {}
    @Override
    @FXML
    protected void FournisseurButtonSwitch(ActionEvent event) {}
    @Override
    @FXML
    protected void BonsButtonSwitch(ActionEvent event) {}
    @Override
    @FXML
    protected void EmplacementButtonSwitch(ActionEvent event) {}
}
