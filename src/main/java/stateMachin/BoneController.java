package stateMachin;

import db.configuration.ConfigDatabase;
import db.java.BonDatabase;
import db.java.ProduitArticleDatabase;
import db.java.ProduitModeleDatabase;
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
import testpackage.model.core.Bon;
import testpackage.model.core.ProduitArticle;
import testpackage.model.core.ProduitModel;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class BoneController extends BaseController {

    private boolean initialized = false;

    @FXML private TableView<ProduitModelSelection> produitTable;
    @FXML private TableColumn<ProduitModelSelection, String> colDesignation;
    @FXML private TableColumn<ProduitModelSelection, Integer> colQuantite;
    @FXML private ComboBox<ProduitModel> produitCombo;
    @FXML private TextField quantiteField;
    @FXML private ComboBox<String> fournisseurCombo;
    @FXML private DatePicker dateBon;
    @FXML private Button confirmButton;
    @FXML private Button cancelButton;

    private final ObservableList<ProduitModelSelection> produitsSelectionnes = FXCollections.observableArrayList();
    private final ObservableList<ProduitModel> produitsDisponibles = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        if (initialized) return;
        initialized = true;

        try {
            ProduitModeleDatabase modeleDB = new ProduitModeleDatabase(null, null);
            produitsDisponibles.addAll(modeleDB.findAll());
            produitCombo.setItems(produitsDisponibles);

            confirmButton.setOnAction(e -> validerBon());
            cancelButton.setOnAction(e -> fermer());
        } catch (Exception e) {
            showError("Erreur de chargement", e.getMessage());
        }
    }

    private void validerBon() {
        try {
            Bon bon = new Bon();
            bon.setDateBon(dateBon.getValue().atStartOfDay());
            bon.setType(true); // réception
            bon.setId_f(fournisseurCombo.getValue());

            ConfigDatabase db = new ConfigDatabase();
            BonDatabase bonDB = new BonDatabase(db, null, null);
            bonDB.add(bon);

            ProduitArticleDatabase articleDB = new ProduitArticleDatabase(db, null, null);
            for (ProduitModelSelection sel : produitsSelectionnes) {
                ProduitArticle article = new ProduitArticle();
                article.setId_article(articleDB.generatedIdPK());
                article.setNom_article(sel.getProduit().getDesignation());
                article.setQuantite_global(sel.getQuantite());
                article.setDate_dachat(LocalDate.now());
                article.setDate_peremption(LocalDate.now().plusYears(1));
                article.setId_modele(sel.getProduit().getId_modele());
                articleDB.add(article);
            }

            showInfo("Succès", "Bon et produits ajoutés.");
            fermer();

        } catch (SQLException | db.errors.LoadPropertiesException e) {
            showError("Erreur DB", e.getMessage());
        }
    }

    private void fermer() {
        Stage stage = (Stage) confirmButton.getScene().getWindow();
        stage.close();
    }

    private void showError(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(title);
        a.setContentText(msg);
        a.showAndWait();
    }

    private void showInfo(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setContentText(msg);
        a.showAndWait();
    }

    public static class ProduitModelSelection {
        private final ProduitModel produit;
        private final javafx.beans.property.SimpleIntegerProperty quantite = new javafx.beans.property.SimpleIntegerProperty();

        public ProduitModelSelection(ProduitModel produit, int quantite) {
            this.produit = produit;
            this.quantite.set(quantite);
        }

        public ProduitModel getProduit() { return produit; }
        public int getQuantite() { return quantite.get(); }

        public javafx.beans.property.SimpleStringProperty designationProperty() {
            return new javafx.beans.property.SimpleStringProperty(produit.getDesignation());
        }

        public javafx.beans.property.SimpleIntegerProperty quantiteProperty() {
            return quantite;
        }
    }

    @FXML private void ProduitButtonSwitch(ActionEvent event) {
        stateMachine.changeScene(EnumScenes.Products, event);
    }

    @FXML private void FournisseurButtonSwitch(ActionEvent event) {
        stateMachine.changeScene(EnumScenes.Fournisur, event);
    }

    @FXML private void BonsButtonSwitch(ActionEvent event) {
        stateMachine.changeScene(EnumScenes.Bone, event);
    }

    @FXML private void EmplacementButtonSwitch(ActionEvent event) {
        stateMachine.changeScene(EnumScenes.Location, event);
    }

    @FXML private void BondeSortieButtonSwitch(ActionEvent event) {
        stateMachine.changeScene(EnumScenes.Bondesortie, event);
    }

    @FXML private void SettingsButton(ActionEvent event) {
        try {
            WelcomeController welcome = (WelcomeController) stateMachine.controllers.get(EnumScenes.Welcome);
            if (welcome != null) {
                welcome.SettingsButtonPopUp(event);
            } else {
                System.err.println("WelcomeController non trouvé");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void ajouter(ActionEvent event){
        try {
            System.out.println("Opening popup for adding a bon de recetpion");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/stateMachin/pages/popUps/BonePopUp.fxml"));
            Parent popupRoot = loader.load();

            Stage popupStage = new Stage();
            popupStage.initStyle(StageStyle.TRANSPARENT);
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("ajouter");
            popupStage.setScene(new Scene(popupRoot, Color.TRANSPARENT));
            popupStage.centerOnScreen();
            popupStage.showAndWait();

            // No need to refresh here — handled by initialize()
        } catch (Exception e) {
            System.err.println("Error opening ajouter popup: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
