package stateMachin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

public class LocationController extends BaseController {

    private boolean initialized = false;

    // Default constructor for FXML loader
    public LocationController() {
        super();
    }

    // Constructor for manual instantiation
    public LocationController(ControllerStateMachine stateMachine) {
        super(stateMachine);
    }

    @FXML
    private void ProduitButtonSwitch(ActionEvent event) {
        try{
            System.out.println("Changing scene to Products");
            stateMachine.changeScene(EnumScenes.Products, event);
            System.out.println("Scene change initiated");

        }catch (Exception e){
            System.err.println("Exception during switching " + e.getMessage());
            e.printStackTrace();
        }

    }
    @FXML
    private void FournisseurButtonSwitch(ActionEvent event) {
        try{
            System.out.println("Changing scene to Fournisseur");
            stateMachine.changeScene(EnumScenes.Fournisur, event);
            System.out.println("Scene change initiated");

        }catch (Exception e){
            System.err.println("Exception during switching " + e.getMessage());
            e.printStackTrace();
        }

    }
    @FXML
    private void BonsButtonSwitch(ActionEvent event) {
        try{
            System.out.println("Changing scene to bons");
            stateMachine.changeScene(EnumScenes.Bone, event);
            System.out.println("Scene change initiated");

        }catch (Exception e){
            System.err.println("Exception during switching " + e.getMessage());
            e.printStackTrace();
        }

    }
    @FXML
    private void EmplacementButtonSwitch(ActionEvent event) {
        try{
            System.out.println("Changing scene to Emplacement");
            stateMachine.changeScene(EnumScenes.Location, event);
            System.out.println("Scene change initiated");

        }catch (Exception e){
            System.err.println("Exception during switching " + e.getMessage());
            e.printStackTrace();
        }

    }

    @FXML
    private void initialize() {
        if (initialized) return;

        System.out.println("Initializing LocationController");


        initialized = true;
    }


    @Override
    public void onEnter() {
        super.onEnter();
        // Refresh data when entering this screen
        refreshLocationData();
    }

    private void refreshLocationData() {
        System.out.println("Refreshing location data");
        // Load/reload location data into the table
    }
}