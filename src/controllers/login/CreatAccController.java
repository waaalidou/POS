package controllers.login;

import dao.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import utils.CancelEvent;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CreatAccController implements Initializable {
    @FXML
    private TextField Username;
    @FXML
    private PasswordField Password;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private Alert a = new Alert(Alert.AlertType.ERROR);
    private ObservableList<String> comptes = FXCollections.observableArrayList();
    @FXML
    private void InsertAbonement() {
        String nom = Username.getText();
        String code = Password.getText();
        if(comptes.contains(nom.trim().toLowerCase())) {
            a.setTitle("Erreur");
            a.setHeaderText("Element trouve !");
            a.setContentText("Ce compte exist  deja entrer unecompte qui n'existe pas");
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
            Username.setText("");
            Password.setText("");
            String query = "INSERT INTO `users`(`username`, `password`) VALUES (?,?)";
            connection  = DBConnection.getConnection();
            try {
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1,nom);
                preparedStatement.setString(2,code);
                preparedStatement.execute();
                Username.setText("");
                Password.setText("");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            a.setAlertType(Alert.AlertType.INFORMATION);
            a.setTitle("Termine!");
            a.setHeaderText("Operation Termine!");
            a.setContentText("Le compte est creer !");
            a.showAndWait();
        }

    }
    @FXML
    private void CancelCreation(ActionEvent event) {
        CancelEvent.Cancel(event);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getAbonement();
    }

    private void getAbonement() {
        String query = "SELECT nom FROM abonement";
        try {
            connection = DBConnection.getConnection();
            preparedStatement = connection.prepareStatement(query);
            ResultSet rs = connection.createStatement().executeQuery(query);
            while (rs.next()) {
                comptes.add(rs.getString("nom").toLowerCase().trim());
            }
            rs.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


}
