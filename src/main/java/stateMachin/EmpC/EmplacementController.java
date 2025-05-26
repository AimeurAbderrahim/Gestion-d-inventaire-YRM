package stateMachin.EmpC;

import db.configuration.ConfigDatabase;
import db.java.EmplacementDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import stateMachin.BaseController;
import stateMachin.EnumScenes;
import stateMachin.WelcomeController;
import testpackage.model.core.Emplacement;

import java.util.List;

public class EmplacementController extends BaseController {

    @FXML private TextField searchField;
    @FXML private TableView<Emplacement> emplacementTable;
    @FXML private TableColumn<Emplacement, String> colId;
    @FXML private TableColumn<Emplacement, String> colTypeSalle;
    @FXML private TableColumn<Emplacement, Double> colSuperficie;
    @FXML private TableColumn<Emplacement, Integer> colBureau;
    @FXML private TableColumn<Emplacement, String> colNomService;

    private final ObservableList<Emplacement> emplacementData = FXCollections.observableArrayList();
    private Emplacement selected = null;

    @FXML
    private void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id_emplacement"));
        colTypeSalle.setCellValueFactory(new PropertyValueFactory<>("type_salle"));
        colSuperficie.setCellValueFactory(new PropertyValueFactory<>("superficie"));
        colBureau.setCellValueFactory(new PropertyValueFactory<>("bureau"));
        colNomService.setCellValueFactory(new PropertyValueFactory<>("nom_service"));

        emplacementTable.setItems(emplacementData);
        emplacementTable.setRowFactory(tv -> {
            TableRow<Emplacement> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    selected = row.getItem();
                    if (event.getClickCount() == 2) {
                        onEdit(selected);
                    }
                }
            });
            return row;
        });
        refreshData();
    }

    @FXML
    private void onSearch() {
        String keyword = searchField.getText().trim();
        try {
            EmplacementDatabase db = new EmplacementDatabase(new ConfigDatabase(), null, null);
            List<Emplacement> list = keyword.isEmpty() ? db.findAll() : db.search(keyword);
            emplacementData.clear();
            emplacementData.addAll(list);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur lors de la recherche : " + e.getMessage());
        }
    }

    @FXML
    private void onAdd(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/stateMachin/pages/popUps/AddEmplacement.fxml"));
            Parent popupRoot = loader.load();

            Stage popupStage = new Stage();
            popupStage.setScene(new Scene(popupRoot));
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initStyle(StageStyle.UTILITY);
            popupStage.setTitle("Ajouter un emplacement");
            popupStage.showAndWait();

            refreshData();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur lors de l'ajout : " + e.getMessage());
        }
    }

    @FXML
    private void onEdit(ActionEvent event) {
        if (selected != null) {
            onEdit(selected);
        } else {
            showAlert("Veuillez sélectionner un emplacement à modifier.");
        }
    }

    private void onEdit(Emplacement emplacement) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/stateMachin/pages/popUps/AddEmplacement.fxml"));
            Parent popupRoot = loader.load();

            AddEmplacementController controller = loader.getController();
            controller.setEditMode(emplacement);

            Stage popupStage = new Stage();
            popupStage.setScene(new Scene(popupRoot));
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initStyle(StageStyle.UTILITY);
            popupStage.setTitle("Modifier un emplacement");
            popupStage.showAndWait();

            refreshData();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur lors de la modification : " + e.getMessage());
        }
    }

    @FXML
    private void onDelete(ActionEvent event) {
        if (selected != null) {
            try {
                EmplacementDatabase db = new EmplacementDatabase(new ConfigDatabase(), null, null);
                db.remove(selected);
                refreshData();
                selected = null;
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Erreur lors de la suppression : " + e.getMessage());
            }
        } else {
            showAlert("Veuillez sélectionner un emplacement à supprimer.");
        }
    }

    private void refreshData() {
        try {
            EmplacementDatabase db = new EmplacementDatabase(new ConfigDatabase(), null, null);
            emplacementData.clear();
            emplacementData.addAll(db.findAll());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur de chargement des emplacements : " + e.getMessage());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    @FXML
    private void ProduitButtonSwitch(ActionEvent event) {
        stateMachine.changeScene(EnumScenes.Products, event);
    }

    @FXML
    private void FournisseurButtonSwitch(ActionEvent event) {
        stateMachine.changeScene(EnumScenes.Fournisur, event);
    }

    @FXML
    private void BonsButtonSwitch(ActionEvent event) {
        stateMachine.changeScene(EnumScenes.Bone, event);
    }

    @FXML
    private void EmplacementButtonSwitch(ActionEvent event) {
        stateMachine.changeScene(EnumScenes.Location, event);
    }

    @FXML
    private void SettingsButton(ActionEvent event) {
        try {
            WelcomeController welcome = (WelcomeController) stateMachine.controllers.get(EnumScenes.Welcome);
            if (welcome != null) {
                welcome.SettingsButtonPopUp(event);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
