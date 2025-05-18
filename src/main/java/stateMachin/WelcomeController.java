package stateMachin;

public class WelcomeController extends BaseController {
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
}
//