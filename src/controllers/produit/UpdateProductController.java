package controllers.produit;

import com.jfoenix.controls.JFXTextField;
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

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UpdateProductController implements Initializable {
    @FXML
    private JFXTextField nomField,codeCategoryField,CodeFournField,MinField,Quantite,CodeField;
    @FXML
    private ComboBox<String> CategoryChoice,FournisseurChoice;
    private Connection con;
    private PreparedStatement preparedStatement;
    private ResultSet rs;
    int id;
    String name,code,quantite,minimum,codeF,codeC;
    private ObservableList<String> categories = FXCollections.observableArrayList();
    private ObservableList<String> fournisseurs = FXCollections.observableArrayList();
    private Alert alert = new Alert(Alert.AlertType.ERROR);
    @FXML
    void Ajouter(ActionEvent event) {
        alert.setTitle("Erreur");
        con  = DBConnection.getConnection();
        String name = nomField.getText();
        String code = CodeField.getText();
        String quantite = Quantite.getText();
        String minimum = MinField.getText();
        String codeF = CodeFournField.getText();
        String codeC = codeCategoryField.getText();
        if (name.isEmpty() || code.isEmpty() || quantite.isEmpty() || minimum.isEmpty() || codeF.isEmpty() || codeC.isEmpty()) {
            alert.setHeaderText("Information Non Complete");
            alert.setContentText("Entrer tous les informations");
            alert.showAndWait();
        }
        else if (!minimum.matches("[0-9]+") || !quantite.matches("[0-9]+")) {

            alert.setHeaderText("Information Non Valid");
            alert.setContentText("Entrer une quantite valide !");
            alert.showAndWait();
        }
        else if (!name.matches("[a-zA-Z0-9_ ]{3,}")) {
            alert.setHeaderText("Nom non Valid");
            alert.setContentText("Entrer un Nom Valid !");
            alert.showAndWait();
        }
            else {
            Update(id);
            nomField.setText("");
            CodeField.setText("");
            Quantite.setText("");
            MinField.setText("");
            CodeFournField.setText("");
            codeCategoryField.setText("");
            CategoryChoice.valueProperty().set(null);
            FournisseurChoice.valueProperty().set(null);
            alert.setAlertType(Alert.AlertType.INFORMATION);
            alert.setTitle("MaJ");
            alert.setHeaderText("Operation Termine");
            alert.setContentText("La mise a jour du produtit est fait");
            alert.showAndWait();
            CancelEvent.Cancel(event);
        }
    }

    @FXML
    void Cancel(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        populateComboBox();
        populateFourniseur();
        fillFields(name,code,quantite,minimum);
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
        CategoryChoice.setItems(categories);
    }

    private void populateFourniseur() {

        try {
            String query_two = "SELECT CONCAT(fournisseur.code, '- ',fournisseur.nom) AS 'Code-Nom' FROM fournisseur";
            rs = con.createStatement().executeQuery(query_two);
            while (rs.next()) {
                fournisseurs.add(rs.getString("Code-Nom"));
            }
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        FournisseurChoice.setItems(null);
        FournisseurChoice.setItems(fournisseurs);
    }

    @FXML
    private void Update(int selectid) {

        selectid = ProductTableController.selectedId();
        String query_three = "UPDATE `Produit` SET `id`=?,`nom`=? ,`code_produit`=? ," +
                "`quantit_stock`= ? ,`quantit_mininmal`=?,`code_category`=? ," +
                "`code_fourniseur`=? WHERE id = " + selectid;
        try {
            preparedStatement = con.prepareStatement(query_three);
            preparedStatement.setInt(1,selectid);
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

    void fillFields(String name,String code,String quantite,String minimum) {
        nomField.setText(name);
        CodeField.setText(code);
        Quantite.setText(quantite);
        MinField.setText(minimum);
    }
}
