package stateMachin;

import db.configuration.ConfigDatabase;
import db.java.CompteDatabase;
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
import stateMachin.setting.AddCompteController;
import stateMachin.setting.ChangePasswordController;
import stateMachin.setting.EditProfileController;
import testpackage.model.core.Compte;
import testpackage.model.core.Personne;

import java.io.IOException;

import java.time.LocalDate;
import java.util.List;

public class WelcomeController extends BaseController {
	public TextField nomDutilisateurField;
	public TextField motDePasseField;
	public ComboBox<String> RoleCombobox;
	@FXML
	private TableView<Compte> CompteTable;

	private final ObservableList<Compte> Comptedata = FXCollections.observableArrayList();
	private boolean initialized = false;
	public WelcomeController() {
		super();
		System.out.println("Welcome constructed by FXML loader");
	}
	@Override
	public void onEnter() {
		super.onEnter();
		// Refresh data when entering this screen
		refreshWelcomeData();
	}
	private void refreshWelcomeData() {
		System.out.println("Refreshing Welcome Page data");
		// Load/reload product data into the table
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
	private TableColumn<Compte, String> ID_Compte ;
	@FXML
	private TableColumn<Compte, String> Nom_Dutilisateur;
	@FXML
	private TableColumn<Compte, String> Mot_De_Passe;
	@FXML
	private TableColumn<Compte, String> Role;

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
	public void SettingsButtonPopUp(ActionEvent event) {
		try {
			System.out.println("Opening popup for Settings");

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/stateMachin/pages/CompteSettingsPage.fxml"));
			Parent popupRoot = loader.load();

			Stage popupStage = new Stage();
			popupStage.initStyle(StageStyle.TRANSPARENT);
			popupStage.initModality(Modality.APPLICATION_MODAL);
			popupStage.setTitle("Settings");
			popupStage.setScene(new Scene(popupRoot, Color.TRANSPARENT));
			popupStage.centerOnScreen();
			popupStage.showAndWait();

			// No need to refresh here — handled by initialize()
		} catch (Exception e) {
			System.err.println("Error opening Settings popup: " + e.getMessage());
			e.printStackTrace();
		}
	}



	private void refreshCompteData() {
		try {
			ConfigDatabase db = new ConfigDatabase();
			db.getConnection();
			CompteDatabase compteDB = new CompteDatabase(db, null, null);

			if(showingUserInfo) {
				Compte current = Session.getCurrentUser();
				Comptedata.clear();
				if(current != null) {
					Comptedata.add(current);
				} else {
					showAlert("No user logged in!");
				}
			} else {
				List<Compte> c = compteDB.findAll();
				Comptedata.clear();
				Comptedata.addAll(c);
			}

			// Force table refresh
			CompteTable.refresh();

		} catch (Exception e) {
			System.err.println("Failed to refresh data: " + e.getMessage());
		}
	}
	@FXML private Button closePopup;
	@FXML
	private void closepopup(ActionEvent event) {
		Stage stage = (Stage) closePopup.getScene().getWindow();
		stage.close();
	}

	private void ModifierOuSuprimerUnCompte(Compte compte) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/stateMachin/pages/popUps/CompteEdit.fxml")); // ✅ FIXED
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

//	@FXML private StackPane infoPanel;
//	@FXML private Label currentUsername;
//	@FXML private Label currentUserRole;
//	@FXML private Label currentUserId;

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

	// Modify initialize method
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


	// Add these class variables
	private boolean showingUserInfo = false;
	private boolean showingPersonneInfo = false;

	@FXML
	private void showCompteTable(ActionEvent event) {
		showingUserInfo = false;
		refreshCompteData();
	}

	@FXML
	private void showPersonneTable(ActionEvent event) {
		showingPersonneInfo = false;
		refreshCompteData();
	}

	@FXML
	private void showUserInfo(ActionEvent event) {
		Compte currentUser = getCurrentUser();
		if(currentUser == null) {
			showAlert("Please log in first!");
			return;
		}
		showingUserInfo = true;
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

	// Add new method for password change
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


	private Compte getCurrentUser() {
		return Session.getCurrentUser();
	}


}
