package controllers.client;


import com.jfoenix.controls.JFXTextField;
import dao.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import utils.CancelEvent;
import utils.SpecialAlert;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddClientController implements Initializable {

    @FXML
    private JFXTextField nomField,CodeField,AdressF,codeAbonField,TelNumF;
    @FXML
    private ComboBox<String> AbonementChoice;
    private ObservableList<String> abonnement = FXCollections.observableArrayList();
    private ObservableList<String> abonementList = FXCollections.observableArrayList();
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet rs;
    private Alert alert = new Alert(Alert.AlertType.ERROR);
    private SpecialAlert sa = new SpecialAlert();
    @FXML
    void Ajouter(ActionEvent event) {
        connection  = DBConnection.getConnection();
        alert.setTitle("Erreur");
        String name = nomField.getText();
        String code = CodeField.getText();
        String phone = TelNumF.getText();
        String adress = AdressF.getText();
        String abonement = codeAbonField.getText();
        if (name.isEmpty() || code.isEmpty() || phone.isEmpty() || adress.isEmpty() || abonement.isEmpty()) {
            alert.setHeaderText("Information Non Complete");
            alert.setContentText("Entrer tous les informations !");
            alert.showAndWait();
        }
        else if (!phone.matches("[0-9]{10}+")) {
            alert.setHeaderText("Numero Telephone Non Valid");
            alert.setContentText("Entrer un numero de telephone Valid !");
            alert.showAndWait();
        }
        else if (!name.matches("[a-zA-Z0-9_ ]{3,}")) {
            alert.setHeaderText("Nom non Valid");
            alert.setContentText("Entrer un Nom Valid !");
            alert.showAndWait();
        } else if(!abonementList.contains(codeAbonField.getText().toLowerCase()) && !codeAbonField.getText().equals("")) {
            alert.setHeaderText("Abonement Invalide");
            alert.setContentText("\"SVP entrer Abonement Valid !");
            alert.showAndWait();
        } else {
            insert();
            alert.setAlertType(Alert.AlertType.INFORMATION);
            alert.setTitle("Ajoute");
            alert.setHeaderText("Operation termine");
            alert.setContentText("Un nouveau client est ajouter");
            alert.showAndWait();
            CancelEvent.Cancel(event);
        }
    }

    private void insert() {
        connection = DBConnection.getConnection();
        String query = "INSERT INTO `client`(`id`, `nom`, `num_tele`, `Adress`, `Code`, `code_abonement`) VALUES (?,?,?,?,?,?)";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,null);
            preparedStatement.setString(2,nomField.getText());
            preparedStatement.setString(5,CodeField.getText());
            preparedStatement.setString(3,TelNumF.getText());
            preparedStatement.setString(4,AdressF.getText());
            preparedStatement.setString(6,codeAbonField.getText());
            preparedStatement.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void populateComboBox() {
        abonnement.clear();
        connection = DBConnection.getConnection();
        String query = "SELECT CONCAT(abonement.code, '- ',abonement.nom) AS 'Code-Nom' FROM abonement";
        try {
            preparedStatement = connection.prepareStatement(query);
            rs = connection.createStatement().executeQuery(query);
            while (rs.next()) {
                abonnement.add(rs.getString("Code-Nom"));
            }
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        AbonementChoice.setItems(null);
        AbonementChoice.setItems(abonnement);
    }
    @FXML
    void Cancel(ActionEvent event) {
        CancelEvent.Cancel(event);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        checkCategory();
        populateComboBox();
    }

    private void checkCategory() {
        try {
            connection = DBConnection.getConnection();
            String querry = "SELECT * FROM abonement";
            connection.prepareStatement(querry);
            rs = connection.createStatement().executeQuery(querry);
            while (rs.next()) {
                abonementList.add(rs.getString("code").toLowerCase());
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
