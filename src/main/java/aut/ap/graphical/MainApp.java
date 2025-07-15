package aut.ap.graphical;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.image.Image;


public class MainApp extends Application {

    public static void main(String[] args){
       launch(args);
    }

    @Override
    public void start(javafx.stage.Stage stage)  {

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/scene1.fxml"));
            Scene scene = new Scene(root);
            stage.setTitle("Milou");
            Image icon = new Image(getClass().getResourceAsStream("/milou_v3.jpg"));
            stage.getIcons().add(icon);
            stage.setScene(scene);
            stage.setResizable(true);
            stage.show();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
