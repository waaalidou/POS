package controllers.login;


import dao.DBConnection;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LogInController  implements Initializable{

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;
    int status;
    Alert alert = new Alert(Alert.AlertType.ERROR);
    @FXML
    private void onLogin(ActionEvent e) {
        if (!usernameField.getText().matches("[a-zA-Z0-9_]{4,}")) {
            return;
        }
        if (passwordField.getText().isEmpty()) {
            return;
        }

         status = DBConnection.checkLogin(usernameField.getText().trim().toLowerCase(), passwordField.getText());

        switch (status) {
            case 0: {
                Stage stage = (Stage) usernameField.getScene().getWindow();

                Parent root = null;
                try {
                    root = FXMLLoader.load(getClass().getResource("/resources/views/products/ProductView.fxml"));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                stage.setScene(new Scene(root));
                stage.centerOnScreen();
            }
            break;
            case -1: {
                alert.setTitle("Erreur");
                alert.setContentText("Connection echoue Verifier que la base des donnes est connecte !");
                alert.showAndWait();
                break;
            }
            case 1: {

                alert.setTitle("Erreur");
                alert.setContentText("Le username ou le mot de pass est errone");
                alert.showAndWait();
                break;
            }

        }
    }

    @FXML
    private void CreateAccount() {
        Parent root ;
        try {
            root = FXMLLoader.load(getClass().getResource("/resources/views/login/CreatAcc.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.setTitle("Creer Compte");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
         status = DBConnection.checkLogin(usernameField.getText().trim().toLowerCase(), passwordField.getText());
    }
}