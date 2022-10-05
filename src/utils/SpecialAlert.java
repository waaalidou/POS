package utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class SpecialAlert {

     Alert alert = new Alert(AlertType.NONE);

    public void show(String title, String header,String message, AlertType alertType) {
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.setAlertType(alertType);
        alert.showAndWait();
    }
}