import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MineSweeper extends Application {
    public static Minefield minefield;

    public static void initMinefield(String path){
        minefield = new Minefield(path);
        minefield.setMinefield();
    }

    @Override // Override the start method in the Application class
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
        Parent root = loader.load();
        Controller myController = (Controller)loader.getController();
        primaryStage.setTitle("MediaLab Minesweeper");
        primaryStage.setScene(new Scene(root)); // Place the scene in the stage
        primaryStage.setResizable(true);
        primaryStage.show(); // Display the stage

        myController.switchToGame(null);
    }
    public static void main(String[] args){
//        minefield.showMinefield();
//        CountDown temp = new CountDown(minefield.getSettings()[3]);
//        CountDown temp = new CountDown(5);
//        Thread mytemp = temp.t;
//        mytemp.join();
//        System.out.println("End");
        launch(args);
    }
}
