import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import javafx.event.ActionEvent;


import java.io.IOException;

public class Controller {
    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchToGame(ActionEvent event) throws IOException {
        BorderPane border = new BorderPane();
        GridPane grid = new GridPane();
        int grid_size = MineSweeper.minefield.getSettings()[1];
        for (int r = 0; r < grid_size; r++) {
            for (int c = 0; c < grid_size; c++) {
                int number = grid_size * r + c;
                Button button = new Button();
                button.setId(Integer.toString(r) + Integer.toString(c));
                button.setPrefSize(50,50);


                Image imageOk = new Image(getClass().getResourceAsStream("graphics/tile.jpg"));
                ImageView img = new ImageView(imageOk);
                img.setFitHeight(50);
                img.setPreserveRatio(true);
                button.setGraphic(img);
                button.setPadding(Insets.EMPTY);


                button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        MouseButton pressedbutton = event.getButton();
                        if(pressedbutton==MouseButton.PRIMARY){
//                            button.setText("PRIMARY button clicked on button");
                            System.out.println(button.getId());
                        }else if(pressedbutton==MouseButton.SECONDARY){
                            button.setText("SECONDARY button clicked on button");
                        }
                    }
                });

                grid.add(button, c, r);
            }
        }
        ScrollPane scrollPane = new ScrollPane(grid);

        border.setCenter(scrollPane);
        Button MainMenu_Button = new Button("Main Menu");

        MainMenu_Button.setOnAction(this::switchToMainMenu);
        border.setTop(MainMenu_Button);

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(border));
        stage.show();
    }

    public void switchToMainMenu(ActionEvent event){
        try{
            Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene((scene));
            stage.show();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
