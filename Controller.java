import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import java.io.IOException;

public class Controller {
    private Stage stage;
    private Scene scene;
    private Parent root;
    public static Button[][] grid_buttons = null;
    public static Label timer_label = null;

    private int tries=0;

    private ImageView getImage(String path){
        Image imageOk = new Image(getClass().getResourceAsStream(path));
        ImageView img = new ImageView(imageOk);
        img.setFitHeight(50);
        img.setPreserveRatio(true);
        return img;
    }

    private void setCorrectImage(final int row, final int col){
        ImageView img = null;
        if(MineSweeper.minefield.getTile(row, col).mine==1){
            img = getImage("graphics/Mine.png");
        }
        else{
            int mines = MineSweeper.minefield.getNumOfMines(row,col);
            switch(mines){
                case 0: img = getImage("graphics/Uncovered_Tile.png"); break;
                case 1: img = getImage("graphics/1.png"); break;
                case 2: img = getImage("graphics/2.png"); break;
                case 3: img = getImage("graphics/3.png"); break;
                case 4: img = getImage("graphics/4.png"); break;
                case 5: img = getImage("graphics/5.png"); break;
                case 6: img = getImage("graphics/6.png"); break;
                case 7: img = getImage("graphics/7.png"); break;
                case 8: img = getImage("graphics/8.png"); break;
                case 9: img = getImage("graphics/Mine.png"); break;
            }
        }
        grid_buttons[row][col].setGraphic(img);
        grid_buttons[row][col].setPadding(Insets.EMPTY);
        grid_buttons[row][col].setDisable(true);
        grid_buttons[row][col].setOpacity(1);
    }

    private void revealEmptyCells(final int row, final int col){
        if(MineSweeper.minefield.getTile(row,col).mine==0){
            int mines = MineSweeper.minefield.getNumOfMines(row,col);
            setCorrectImage(row,col);
            MineSweeper.minefield.setTileOpened(row,col);
            if(mines>0) return;
            int grid_size = MineSweeper.minefield.getSettings()[1];
            for(int i=-1;i<2;i++){
                if(row+i<0 || row+i>grid_size-1){
                    continue;
                }
                for(int j=-1;j<2;j++){
                    if(col+j<0 || col+j>grid_size-1){
                        continue;
                    }
                    if(!MineSweeper.minefield.getTile(row+i,col+j).opened){
                        revealEmptyCells(row+i,col+j);
                    }
                }
            }
        }
    }

    private void performFlag(final int row, final int col){
        int flagged_mines=0,grid_size,mine_count;
        grid_size = MineSweeper.minefield.getSettings()[1];
        mine_count = MineSweeper.minefield.getSettings()[2];
        for(int k=0;k<grid_size;k++){
            for(int m=0;m<grid_size;m++){
                if(MineSweeper.minefield.getTile(k,m).flagged){
                    flagged_mines+=1;
                }
            }
        }
        if(mine_count>flagged_mines){//num of mines = settings[2]
            if(MineSweeper.minefield.getTile(row,col).supermine==1 && tries<4){
                //reveal row&col
                for(int i=0;i<grid_size;i++){
                    setCorrectImage(i,col);
                    if(MineSweeper.minefield.getTile(i,col).mine==0){
                        MineSweeper.minefield.setTileOpened(i,col);
                    }
                    setCorrectImage(row,i);
                    if(MineSweeper.minefield.getTile(row,i).mine==0){
                        MineSweeper.minefield.setTileOpened(row,i);
                    }
                }

            }
            else{
                ImageView img = getImage("graphics/Flag.png");
                MineSweeper.minefield.setTileFlag(row,col,true);
                grid_buttons[row][col].setGraphic(img);
                grid_buttons[row][col].setPadding(Insets.EMPTY);
            }
        }
    }

    private void setAndDisableAllButtons(){
        int grid_size = MineSweeper.minefield.getSettings()[1];
        for(int i=0;i<grid_size;i++){
            for(int j=0;j<grid_size;j++){
                setCorrectImage(i,j);
            }
        }
    }

    public void gameOver(boolean won){
        //stop the thread

        if(CountDown.mythread.isAlive()){
            CountDown.mythread.interrupt();
        }
        String text;
        if(won){
            text = "You Win :)";
        }
        else{
            text = "You Lose";
        }
        timer_label.setText(text);
        setAndDisableAllButtons();
    }

    private void myclickhandler(MouseEvent event){
        Button button = (Button)event.getSource();
        MouseButton pressedButton = event.getButton();
        //getbuttonid
        String id = button.getId();
        int i=0;
        while(id.charAt(i) != ' '){
            i++;
        }
        String myrow = id.substring(0,i);
        String mycol = id.substring(i+1);
        int brow = Integer.parseInt(myrow);
        int bcol = Integer.parseInt(mycol);
        if(pressedButton==MouseButton.PRIMARY){
            System.out.println(id);
            tries++;

            if(MineSweeper.minefield.getTile(brow,bcol).mine==0){
                //call recursive check
                revealEmptyCells(brow,bcol);
                if(MineSweeper.minefield.gameWon()){
                    gameOver(true);

                }
            }
            else{
                gameOver(false);
            }
        }
        else if(pressedButton==MouseButton.SECONDARY){
            ImageView img;
            if(MineSweeper.minefield.getTile(brow,bcol).flagged){
                img = getImage("graphics/Covered_Tile.png");
                MineSweeper.minefield.setTileFlag(brow,bcol,false);
                button.setGraphic(img);
                button.setPadding(Insets.EMPTY);
            }
            else{
                performFlag(brow,bcol);
            }
        }
    }

    public void switchToGame(ActionEvent event){
        MineSweeper.initMinefield("./medialab/SCENARIO2.txt");//change path
        int grid_size = MineSweeper.minefield.getSettings()[1];
        int time = MineSweeper.minefield.getSettings()[3];

        CountDown timer = new CountDown(time, this);
        timer.mythread = new Thread(timer,"Timer");
        timer.mythread.setDaemon(true);

        grid_buttons = new Button[grid_size][grid_size];
        timer_label = new Label(Integer.toString(time));

        timer_label.setFont(Font.font("Arial", FontWeight.BOLD, 35));

        BorderPane border = new BorderPane();
        GridPane grid = new GridPane();

        for (int r = 0; r < grid_size; r++) {
            for (int c = 0; c < grid_size; c++) {
                Button button = new Button();
                button.setId(r + " " +c);
                button.setPrefSize(50,50);

                ImageView img = getImage("graphics/Covered_Tile.png");
                button.setGraphic(img);
                button.setPadding(Insets.EMPTY);
                button.setOnMouseClicked(this::myclickhandler);
                grid.add(button, c, r);
                grid_buttons[r][c] = button;
            }
        }
        ScrollPane scrollPane = new ScrollPane(grid);

        border.setCenter(scrollPane);
        Button MainMenu_Button = new Button("Main Menu");
        Button show_minefield_button = new Button("Show Minefield");

        MainMenu_Button.setOnAction(this::switchToMainMenu);
        show_minefield_button.setOnAction(actionEvent -> MineSweeper.minefield.showMinefield());

        FlowPane top_buttons = new FlowPane();
        top_buttons.getChildren().addAll(MainMenu_Button , show_minefield_button, timer_label) ;
        border.setTop(top_buttons);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(border));
        stage.show();

        timer.mythread.start();
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
