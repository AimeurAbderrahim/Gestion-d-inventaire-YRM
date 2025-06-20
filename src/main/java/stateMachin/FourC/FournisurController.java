package stateMachin.FourC;

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
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import stateMachin.BaseController;
import stateMachin.EnumScenes;
import stateMachin.WelcomeController;
import testpackage.model.core.Fournisseur;

import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

public class FournisurController extends BaseController {
    private static final Logger LOGGER = Logger.getLogger(FournisurController.class.getName());
    private boolean initialized = false;

    @FXML private TableView<Fournisseur> fournisseurTable;
    @FXML private TableColumn<Fournisseur, String> colNom;
    @FXML private TableColumn<Fournisseur, String> colAdresse;
    @FXML private TableColumn<Fournisseur, String> colNumTel;
    @FXML private TableColumn<Fournisseur, String> colMail;
    @FXML private TableColumn<Fournisseur, String> colNIF;
    @FXML private TableColumn<Fournisseur, String> colNIS;
    @FXML private TableColumn<Fournisseur, String> colRC;
    @FXML private TextField searchField;

    private final ObservableList<Fournisseur> fournisseurData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        if (initialized) return;
        initialized = true;

        if (fournisseurTable != null) {
            colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
            colAdresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
            colNumTel.setCellValueFactory(new PropertyValueFactory<>("numero_tlph"));
            colMail.setCellValueFactory(new PropertyValueFactory<>("email"));
            colNIF.setCellValueFactory(new PropertyValueFactory<>("NIF"));
            colNIS.setCellValueFactory(new PropertyValueFactory<>("NIS"));
            colRC.setCellValueFactory(new PropertyValueFactory<>("RC"));

            fournisseurTable.setItems(fournisseurData);
            
            // Add search functionality
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == null || newValue.isEmpty()) {
                    fournisseurTable.setItems(fournisseurData);
                } else {
                    ObservableList<Fournisseur> filteredList = FXCollections.observableArrayList();
                    for (Fournisseur fournisseur : fournisseurData) {
                        if (matchesSearch(fournisseur, newValue.toLowerCase())) {
                            filteredList.add(fournisseur);
                        }
                    }
                    fournisseurTable.setItems(filteredList);
                }
            });
            
            setDoubleClickAction();
            refreshData();
        }
    }

    private boolean matchesSearch(Fournisseur fournisseur, String searchText) {
        return fournisseur.getNom().toLowerCase().contains(searchText) ||
               fournisseur.getAdresse().toLowerCase().contains(searchText) ||
               fournisseur.getNumero_tlph().toLowerCase().contains(searchText) ||
               fournisseur.getEmail().toLowerCase().contains(searchText) ||
               fournisseur.getNIF().toLowerCase().contains(searchText) ||
               fournisseur.getNIS().toLowerCase().contains(searchText) ||
               fournisseur.getRC().toLowerCase().contains(searchText);
    }

    private void setDoubleClickAction() {
        fournisseurTable.setRowFactory(tv -> {
            TableRow<Fournisseur> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) {
                    openEditPopup(row.getItem());
                }
            });
            return row;
        });
    }

    private void openEditPopup(Fournisseur fournisseur) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/stateMachin/pages/popUps/EditFournisseurPopUP.fxml"));
            Parent root = loader.load();

            EditFournisseurController controller = loader.getController();
            controller.setFournisseur(fournisseur);

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Modifier Fournisseur");
            stage.setScene(scene);
            stage.showAndWait();

            refreshData();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur lors de l'ouverture du popup.");
        }
    }

    @Override
    public void onEnter() {
        super.onEnter();
        refreshData();
    }

    private void refreshData() {
        try {
            ConfigDatabase configDB = new ConfigDatabase();
            FournisseurDatabase fournisseurDB = new FournisseurDatabase(configDB, null, null);
            List<Fournisseur> fournisseurs = fournisseurDB.findAll();
            fournisseurData.clear();
            fournisseurData.addAll(fournisseurs);
            LOGGER.info("Loaded " + fournisseurs.size() + " fournisseurs");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading fournisseurs", e);
            showAlert("Error loading fournisseurs: " + e.getMessage());
        }
    }

    @FXML
    private void AjouterFournisurButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/stateMachin/pages/popUps/addFournisseurPopup.fxml"));
            Parent popupRoot = loader.load();

            Stage popupStage = new Stage();
            Scene scene = new Scene(popupRoot);
            scene.setFill(Color.TRANSPARENT);
            popupStage.initStyle(StageStyle.TRANSPARENT);
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Ajouter un Fournisseur");
            popupStage.setScene(scene);
            popupStage.showAndWait();

            refreshData();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur lors de l'ajout.");
        }
    }

    @FXML
    private void SettingsButton(ActionEvent event) {
        try {
            WelcomeController welcome = (WelcomeController) stateMachine.controllers.get(EnumScenes.Welcome);
            if (welcome != null) {
                welcome.SettingsButtonPopUp(event);
            } else {
                System.err.println("WelcomeController non trouvé");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Navigation boutons
    @FXML private void ProduitButtonSwitch(ActionEvent event) {
        stateMachine.changeScene(EnumScenes.Products, event);
    }

    @FXML private void FournisseurButtonSwitch(ActionEvent event) {
        stateMachine.changeScene(EnumScenes.Fournisur, event);
    }

    @FXML private void BonsButtonSwitch(ActionEvent event) {
        stateMachine.changeScene(EnumScenes.Bone, event);
    }

    @FXML private void EmplacementButtonSwitch(ActionEvent event) {
        stateMachine.changeScene(EnumScenes.Location, event);
    }


}
