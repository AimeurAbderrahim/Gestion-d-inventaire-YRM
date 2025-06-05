package stateMachin;

import db.configuration.ConfigDatabase;
import db.java.PersonneDatabase;
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
import stateMachin.setting.ChangePasswordController;
import stateMachin.setting.EditProfileController;
import stateMachin.setting.ajouterPersonneControler;
import testpackage.model.core.Compte;
import testpackage.model.core.Personne;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static stateMachin.Session.getCurrentUser;

public class PersonSettingController extends BaseController{
    @FXML
    private TableView<Personne> personneTable;
    @FXML
    private TableColumn<Personne, String> ID_Personne;
    @FXML
    private TableColumn<Personne, String> Nom;
    @FXML
    private TableColumn<Personne, String> Prenom;
    @FXML
    private TableColumn<Personne, LocalDate> DateNaissance;
    @FXML
    private TableColumn<Personne, String> Email;
    @FXML
    private TableColumn<Personne, String> Adresse;
    @FXML
    private TableColumn<Personne, String> NumTlf;
    @FXML
    private TableColumn<Personne, Boolean> AvoirComte;
    @FXML
    private Button closePopup;

    private final ObservableList<Personne> personneData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        System.out.println("PersonSettingController initialized");
        try {
            if (personneTable != null) {
                ID_Personne.setCellValueFactory(new PropertyValueFactory<>("id_p"));
                Nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
                Prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
                DateNaissance.setCellValueFactory(new PropertyValueFactory<>("localDate"));
                Email.setCellValueFactory(new PropertyValueFactory<>("email"));
                Adresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
                NumTlf.setCellValueFactory(new PropertyValueFactory<>("numero_tel_personne"));
                AvoirComte.setCellValueFactory(new PropertyValueFactory<>("avoir_compte"));

                refreshPersonneData();
                personneTable.setItems(personneData);

                personneTable.setRowFactory(tv -> {
                    TableRow<Personne> row = new TableRow<>();
                    row.setOnMouseClicked(ev -> {
                        if (!row.isEmpty() && ev.getClickCount() == 2 &&
                                ev.getButton() == javafx.scene.input.MouseButton.PRIMARY) {
                            // Implémenter ouverture popup de modification si besoin
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
    private void closepopup(ActionEvent event) {
        Stage stage = (Stage) closePopup.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void showPersonneTable(ActionEvent event) {
        refreshPersonneData();
    }

    private void refreshPersonneData() {
        try {
            ConfigDatabase db = new ConfigDatabase();
            db.getConnection();
            PersonneDatabase PersonneeDB = new PersonneDatabase(db, null, null);
            List<Personne> c = PersonneeDB.findAll();
            personneData.clear();
            personneData.addAll(c);
            personneTable.refresh();
        } catch (Exception e) {
            System.err.println("Failed to refresh data: " + e.getMessage());
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/stateMachin/pages/popUps/ChangePasswordPopUp.fxml"));
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

    @FXML
    private void gotoComptescene(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/stateMachin/pages/CompteSettingsPage.fxml"));
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

            refreshPersonneData();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error loading edit profile window");
        }
    }
    @FXML
    private void AjouterPersonButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/stateMachin/pages/popUps/ajoutPersonne.fxml"));
            Parent popupRoot = loader.load();

            ajouterPersonneControler controller = loader.getController(); // no need to call anything

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initStyle(StageStyle.TRANSPARENT);
            popupStage.setScene(new Scene(popupRoot));
            popupStage.showAndWait();

            refreshPersonneData();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
