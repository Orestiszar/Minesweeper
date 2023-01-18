import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MineSweeper extends Application {
    public static Minefield minefield;

    @Override // Override the start method in the Application class
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        primaryStage.setTitle("test");
        primaryStage.setScene(new Scene(root)); // Place the scene in the stage
        primaryStage.show(); // Display the stage
    }
    public static void main(String[] args){
        minefield = new Minefield("./medialab/SCENARIO1.txt");
        minefield.setMinefield();
//        minefield.showMinefield();
//        CountDown temp = new CountDown(minefield.getSettings()[3]);
//        CountDown temp = new CountDown(5);
//        Thread mytemp = temp.t;
//        mytemp.join();
//        System.out.println("End");
        launch(args);
    }
}
