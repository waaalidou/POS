package controllers.produit;

import com.jfoenix.controls.JFXTextField;


import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import dao.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import utils.CancelEvent;
import utils.SpecialAlert;

public class AddProductController implements Initializable {

    @FXML
    private JFXTextField nomField;
    @FXML
    private JFXTextField CodeField;
    @FXML
    private JFXTextField Quantite;
    @FXML
    private JFXTextField MinField;
    @FXML
    private ComboBox<String> CategoryChoice;
    @FXML
    private ComboBox<String> FournisseurChoice;
    @FXML
    private JFXTextField CodeFournField;
    @FXML
    private JFXTextField codeCategoryField;
    private Connection con;
    private PreparedStatement preparedStatement;
    private ResultSet rs;
    private ObservableList<String> categories = FXCollections.observableArrayList();
    private ObservableList<String> fournisseurs = FXCollections.observableArrayList();
    private ObservableList<String> CategoriesList = FXCollections.observableArrayList();
    private ObservableList<String> FournisseurList = FXCollections.observableArrayList();
    private SpecialAlert sa = new SpecialAlert();
    private Alert a = new Alert(Alert.AlertType.CONFIRMATION);
    @FXML
    private void Ajouter(ActionEvent event) {
        con  = DBConnection.getConnection();
        String name = nomField.getText();
        String code = CodeField.getText();
        String quantite = Quantite.getText();
        String minimum = MinField.getText();
        if (name.isEmpty() || code.isEmpty() || quantite.isEmpty() || minimum.isEmpty()) {
            sa.show("Erreur", "Information Non Complete", "Entrer tous les informations", Alert.AlertType.ERROR);
        }
        else if (!minimum.matches("[0-9]+") || !quantite.matches("[0-9]+")) {
            sa.show("Erreur", "Information Non Valid", "Entrer informations Valid !", Alert.AlertType.ERROR);
        }
        else if (!name.matches("[a-zA-Z0-9_ ]{3,}")) {
            sa.show("Erreur", "Nom non Valid", "Entrer un Nom Valid !", Alert.AlertType.ERROR);

        } else if(!CategoriesList.contains(codeCategoryField.getText().toLowerCase())) {
            sa.show("Erreur","Categorie Invalide","SVP entrer categorie Valid", Alert.AlertType.ERROR);
        } else if(!FournisseurList.contains(CodeFournField.getText().toLowerCase()))
            sa.show("Erreur", "Fournisseur Invalide", "SVP entrer Fournisseur Valid", Alert.AlertType.ERROR);
        else if(Integer.parseInt(quantite) < Integer.parseInt(minimum)  ) {
            a.setTitle("Attention");
            a.setContentText("La quantite est inferieur au minimum!");
            if(a.showAndWait().get() == ButtonType.OK) {
                afterInsertClicked(event);
            }
        }
        else {
            afterInsertClicked(event);
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Ajoute");
            a.setContentText("L'operation est termine");
            a.showAndWait();
            CancelEvent.Cancel(event);
        }
    }

    private void afterInsertClicked(ActionEvent event) {
        insert();
        nomField.setText("");
        CodeField.setText("");
        Quantite.setText("");
        MinField.setText("");
        CodeFournField.setText("");
        codeCategoryField.setText("");
        CategoryChoice.valueProperty().set(null);
        FournisseurChoice.valueProperty().set(null);
        Cancel(event);
    }
    @FXML
    void Cancel(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    private void insert() {
        String query_three = "INSERT INTO `Produit`(`id`, `nom`, `code_produit`, `quantit_stock`, " +
                "`quantit_mininmal`, `code_category`, `code_fourniseur`) " +
                "VALUES (?,?,?,?,?,?,?)";
        try {
            preparedStatement = con.prepareStatement(query_three);
            preparedStatement.setString(1,null);
            preparedStatement.setString(2,nomField.getText());
            preparedStatement.setString(3,CodeField.getText());
            preparedStatement.setInt(4,Integer.parseInt(Quantite.getText()));
            preparedStatement.setInt(5,Integer.parseInt(MinField.getText()));
            preparedStatement.setString(6,codeCategoryField.getText());
            preparedStatement.setString(7,CodeFournField.getText());
            preparedStatement.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        populateComboBox();
        populateFourniseur();
        checkCategory();
        checkFournisseur();
    }

    private void populateComboBox() {
        con = DBConnection.getConnection();
        String query = "SELECT CONCAT(category.code, '- ',category.nom) AS 'Code-Nom' FROM category";
        try {
            preparedStatement = con.prepareStatement(query);
            rs = con.createStatement().executeQuery(query);
            while (rs.next()) {
                categories.add(rs.getString("Code-Nom"));
            }
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        CategoryChoice.setItems(null); //beh yjou mratbin machhi jdid yatle3 foug lgdim ki t'inserer mn lapp
        CategoryChoice.setItems(categories);
    }

    private void populateFourniseur() {

        try {
            String query_two = "SELECT CONCAT(fournisseur.code, '- ',fournisseur.nom) AS 'Code-Nom' FROM fournisseur";
            rs = con.createStatement().executeQuery(query_two);
            while (rs.next()) {
                fournisseurs.add(rs.getString("Code-Nom"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        FournisseurChoice.setItems(null);
        FournisseurChoice.setItems(fournisseurs);
    }

    private void checkCategory() {
        try {
            con = DBConnection.getConnection();
            String querry = "SELECT * FROM category";
            con.prepareStatement(querry);
            rs = preparedStatement.executeQuery(querry);
            while (rs.next()) {
                CategoriesList.add(rs.getString("code").toLowerCase());
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void checkFournisseur() {
        try {
            con = DBConnection.getConnection();
            String querryF = "SELECT * FROM fournisseur";
            con.prepareStatement(querryF);
            rs = preparedStatement.executeQuery(querryF);
            while (rs.next()) {
                FournisseurList.add(rs.getString("code").toLowerCase());
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
