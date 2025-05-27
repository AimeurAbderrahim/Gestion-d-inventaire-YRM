package stateMachin;

import db.configuration.ConfigDatabase;
import db.java.BonDatabase;
import db.java.FournisseurDatabase;
import db.java.ProduitArticleDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import testpackage.model.core.Bon;
import testpackage.model.core.Fournisseur;
import testpackage.model.core.ProduitArticle;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class BonPopUpController {

    @FXML private TextField idBonField;
    @FXML private DatePicker dateField;
    @FXML private ComboBox<String> emplacementCombo;

    private Bon currentBon;
    private final ObservableList<String> fournisseurIds = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        try {
            ConfigDatabase db=new ConfigDatabase();
            FournisseurDatabase fournisseurDB = new FournisseurDatabase(db, null, null);
            List<Fournisseur> fournisseurs = fournisseurDB.findAll();
            fournisseurIds.addAll(fournisseurs.stream().map(Fournisseur::getRC).collect(Collectors.toList()));
            emplacementCombo.setItems(fournisseurIds);
        } catch (Exception e) {
            showError("Erreur", "Impossible de charger les fournisseurs: " + e.getMessage());
        }
    }

    public void setBon(Bon bon) {
        this.currentBon = bon;
        if (bon != null) {
            idBonField.setText(bon.getIdBon());
            dateField.setValue(bon.getDateBon().toLocalDate());
            emplacementCombo.setValue(bon.isBonReception() ? bon.getId_f() : bon.getId_emplacement());
        }
    }

    @FXML
    private void onValidate() {
        try {
            Bon bon = new Bon();
            bon.setDateBon(LocalDateTime.from(dateField.getValue().atStartOfDay()));
            bon.setType(true); // réception
            bon.setId_f(emplacementCombo.getValue());

            BonDatabase bonDB = new BonDatabase((ConfigDatabase) null, null, null);
            bonDB.add(bon);

            // ID du produit modèle lié au bon
            String idModele = bon.getReferenceId();

            ProduitArticleDatabase articleDB = new ProduitArticleDatabase((ConfigDatabase) null, null, null);
            List<ProduitArticle> articles = articleDB.findAll();

            ProduitArticle existant = articles.stream()
                    .filter(a -> a.getId_modele().equals(idModele))
                    .findFirst()
                    .orElse(null);

            if (existant != null) {
                int nouvelleQuantite = existant.getQuantite_global() + 1;
                existant.setQuantite_global(nouvelleQuantite);
                articleDB.update(existant, existant);
            } else {
                ProduitArticle article = new ProduitArticle();
                article.setId_article(articleDB.generatedIdPK());
                article.setNom_article("Article " + idModele);
                article.setQuantite_global(1);
                article.setDate_dachat(LocalDate.now());
                article.setDate_peremption(LocalDate.now().plusYears(1));
                article.setId_modele(idModele);
                articleDB.add(article);
            }

            showInfo("Succès", "Bon enregistré et article ajouté/mis à jour.");
            close();
        } catch (Exception e) {
            showError("Erreur", "Impossible d’enregistrer : " + e.getMessage());
        }
    }

    @FXML
    private void onDelete() {
        if (currentBon != null) {
            try {
                BonDatabase db = new BonDatabase((ConfigDatabase) null, null, null);
                db.remove(currentBon);
                close();
            } catch (SQLException e) {
                showError("Erreur", "Suppression impossible: " + e.getMessage());
            } catch (Exception e) {
                showError("Erreur", "Problème : " + e.getMessage());
            }
        }
    }

    @FXML
    private void onCancel() {
        close();
    }

    private void close() {
        Stage stage = (Stage) idBonField.getScene().getWindow();
        stage.close();
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showInfo(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
