package controllers.fournisseur;

import com.jfoenix.controls.JFXTextField;
import dao.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import utils.CancelEvent;
import utils.SpecialAlert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddFournisseurController {

    @FXML
    private JFXTextField nomField;
    @FXML
    private JFXTextField CodeField;
    @FXML
    private JFXTextField PhoneField;
    @FXML
    private JFXTextField AdressField;

    private Connection con;
    private SpecialAlert sa = new SpecialAlert();
    private Alert alert = new Alert(Alert.AlertType.ERROR);
    @FXML
    private void Ajouter(ActionEvent event) {
        con  = DBConnection.getConnection();
        String name = nomField.getText();
        String code = CodeField.getText();
        String phone = PhoneField.getText();
        String adress = AdressField.getText();
        if (name.isEmpty() || code.isEmpty() || phone.isEmpty() || adress.isEmpty()) {
            alert.setTitle("Erreur");
            alert.setHeaderText("Information Non Complete");
            alert.setContentText("Entrer tous les informations");
            alert.showAndWait();
            //sa.show("Erreur", "Information Non Complete", "Entrer tous les informations", Alert.AlertType.ERROR);
        }        else if (!phone.matches("[0-9]{10}")) {
            alert.setTitle("Erreur");
            alert.setHeaderText("Information Non Valid");
            alert.setContentText("Entrer numero telephone Valid !");
            alert.showAndWait();
        }
        else if (!name.matches("[a-zA-Z0-9_ ]{3,}")) {
            alert.setTitle("Erreur");
            alert.setHeaderText("Nom non Valid");
            alert.setContentText("Entrer un Nom Valid !");
            alert.showAndWait();
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
        PhoneField.setText("");
        AdressField.setText("");
        Cancel(event);
    }
    @FXML
    void Cancel(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    private void insert() {
        String query_three = "INSERT INTO `fournisseur`(`id`, `nom`, `num_tele`, `code`, `adress`) VALUES (?,?,?,?,?)";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(query_three);
            preparedStatement.setString(1,null);
            preparedStatement.setString(2,nomField.getText());
            preparedStatement.setString(4,CodeField.getText());
            preparedStatement.setString(3,PhoneField.getText());
            preparedStatement.setString(5,AdressField.getText());
            preparedStatement.execute();
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
