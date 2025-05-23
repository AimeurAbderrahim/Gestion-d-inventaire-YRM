package stateMachin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

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
            String username = usernameField.getText();
            String password = passwordField.getText();

            // Here you would normally validate credentials
            System.out.println("Login attempt with username: " + username);

            // Check if state machine is available
            if (stateMachine != null) {
                System.out.println("Changing scene to Location");
                stateMachine.changeScene(EnumScenes.Welcome, event);
                System.out.println("Scene change initiated");
            } else {
                System.err.println("ERROR: stateMachine is null in LoginController");
            }
        } catch (Exception e) {
            System.err.println("Exception during login: " + e.getMessage());
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

    @Override
    @FXML
    public void ProduitButtonSwitch(ActionEvent event) {} // no button switch here but we need to declare empty block because this is not abstract class
    @Override
    @FXML
    public void FournisseurButtonSwitch(ActionEvent event) {} // no button switch here but we need to declare empty block because this is not abstract class
    @Override
    @FXML
    public void BonsButtonSwitch(ActionEvent event) {} // no button switch here but we need to declare empty block because this is not abstract class
    @Override
    @FXML
    public void EmplacementButtonSwitch(ActionEvent event) {} // no button switch here but we need to declare empty block because this is not abstract class
}
