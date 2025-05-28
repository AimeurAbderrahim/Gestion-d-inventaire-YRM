//package stateMachin;
//
//import db.configuration.ConfigDatabase;
//import db.java.CompteDatabase;
//import db.java.PersonneDatabase;
//import javafx.beans.property.SimpleStringProperty;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.fxml.FXML;
//import javafx.scene.control.*;
//import javafx.scene.layout.VBox;
//import testpackage.model.core.Personne;
//import testpackage.model.core.Compte;
//
//
//import java.sql.SQLException;
//import java.util.List;
//import java.util.Optional;
//
//public class SettingsController {
//
//    @FXML private Button btnComptes, btnPersonnes, btnAjouter, btnModifier, btnSupprimer;
//    @FXML private TableView<Object> mainTable;
//try{
//        ConfigDatabase db = new ConfigDatabase();
//    }catch(Exception err){
//    System.out.println("init db in settings controller");
//    }
//    private final PersonneDatabase personneDb = new PersonneDatabase();
//    private final CompteDatabase compteDb = new CompteDatabase();
//
//    private enum Mode { PERSONNE, COMPTE }
//    private Mode currentMode = Mode.PERSONNE;
//
//    @FXML
//    public void initialize() {
//        btnComptes.setOnAction(e -> afficherComptes());
//        btnPersonnes.setOnAction(e -> afficherPersonnes());
//
//        btnAjouter.setOnAction(e -> handleAjouter());
//        btnModifier.setOnAction(e -> handleModifier());
//        btnSupprimer.setOnAction(e -> handleSupprimer());
//
//        afficherPersonnes(); // affichage par défaut
//    }
//
//    private void afficherPersonnes() {
//        currentMode = Mode.PERSONNE;
//        try {
//            List<Personne> personnes = personneDb.findAll();
//            mainTable.getItems().clear();
//            mainTable.getColumns().setAll(
//                    col("ID", p -> ((Personne) p).getId_p()),
//                    col("Nom", p -> ((Personne) p).getNom()),
//                    col("Prénom", p -> ((Personne) p).getPrenom()),
//                    col("Email", p -> ((Personne) p).getEmail()),
//                    col("Compte", p -> ((Personne) p).isAvoir_compte() ? "OUI" : "NON")
//            );
//
//            ObservableList<Object> data = FXCollections.observableArrayList(personnes);
//            mainTable.setItems(data);
//
//            // surbrillance rouge
//            mainTable.setRowFactory(tv -> new TableRow<>() {
//                @Override
//                protected void updateItem(Object item, boolean empty) {
//                    super.updateItem(item, empty);
//                    if (!empty && item instanceof Personne p && !p.isAvoir_compte()) {
//                        setStyle("-fx-background-color: #fdd;");
//                    } else {
//                        setStyle("");
//                    }
//                }
//            });
//
//        } catch (SQLException e) {
//            showError("Erreur chargement personnes", e.getMessage());
//        }
//    }
//
//    private void afficherComptes() {
//        currentMode = Mode.COMPTE;
//        try {
//            List<Compte> comptes = compteDb.findAll();
//            mainTable.getItems().clear();
//            mainTable.getColumns().setAll(
//                    col("ID Compte", c -> ((Compte) c).getId_c()),
//                    col("Nom d'utilisateur", c -> ((Compte) c).getNom_utilisateur()),
//                    col("Rôle", c -> ((Compte) c).getRoles().toString())
//            );
//
//            ObservableList<Object> data = FXCollections.observableArrayList(comptes);
//            mainTable.setItems(data);
//
//            mainTable.setRowFactory(null); // désactiver surbrillance
//        } catch (SQLException e) {
//            showError("Erreur chargement comptes", e.getMessage());
//        }
//    }
//
//    private void handleAjouter() {
//        if (currentMode == Mode.PERSONNE) ajouterPersonne();
//        else showInfo("Info", "Ajout de compte non implémenté ici.");
//    }
//
//    private void handleModifier() {
//        Object selected = mainTable.getSelectionModel().getSelectedItem();
//        if (selected == null) return;
//        if (currentMode == Mode.PERSONNE) modifierPersonne((Personne) selected);
//    }
//
//    private void handleSupprimer() {
//        Object selected = mainTable.getSelectionModel().getSelectedItem();
//        if (selected == null) return;
//        try {
//            if (currentMode == Mode.PERSONNE) {
//                personneDb.remove((Personne) selected);
//                afficherPersonnes();
//            }
//            if (currentMode == Mode.COMPTE) {
//                compteDb.remove((Compte) selected);
//                afficherComptes();
//            }
//        } catch (SQLException e) {
//            showError("Erreur suppression", e.getMessage());
//        }
//    }
//
//    private void ajouterPersonne() {
//        Dialog<Personne> dialog = getPersonneDialog(null);
//        Optional<Personne> result = dialog.showAndWait();
//        result.ifPresent(p -> {
//            try {
//                personneDb.add(p);
//                afficherPersonnes();
//            } catch (SQLException e) {
//                showError("Ajout impossible", e.getMessage());
//            }
//        });
//    }
//
//    private void modifierPersonne(Personne p) {
//        Dialog<Personne> dialog = getPersonneDialog(p);
//        Optional<Personne> result = dialog.showAndWait();
//        result.ifPresent(updated -> {
//            try {
//                personneDb.update(updated,updated);
//                afficherPersonnes();
//            } catch (SQLException e) {
//                showError("Erreur mise à jour", e.getMessage());
//            }
//        });
//    }
//
//    private Dialog<Personne> getPersonneDialog(Personne p) {
//        Dialog<Personne> dialog = new Dialog<>();
//        dialog.setTitle(p == null ? "Ajouter" : "Modifier");
//        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
//
//        TextField id = new TextField(); id.setPromptText("ID");
//        TextField nom = new TextField(); nom.setPromptText("Nom");
//        TextField prenom = new TextField(); prenom.setPromptText("Prénom");
//        TextField email = new TextField(); email.setPromptText("Email");
//        CheckBox check = new CheckBox("Attribuer un compte");
//
//        if (p != null) {
//            id.setText(p.getId_p());
//            nom.setText(p.getNom());
//            prenom.setText(p.getPrenom());
//            email.setText(p.getEmail());
//            check.setSelected(p.isAvoir_compte());
//        }
//
//        dialog.getDialogPane().setContent(new VBox(10, id, nom, prenom, email, check));
//
//        dialog.setResultConverter(btn -> {
//            if (btn == ButtonType.OK) {
//                return new Personne(id.getText(), nom.getText(), prenom.getText(), email.getText(), check.isSelected());
//            }
//            return null;
//        });
//
//        return dialog;
//    }
//
//    private <T> TableColumn<Object, String> col(String title, java.util.function.Function<Object, String> extractor) {
//        TableColumn<Object, String> column = new TableColumn<>(title);
//        column.setCellValueFactory(c -> new SimpleStringProperty(extractor.apply(c.getValue())));
//        return column;
//    }
//
//    private void showError(String title, String msg) {
//        Alert a = new Alert(Alert.AlertType.ERROR);
//        a.setTitle(title);
//        a.setContentText(msg);
//        a.showAndWait();
//    }
//
//    private void showInfo(String title, String msg) {
//        Alert a = new Alert(Alert.AlertType.INFORMATION);
//        a.setTitle(title);
//        a.setContentText(msg);
//        a.showAndWait();
//    }
//}
