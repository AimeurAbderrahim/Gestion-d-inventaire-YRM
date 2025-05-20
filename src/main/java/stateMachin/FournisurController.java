package stateMachin;

import javafx.event.ActionEvent;
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