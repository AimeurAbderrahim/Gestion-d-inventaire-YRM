package stateMachin;

import db.configuration.ConfigDatabase;
import db.java.FournisseurDatabase;
import db.utils.AuthunticationVerification;
import db.utils.CreateAccount;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import testpackage.model.core.Fournisseur;
import testpackage.model.enumeration.Roles;

public class LoginController extends BaseController {
    @FXML private Label ConnexionLabel;
    @FXML private AnchorPane ConnexionStyle;
    @FXML private ImageView Background;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button connexionButton;

    private boolean initialized = false;

    // Default constructor for FXML loader
    public LoginController() {
        super();
        System.out.println("LoginController constructed by FXML loader");
    }

    @FXML
    private void initialize() {
        if (initialized) return;

        System.out.println("Initializing LoginController");

        // Check if UI elements are properly loaded
        System.out.println("connexionButton: " + (connexionButton != null ? "found" : "NOT FOUND"));
        System.out.println("usernameField: " + (usernameField != null ? "found" : "NOT FOUND"));
        System.out.println("passwordField: " + (passwordField != null ? "found" : "NOT FOUND"));

        initialized = true;
    }
    @FXML
    public void login(ActionEvent event) {
        System.out.println("Login method called");

        try {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            // Check empty fields first
            if (username.isEmpty() || password.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Invalid Credentials");

                if (username.isEmpty() && password.isEmpty()) {
                    alert.setHeaderText("Veuillez entrer votre nom d'utilisateur et votre mot de passe.");
                } else if (username.isEmpty()) {
                    alert.setHeaderText("Veuillez entrer votre nom d'utilisateur.");
                } else {
                    alert.setHeaderText("Veuillez entrer votre mot de passe.");
                }

                alert.showAndWait();
                return;
            }

            System.out.println("Login attempt with username: " + username);
            AuthunticationVerification auth = new AuthunticationVerification(username, password);

            if (auth.checkAuth()) {
                if (stateMachine != null) {
                    System.out.println("Welcome " + username);
                    System.out.println("Changing scene to Welcome");
                    stateMachine.changeScene(EnumScenes.Welcome, event);
                    System.out.println("Scene change initiated");
                } else {
                    System.err.println("ERROR: stateMachine is null in LoginController");
                }
            } else {
                System.out.println(password);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Échec de l'authentification");
                alert.setHeaderText("Nom d'utilisateur ou mot de passe incorrect.");
                alert.showAndWait();
            }
        } catch (Exception e) {
            System.err.println("Exception during login: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void forgotPasseword(ActionEvent event) {

    }
    @FXML
    private void newAccount(ActionEvent event) {
        try{
            System.out.println("Opening popup for adding Fournisseur");
            Stage popupStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/stateMachin/pages/popUps/AddComptePopUp.fxml"));
            Parent popupRoot = fxmlLoader.load();
            Scene scene = new Scene(popupRoot);
            scene.setFill(Color.TRANSPARENT); // important
            popupStage.initStyle(StageStyle.TRANSPARENT);
//            popupStage.initStyle(StageStyle.UNDECORATED);  // removes window decorations
            popupStage.initModality(Modality.APPLICATION_MODAL); // Block input to other windows
            popupStage.setTitle("Popup");
            popupStage.setScene(scene);

            popupStage.showAndWait();// Use show() for non-blocking

        }catch (Exception e){
            System.err.println("Exception during call of AjouterFournisurButton" + e.getMessage());
            e.printStackTrace();
        }
    }
    @FXML private TextField NomDutilisateurField;
    @FXML private TextField MotDePasseField;
    @FXML private ComboBox<String> roleComboBox;
    @FXML
    private void newAccountDbAction(ActionEvent event) {

        String Nom = NomDutilisateurField.getText();
        String MotDePasse = MotDePasseField.getText();
        String selectedRole = roleComboBox.getValue();
        if (selectedRole == null || selectedRole.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Rôle manquant");
            alert.setHeaderText("Veuillez sélectionner un rôle, s'il vous plaît.");
            alert.showAndWait();
            return;
        }
        Roles role;
        switch (selectedRole) {
            case "ADMINISTRATEUR":
                role = Roles.ADMINISTRATEUR;
                break;
            case "MAGASINIER":
                role = Roles.MAGASINIER;
                break;
            case "SECRETAIRE":
                role = Roles.SECRETAIRE;
                break;
            case "CLIENT":
                role = Roles.CLIENT;
                break;
            default:
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Rôle invalide");
                alert.setHeaderText("Le rôle sélectionné n'est pas valide.");
                alert.showAndWait();
                return;
        }
        try{
            ConfigDatabase db = new ConfigDatabase();
            db.getConnection();

            try {
                CreateAccount Account = new CreateAccount(Nom,MotDePasse,role);
                Account.SignUp();
                Stage stage = (Stage) closePopup.getScene().getWindow();
                stage.close();
                db.closeConnection();

            } catch (Exception e) {
                System.err.println("Failed to connect to database: " + e.getMessage());
            }

        }catch (Exception e){
            System.err.println("Exception during closing popup " + e.getMessage());
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


    @Override
    public void onEnter() {
        super.onEnter();
        // Reset fields when entering login screen
        if (usernameField != null) {
            usernameField.clear();
            passwordField.clear();
        }
    }
}
