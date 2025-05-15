package stateMachin;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController extends BaseController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button connexionButton;

    public LoginController() {
        super(null); // Will be set after via setter
    }

    public void setStateMachine(ControllerStateMachine stateMachine) {
        this.stateMachine = stateMachine;
    }

    @FXML
    private void initialize() {
        connexionButton.setOnAction(e -> handleLogin());
    }

    private void handleLogin() {
        System.out.println("Login: " + usernameField.getText());

        ProductController next = new ProductController(stateMachine);
        stateMachine.changeState(next);
    }

    @Override
    public Parent getRoot() {
        return null;
    }
}
