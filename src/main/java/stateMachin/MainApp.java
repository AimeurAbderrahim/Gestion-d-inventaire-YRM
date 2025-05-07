package stateMachin;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MainApp extends Application {
    private ControllerStateMachine stateMachine;

    @Override
    public void start(Stage stage) throws Exception {
        System.setProperty("prism.forceGPU", "true");
        System.setProperty("javafx.animation.fullspeed", "true");

        Platform.runLater(() -> {
            Font.loadFont(getClass().getResourceAsStream("/fonts/System.font"), 10);
        });

        stage.setTitle("Login");

        stateMachine = new ControllerStateMachine(stage);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/testpackage/gestiondinventaireyrm/pages/LoginPage.fxml"));
        Scene scene = new Scene(loader.load());

        LoginController controller = loader.getController();
        controller.setStateMachine(stateMachine); // Inject state machine

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() {
        Platform.exit();
    }
}
