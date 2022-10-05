package utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class MovingBetweenScreens {
    public void switchScreen(String path, String title, MouseEvent e) {
        Parent root ;
        Stage stage;
        try {
            root = FXMLLoader.load(getClass().getResource(path));
            Scene scene = new Scene(root);
            stage = (Stage)((Node)e.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
