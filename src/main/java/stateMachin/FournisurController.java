package stateMachin;

import java.util.List;

import db.configuration.ConfigDatabase;
import db.java.FournisseurDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.w3c.dom.events.MouseEvent;
import testpackage.model.core.Fournisseur;
import testpackage.model.errors.NotNullException;

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
    private TableView<Fournisseur> fournisseurTable;

    @FXML
    private TableColumn<Fournisseur, String> colNom;
    @FXML
    private TableColumn<Fournisseur, String> colAdresse;
    @FXML
    private TableColumn<Fournisseur, String> colNumTel;
    @FXML
    private TableColumn<Fournisseur, String> colMail;
    @FXML
    private TableColumn<Fournisseur, String> colNIF;
    @FXML
    private TableColumn<Fournisseur, String> colNIS;
    @FXML
    private TableColumn<Fournisseur, String> colRC;
    @FXML
    private Button modifierFournisseurDbButton;

    private final ObservableList<Fournisseur> fournisseurData = FXCollections.observableArrayList();
    private Fournisseur fournisseurToEdit = null;

    @FXML
    private void initialize() {

        try {

            // already initialised for this *instance*?
            if (initialized) return;
            initialized = true;

            /* ── only the main screen has the table ── */
            if (fournisseurTable != null) {
                colNom.setCellValueFactory(new PropertyValueFactory<>("nom_f"));
                colAdresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
                colNumTel.setCellValueFactory(new PropertyValueFactory<>("num_tel"));
                colMail.setCellValueFactory(new PropertyValueFactory<>("mail_f"));
                colNIF.setCellValueFactory(new PropertyValueFactory<>("NIF"));
                colNIS.setCellValueFactory(new PropertyValueFactory<>("NIS"));
                colRC.setCellValueFactory(new PropertyValueFactory<>("RC"));

                refreshFournisurData();
                fournisseurTable.setItems(fournisseurData);

                fournisseurTable.setRowFactory(tv -> {
                    TableRow<Fournisseur> row = new TableRow<>();
                    row.setOnMouseClicked(ev -> {
                        if (!row.isEmpty() && ev.getClickCount() == 2 &&
                                ev.getButton() == javafx.scene.input.MouseButton.PRIMARY) {
                            modifierFournisseur(row.getItem());
                        }
                    });
                    return row;
                });
            }



        } catch (Exception e) {
            System.err.println("Failed to initialise controller: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setFournisseurData(Fournisseur f) {

        nomField.setText(f.getNom_f());
        adresseField.setText(f.getAdresse());
        numeroField.setText(f.getNum_tel());
        emailField.setText(f.getMail_f());
        nifField.setText(f.getNIF());
        nisField.setText(f.getNIS());
        rcField.setText(f.getRC());
    }

    @FXML
    private void modifierFournisseur(Fournisseur f) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/stateMachin/pages/popUps/EditFournisseurPopUP.fxml"));
            Parent popupRoot = loader.load();

            FournisurController popupController = loader.getController();
            popupController.setFournisseurData(f);
            popupController.fournisseurToEdit = f;

            Stage popupStage = new Stage();
            popupStage.initStyle(StageStyle.UNDECORATED);
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Modifier Fournisseur");
            popupStage.setScene(new Scene(popupRoot));

            /* --------- BLOCKING call --------- */
            popupStage.showAndWait();
            /* --------------------------------- */

            // when we come back, refresh the visible table
            refreshFournisurData();

        } catch (Exception e) {
            System.err.println("Error loading popup for editing: " + e.getMessage());
            e.printStackTrace();
        }
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

    @FXML private TextField nomField;
    @FXML private TextField adresseField;
    @FXML private TextField numeroField;
    @FXML private TextField emailField;
    @FXML private TextField nifField;
    @FXML private TextField nisField;
    @FXML private TextField rcField;

    @FXML
    private void AjouterFournisseurDbAction(ActionEvent event) {

        String Nom = nomField.getText();
        String Adresse = adresseField.getText();
        String NumTel = numeroField.getText();
        String Mail = emailField.getText();
        String NIF = nifField.getText();
        String NIS = nisField.getText();
        String RC = rcField.getText();

        try{
            ConfigDatabase db = new ConfigDatabase();
            db.getConnection();

            Fournisseur fournisseur = new Fournisseur(Nom, Adresse, NumTel, Mail, NIF, NIS, RC);
            try {
                FournisseurDatabase fournisseurDB = new FournisseurDatabase(db, null , null);
                fournisseurDB.add(fournisseur);
                Stage stage = (Stage) closePopup.getScene().getWindow();
                stage.close();
                refreshFournisurData();
                db.closeConnection();

            } catch (Exception e) {
                System.err.println("Failed to connect to database: " + e.getMessage());
            }

        }catch (Exception e){
            System.err.println("Exception during closing popup " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void SupprimerFourniseurDbAction(ActionEvent event)  {
        try{
            ConfigDatabase db = new ConfigDatabase();
            db.getConnection();
            try {
                FournisseurDatabase fournisseurDB = new FournisseurDatabase(db, null , null);
                fournisseurDB.remove(fournisseurToEdit);
                Stage stage = (Stage) closePopup.getScene().getWindow();
                stage.close();
                refreshFournisurData();
                db.closeConnection();

            } catch (Exception e) {
                System.err.println("Failed to connect to database: " + e.getMessage());
            }


        }catch (Exception e){
            System.err.println("Exception during closing popup " + e.getMessage());
            e.printStackTrace();
        }

    }

    @FXML
    private void ModifierFournisseurDbAction(ActionEvent event) {


        String Nom = nomField.getText();
        String Adresse = adresseField.getText();
        String NumTel = numeroField.getText();
        String Mail = emailField.getText();
        String NIF = nifField.getText();
        String NIS = nisField.getText();
        String RC = rcField.getText();

        try{
            ConfigDatabase db = new ConfigDatabase();
            db.getConnection();

            Fournisseur fournisseur = new Fournisseur(Nom, Adresse, NumTel, Mail, NIF, NIS, RC);
            try {
                FournisseurDatabase fournisseurDB = new FournisseurDatabase(db, null , null);
                System.out.println("Updating fournisseur: " + fournisseurToEdit);

                fournisseurDB.update(fournisseurToEdit,fournisseur);
                Stage stage = (Stage) closePopup.getScene().getWindow();
                stage.close();
                refreshFournisurData();
                db.closeConnection();

            } catch (Exception e) {
                System.err.println("Failed to connect to database: " + e.getMessage());
            }


        }catch (Exception e){
            System.err.println("Exception during closing popup " + e.getMessage());
            e.printStackTrace();
        }
    }





    @FXML
    private void AjouterFournisurButton(ActionEvent event) {
        try{
            System.out.println("Opening popup for adding Fournisseur");
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/stateMachin/pages/popUps/addFournisseurPopup.fxml"));
            Parent popupRoot = fxmlLoader.load();

            Stage popupStage = new Stage();

            popupStage.initStyle(StageStyle.UNDECORATED);  // removes window decorations

            popupStage.initModality(Modality.APPLICATION_MODAL); // Block input to other windows
            popupStage.setTitle("Popup");
            popupStage.setScene(new Scene(popupRoot));

            popupStage.showAndWait();// Use show() for non-blocking
            refreshFournisurData();

        }catch (Exception e){
            System.err.println("Exception during call of AjouterFournisurButton" + e.getMessage());
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

        try {
            ConfigDatabase db = new ConfigDatabase();
            db.getConnection();
            FournisseurDatabase fournisseurDB = new FournisseurDatabase(db, null , null);

            List<Fournisseur> f = fournisseurDB.findAll();
            System.out.println("testing data connection");

            fournisseurData.clear();       // Clear old data
            fournisseurData.addAll(f);     // Add updated list

        } catch (Exception e) {
            System.err.println("Failed to refresh data: " + e.getMessage());
        }
    }


}
