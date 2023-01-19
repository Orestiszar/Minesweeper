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
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import javafx.event.ActionEvent;


import java.io.IOException;
import java.io.StringReader;

public class Controller {
    private Stage stage;
    private Scene scene;
    private static Parent root;

    private ImageView getImage(String path){
        Image imageOk = new Image(getClass().getResourceAsStream(path));
        ImageView img = new ImageView(imageOk);
        img.setFitHeight(50);
        img.setPreserveRatio(true);
        return img;
    }

    public void switchToGame(ActionEvent event) throws IOException {
        BorderPane border = new BorderPane();
        GridPane grid = new GridPane();
        int grid_size = MineSweeper.minefield.getSettings()[1];
        for (int r = 0; r < grid_size; r++) {
            for (int c = 0; c < grid_size; c++) {
                int number = grid_size * r + c;
                Button button = new Button();
                button.setId(Integer.toString(r) + " " +Integer.toString(c));
                button.setPrefSize(50,50);

                ImageView img = getImage("graphics/Covered_Tile.png");
                button.setGraphic(img);
                button.setPadding(Insets.EMPTY);

                button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        MouseButton pressedButton = event.getButton();
                        if(pressedButton==MouseButton.PRIMARY){
//                            button.setText("PRIMARY button clicked on button");
                            String id = button.getId();
                            int i=0;
                            while(id.charAt(i) != ' '){
                                i++;
                            }
                            String myrow = id.substring(0,i);
                            String mycol = id.substring(i+1);
                            int brow = Integer.parseInt(myrow);
                            int bcol = Integer.parseInt(mycol);
                            System.out.println(id);
                            button.setDisable(true);
                            button.setOpacity(1);

                            MineSweeper.minefield.setTileOpened(brow,bcol);
                            if(MineSweeper.minefield.getTile(brow,bcol).mine==0){
                                //call recursive check
                                ImageView img = getImage("graphics/Uncovered_Tile.png");
                                button.setGraphic(img);
                                button.setPadding(Insets.EMPTY);
                            }
                            else{
                                //gameover
                                ImageView img = getImage("graphics/Mine.png");
                                button.setGraphic(img);
                                button.setPadding(Insets.EMPTY);
                            }

//                            Image imageOk = new Image(getClass().getResourceAsStream("graphics/Uncovered_Tile.png"));
//                            ImageView img = new ImageView(imageOk);
//                            img.setFitHeight(50);
//                            img.setPreserveRatio(true);
//                            button.setGraphic(img);
//                            button.setPadding(Insets.EMPTY);
//                            button.setDisable(true);
//                            button.setOpacity(1);

                        }
                        else if(pressedButton==MouseButton.SECONDARY){
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
        Button show_minefield_button = new Button("Show Minefield");

        MainMenu_Button.setOnAction(this::switchToMainMenu);
        show_minefield_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                MineSweeper.minefield.showMinefield();
            }
        });

        FlowPane top_buttons = new FlowPane();
        top_buttons.getChildren().addAll( MainMenu_Button , show_minefield_button) ;
        border.setTop(top_buttons);
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
