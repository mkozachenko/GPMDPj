package main;

import static javafx.application.Application.launch;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    public static FXMLLoader loader;


   public static void main (String[] args){
       launch(args);
   }

    @Override
    public void start(Stage primaryStage) throws Exception {
        loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/player.fxml"));
        Parent root = loader.load();
        primaryStage.setResizable(false);
        primaryStage.setTitle("Player");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
