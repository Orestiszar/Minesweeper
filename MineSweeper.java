import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MineSweeper extends Application {
    public static Minefield minefield;

    @Override // Override the start method in the Application class
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("stages/MainMenu.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("MediaLab Minesweeper");
        primaryStage.setScene(new Scene(root)); // Place the scene in the stage
        primaryStage.setResizable(true);
        primaryStage.show(); // Display the stage
    }
    public static void main(String[] args){
        launch(args);
    }
}
