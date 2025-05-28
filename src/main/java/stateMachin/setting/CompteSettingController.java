package stateMachin.setting;

import db.configuration.ConfigDatabase;
import db.java.CompteDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import stateMachin.BaseController;
import testpackage.model.core.Compte;

import java.io.IOException;
import java.util.List;

import static stateMachin.Session.getCurrentUser;

public class CompteSettingController extends BaseController {

    @FXML
    private TableColumn<Compte, String> ID_Compte ;
    @FXML
    private TableColumn<Compte, String> Nom_Dutilisateur;
    @FXML
    private TableView<Compte> CompteTable;
    @FXML
    private TableColumn<Compte, String> Role;
    @FXML
    private Button closePopup;



    private final ObservableList<Compte> Comptedata = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        System.out.println("WelcomeController initialized");
        try {
            if (CompteTable != null) {
                ID_Compte.setCellValueFactory(new PropertyValueFactory<>("id_c"));
                Nom_Dutilisateur.setCellValueFactory(new PropertyValueFactory<>("nom_utilisateur"));
                Role.setCellValueFactory(new PropertyValueFactory<>("roles"));

                refreshCompteData();
                updateCurrentUserInfo();
                CompteTable.setItems(Comptedata);

                CompteTable.setRowFactory(tv -> {
                    TableRow<Compte> row = new TableRow<>();
                    row.setOnMouseClicked(ev -> {
                        if (!row.isEmpty() && ev.getClickCount() == 2 &&
                                ev.getButton() == javafx.scene.input.MouseButton.PRIMARY) {
                            ModifierOuSuprimerUnCompte(row.getItem());
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
    @FXML
    private void goToPersonneScene(ActionEvent event) {
        {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/stateMachin/pages/PersonSettingsPage.fxml"));
                Parent newRoot = loader.load();

                Scene currentScene = closePopup.getScene();
                Stage stage = (Stage) currentScene.getWindow();
                stage.setScene(new Scene(newRoot));

                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Erreur lors du changement de popup vers la création de compte");
            }
        }


    }
    private void refreshCompteData() {
        try {
            ConfigDatabase db = new ConfigDatabase();
            db.getConnection();
            CompteDatabase compteDB = new CompteDatabase(db, null, null);


                List<Compte> c = compteDB.findAll();
                Comptedata.clear();
                Comptedata.addAll(c);


            // Force table refresh
            CompteTable.refresh();

        } catch (Exception e) {
            System.err.println("Failed to refresh data: " + e.getMessage());
        }
    }

    @FXML
    private void closepopup(ActionEvent event) {
        Stage stage = (Stage) closePopup.getScene().getWindow();
        stage.close();
    }
    private void ModifierOuSuprimerUnCompte(Compte compte) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/stateMachin/pages/popUps/CompteEdit.fxml"));
            Parent popupRoot = loader.load();

            AddCompteController controller = loader.getController();
            controller.setEditMode(compte);

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initStyle(StageStyle.TRANSPARENT);
            popupStage.setScene(new Scene(popupRoot));
            popupStage.showAndWait();

            refreshCompteData();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void AjouterCompteButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/stateMachin/pages/popUps/AddComptePopUp.fxml"));
            Parent popupRoot = loader.load();

            AddCompteController controller = loader.getController(); // no need to call anything

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initStyle(StageStyle.TRANSPARENT);
            popupStage.setScene(new Scene(popupRoot));
            popupStage.showAndWait();

            refreshCompteData();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void updateCurrentUserInfo() {
        Compte currentUser = getCurrentUser();
        if(currentUser != null) {
            // currentUsername.setText("Username: " + currentUser.getNom_utilisateur());
            System.out.println(currentUser.getNom_utilisateur());
            // currentUserRole.setText("Role: " + currentUser.getRoles().toString());
            System.out.println(currentUser.getRoles().toString());
            // currentUserId.setText("ID: " + currentUser.getId_c());
            System.out.println(currentUser.getId_c());
        }
    }
    @FXML
    private void showPersonneTable(ActionEvent event) {

        refreshCompteData();
    }
    @FXML
    private void editCurrentUser(ActionEvent event) {
        Compte currentUser = getCurrentUser();
        if(currentUser == null) {
            showAlert("Please log in first!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/stateMachin/pages/popUps/EditProfilePopUp.fxml"));
            Parent popupRoot = loader.load();

            EditProfileController controller = loader.getController();
            controller.setCurrentUser(currentUser);

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initStyle(StageStyle.TRANSPARENT);
            popupStage.setScene(new Scene(popupRoot));
            popupStage.showAndWait();

            refreshCompteData();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error loading edit profile window");
        }
    }
    @FXML
    private void changePassword(ActionEvent event) {
        Compte currentUser = getCurrentUser();
        if(currentUser == null) {
            showAlert("Please log in first!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/stateMachin/pages/popUps/ChangePasswordPopUp.fxml"));
            Parent popupRoot = loader.load();

            ChangePasswordController controller = loader.getController();
            controller.setCurrentUser(currentUser);

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initStyle(StageStyle.TRANSPARENT);
            popupStage.setScene(new Scene(popupRoot));
            popupStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error loading password change window");
        }
    }
}
