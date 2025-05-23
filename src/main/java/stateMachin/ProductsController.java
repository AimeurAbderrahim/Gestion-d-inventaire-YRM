package stateMachin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

public class ProductsController extends BaseController {
    @FXML private AnchorPane productsPane;
    @FXML private TableView<?> productsTable;
    @FXML private Button addButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;
    @FXML private Button backButton;

    private boolean initialized = false;

    // Default constructor for FXML loader
    public ProductsController() {
        super();
    }

    // Constructor for manual instantiation
    public ProductsController(ControllerStateMachine stateMachine) {
        super(stateMachine);
    }


    @Override
    public void onEnter() {
        super.onEnter();
        // Refresh data when entering this screen
        refreshProductData();
    }

    private void refreshProductData() {
        System.out.println("Refreshing product data");
        // Load/reload product data into the table
    }
    @Override
    @FXML
    protected void ProduitButtonSwitch(ActionEvent event) {
        try{
            System.out.println("Changing scene to Products");
            stateMachine.changeScene(EnumScenes.Products, event);
            System.out.println("Scene change initiated");

        }catch (Exception e){
            System.err.println("Exception during switching " + e.getMessage());
            e.printStackTrace();
        }

    }
    @Override
    @FXML
    protected void FournisseurButtonSwitch(ActionEvent event) {
        try{
            System.out.println("Changing scene to Fournisseur");
            stateMachine.changeScene(EnumScenes.Fournisur, event);
            System.out.println("Scene change initiated");

        }catch (Exception e){
            System.err.println("Exception during switching " + e.getMessage());
            e.printStackTrace();
        }

    }
    @Override
    @FXML
    protected void BonsButtonSwitch(ActionEvent event) {
        try{
            System.out.println("Changing scene to bons");
            stateMachine.changeScene(EnumScenes.Bone, event);
            System.out.println("Scene change initiated");

        }catch (Exception e){
            System.err.println("Exception during switching " + e.getMessage());
            e.printStackTrace();
        }

    }
    @Override
    @FXML
    protected void EmplacementButtonSwitch(ActionEvent event) {
        try{
            System.out.println("Changing scene to Emplacement");
            stateMachine.changeScene(EnumScenes.Location, event);
            System.out.println("Scene change initiated");

        }catch (Exception e){
            System.err.println("Exception during switching " + e.getMessage());
            e.printStackTrace();
        }

    }
}
