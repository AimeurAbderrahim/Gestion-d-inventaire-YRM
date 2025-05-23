package stateMachin;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


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
    public void ProduitButtonSwitch(ActionEvent event) {
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
    public void FournisseurButtonSwitch(ActionEvent event) {
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
    public void BonsButtonSwitch(ActionEvent event) {
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
    public void EmplacementButtonSwitch(ActionEvent event) {
        try{
            System.out.println("Changing scene to Emplacement");
            stateMachine.changeScene(EnumScenes.Location, event);
            System.out.println("Scene change initiated");

        }catch (Exception e){
            System.err.println("Exception during switching " + e.getMessage());
            e.printStackTrace();
        }

    }


    // NOTE: rayden part ...
    // TODO: move this method to BaseController as abstract method if there's nothing special about her
    @FXML private Button closePopup;

    @FXML
    private void PopupCloseButton(ActionEvent event) {
        try{
            System.out.println("Closing popup");
            Stage stage = (Stage) closePopup.getScene().getWindow();
            stage.close();
            System.out.println("Popup closed");

        }catch (Exception e){
            System.err.println("Exception during closing popup " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML private TextField codeField;
    @FXML private TextField categorieField;
    @FXML private TextField typeField;
    @FXML private TextField quantityField;
    @FXML private TextField dateCommandField;

    @FXML
    private void AddProductDBAction(ActionEvent event) {


        // String Nom = nomField.getText();
        // String Adresse = adresseField.getText();
        // String NumTel = numeroField.getText();
        // String Mail = emailField.getText();
        // String NIF = nifField.getText();
        // String NIS = nisField.getText();
        // String RC = rcField.getText();

        // try{
        //     ConfigDatabase db = new ConfigDatabase();
        //     // db.getConnection();

        //     Fournisseur fournisseur = new Fournisseur(Nom, Adresse, NumTel, Mail, NIF, NIS, RC);
        //     try {	
        //         FournisseurDatabase fournisseurDB = new FournisseurDatabase(db, null , null);
        //         fournisseurDB.add(fournisseur);
        //         Stage stage = (Stage) closePopup.getScene().getWindow();
        //         stage.close();
        //         refreshFournisurData();

        //     } catch (Exception e) {
        //         System.err.println("Failed to connect to database: " + e.getMessage());
        //     }

        // }catch (Exception e){
        //     System.err.println("Exception during closing popup " + e.getMessage());
        //     e.printStackTrace();
        // }
    }
}
