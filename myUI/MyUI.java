package myUI;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MyUI extends Application{
    @Override // Override the start method in the Application class
    public void start(Stage primaryStage) throws Exception{
            Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
            primaryStage.setTitle("test");
            primaryStage.setScene(new Scene(root)); // Place the scene in the stage
            primaryStage.show(); // Display the stage
    }
}
