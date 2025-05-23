package stateMachin;//package stateMachin;
//import javafx.application.Platform;
//import javafx.fxml.FXML;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.control.PasswordField;
//import javafx.scene.control.TextField;
//import javafx.scene.layout.AnchorPane;
//
//public class LoginPageController extends BaseController {
//    @FXML private Label ConnexionLabel;
//    @FXML private AnchorPane ConnexionStyle;
//    @FXML private TextField usernameField;
//    @FXML private PasswordField passwordField;
//    @FXML private Button connexionButton;
//
//    private boolean initialized = false;
//
//    public LoginPageController(ControllerStateMachine stateMachine) {
//        super(stateMachine);
//    }
//
//    @Override
//    protected void loadFXML() {
//        root = FXMLLoaderUtil.loadFXML("/stateMachin/pages/LoginPage.fxml", this);
//        initialize();
//    }
//
//    private void initialize() {
//        if (initialized) return;
//
//        // Setup button actions
//        if (connexionButton != null) {
//            connexionButton.setOnAction(e -> login());
//        } else {
//            System.err.println("Warning: connexionButton not found in FXML");
//        }
//
//        initialized = true;
//    }
//
//    private void login() {
//        String username = usernameField.getText();
//        String password = passwordField.getText();
//
//        // Here you would normally validate credentials
//        System.out.println("Login attempt with username: " + username);
//
//        // For demonstration, navigate to location screen upon successful login
//        LocationController locationController =
//                (LocationController) stateMachine.getController(LocationController.class);
//        stateMachine.changeState(locationController);
//    }
//
//    @Override
//    public void onEnter() {
//        super.onEnter();
//        // Reset fields when entering login screen
//        Platform.runLater(() -> {
//            if (usernameField != null) {
//                usernameField.clear();
//                passwordField.clear();
//                // Set focus to username field
//                usernameField.requestFocus();
//            }
//        });
//    }
//}