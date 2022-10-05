import javafx.application.Application;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
            Parent root = FXMLLoader.load(getClass().getResource("/resources/views/login/login.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle("SmS");
            stage.centerOnScreen();
            stage.setResizable(false);
            stage.getIcons().add(new Image("/resources/images/app_icon.png"));
            stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}