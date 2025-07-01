package aut.ap.graphical;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;


public class MainApp extends Application {

    public static void main(String[] args){
       launch(args);
    }

    @Override
    public void start(javafx.stage.Stage stage)  {

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/scene1.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(true);
            stage.show();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
