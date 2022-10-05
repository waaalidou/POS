package controllers.category;

import dao.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CategoryController implements Initializable {

    @FXML
    private TextField NomCateg;
    @FXML
    private TextField CodeCateg;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private Alert a = new Alert(Alert.AlertType.ERROR);
    private ObservableList<String> categories = FXCollections.observableArrayList();
    @FXML
     private void InsertCategory() {
        String nom = NomCateg.getText();
        String code = CodeCateg.getText();
        if(categories.contains(nom.trim().toLowerCase())) {
            a.setTitle("Erreur");
            a.setHeaderText("Element trouve !");
            a.setContentText("Cette categorie exist  deja entrer une categorie qui n'existe pas");
            a.showAndWait();
            return;
        }
        if (nom.isEmpty() || code.isEmpty()) {
            a.setTitle("Ereure");
            a.setHeaderText("Informations non complete");
            a.setContentText("Svp entrer tous les informations");
            a.showAndWait();
        }
        else {
            NomCateg.setText("");
            CodeCateg.setText("");
            String query = "INSERT INTO `category`(`id`, `nom`, `code`) VALUES (?,?,?)";
            connection  = DBConnection.getConnection();
            try {
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1,null);
                preparedStatement.setString(2,nom);
                preparedStatement.setString(3,code);
                preparedStatement.execute();
                NomCateg.setText("");
                CodeCateg.setText("");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            a.setAlertType(Alert.AlertType.INFORMATION);
            a.setTitle("Termine!");
            a.setHeaderText("Operation Termine!");
            a.setContentText("La categorie est Inserer !");
            a.showAndWait();
        }

    }
    @FXML
    private void SuppCategory() {
        String nom = NomCateg.getText();
        String query = "DELETE FROM `category` WHERE `nom` = '" + NomCateg.getText() + "'";
        if (nom.isEmpty()) {
            a.setTitle("Erreur");
            a.setHeaderText("Informations non complete");
            a.setContentText("Svp entrer au moins le nom pour supprimer");
            a.showAndWait();
        }
        else if(!categories.contains(nom.toLowerCase())) {
            a.setTitle("Erreur");
            a.setHeaderText("Categorie invalide");
            a.setContentText("Cette Categorie n'existe pas");
            a.showAndWait();
        }
        else {
            connection = DBConnection.getConnection();
            try {
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.execute();
                NomCateg.setText("");
                CodeCateg.setText("");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            a.setAlertType(Alert.AlertType.INFORMATION);
            a.setTitle("Termine!");
            a.setHeaderText("Operation Termine!");
            a.setContentText("La categorie est Supprimer !");
            a.showAndWait();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getCategories();
    }

    private void getCategories() {
        String query = "SELECT nom FROM category";
        try {
            connection = DBConnection.getConnection();
            preparedStatement = connection.prepareStatement(query);
            ResultSet rs = connection.createStatement().executeQuery(query);
            while (rs.next()) {
                categories.add(rs.getString("nom").toLowerCase().trim());
            }
            rs.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    }