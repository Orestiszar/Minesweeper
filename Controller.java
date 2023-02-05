import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import myExceptions.InvalidDescriptionException;
import myExceptions.InvalidValueException;

import java.io.*;



public class Controller {
    private Stage stage;
    public Button[][] grid_buttons;

    @FXML
    public FlowPane flowpane;
    @FXML
    public GridPane grid;
    @FXML
    public Label timer_label;
    @FXML
    public Label mine_label;
    @FXML
    public Label flag_label;
    @FXML
    public VBox myvbox;



    @FXML
    public TextField scenario_id_textfield;
    @FXML
    public TextField difficulty_textfield;
    @FXML
    public TextField numofmines_textfield;
    @FXML
    public TextField time_limit_textfield;
    @FXML
    public CheckBox supermine_checkbox;

    @FXML
    public TextField load_scenario_textfield;
    @FXML
    public Label load_scenario_label;
    @FXML
    public Button load_button;

    @FXML
    public TableView<Myrow> tableView;

    @FXML
    public TableColumn<Myrow, Integer> mines_column;
    @FXML
    public TableColumn<Myrow, Integer> tries_column;
    @FXML
    public TableColumn<Myrow, Integer> time_column;
    @FXML
    public TableColumn<Myrow, String> winner_column;

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
            MineSweeper.minefield.setTileFlag(row,col,false);
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

        if(MineSweeper.minefield.getTile(row,col).opened) return;

        int flagged_mines=0,grid_size,mine_count;
        grid_size = MineSweeper.minefield.getSettings()[1];
        mine_count = MineSweeper.minefield.getSettings()[2];
        flagged_mines = MineSweeper.minefield.getFlaggedMines();

        if(MineSweeper.minefield.getTile(row,col).flagged){
            ImageView img = getImage("graphics/Covered_Tile.png");
            MineSweeper.minefield.setTileFlag(row,row,false);
            grid_buttons[row][col].setGraphic(img);
            grid_buttons[row][col].setPadding(Insets.EMPTY);
            flag_label.setText(Integer.toString(flagged_mines-1));
            MineSweeper.minefield.setTileFlag(row,col,false);
            return;
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
                flag_label.setText(Integer.toString(flagged_mines+1));
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
        if(CountDown.mythread == null) return;
        if(CountDown.mythread.isAlive()){
            CountDown.mythread.interrupt();
            String text;
            if(won){
                text = "You Win :)";
            }
            else{
                text = "You Lose :(";
            }
            double s_width = stage.getWidth();
            double l_width = timer_label.getWidth();
            timer_label.setPadding(new Insets(0,(s_width-l_width),0,(s_width-l_width)));
            timer_label.setText(text);
            setAndDisableAllButtons();
        }
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
//            System.out.println(id);
            tries++;
            if(MineSweeper.minefield.getTile(brow,bcol).mine==0){
                //call recursive check
                revealEmptyCells(brow,bcol);
                flag_label.setText(Integer.toString(MineSweeper.minefield.getFlaggedMines()));
                if(MineSweeper.minefield.gameWon()){
                    gameOver(true);
                }
            }
            else{
                gameOver(false);
            }
        }
        else if(pressedButton==MouseButton.SECONDARY){
            performFlag(brow,bcol);
        }
    }

    public void switchToGame(){
        stage = (Stage)(myvbox).getScene().getWindow();
        if(MineSweeper.minefield!=null){
            MineSweeper.minefield.setMinefield();
        }
        else{
            timer_label.setPadding(new Insets(0,110,0,100));
            timer_label.setText("Load a Scenario first!");
            return;
        }
        tries = 0;
        int grid_size = MineSweeper.minefield.getSettings()[1];
        int mine_count = MineSweeper.minefield.getSettings()[2];
        int time = MineSweeper.minefield.getSettings()[3];

        CountDown timer = new CountDown(time, this);
        if(CountDown.mythread != null){
            CountDown.mythread.interrupt();
        }
        CountDown.mythread = new Thread(timer,"Timer");
        CountDown.mythread.setDaemon(true);

        grid_buttons = new Button[grid_size][grid_size];
        timer_label.setFont(Font.font("Arial", FontWeight.BOLD, 25));
        timer_label.setText(Integer.toString(time));
        mine_label.setText(Integer.toString(mine_count));
        flag_label.setText("0");
        myvbox.getChildren().remove(grid);
        grid = new GridPane();

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
        myvbox.getChildren().add(grid);
        stage.sizeToScene();
        double s_width = stage.getWidth();
        timer_label.setPadding(new Insets(0,(s_width/3),0,s_width/3));
        CountDown.mythread.start();
    }

    public void create_button_popup(){
        try{
            Parent root = FXMLLoader.load(getClass().getResource("Create.fxml"));
            Stage create_popup = new Stage();
            create_popup.initModality(Modality.APPLICATION_MODAL);
            create_popup.setTitle("Create");
            Scene scene1= new Scene(root);
            create_popup.setScene(scene1);
            create_popup.showAndWait();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public void create_scenario(){
        BufferedWriter bwr = null;
        String scenario_id,difficulty,numofmines,time_limit;
        scenario_id = scenario_id_textfield.getText();
        difficulty =  difficulty_textfield.getText();
        numofmines = numofmines_textfield.getText();
        time_limit = time_limit_textfield.getText();
        boolean supermine = supermine_checkbox.isSelected();

        try {
            bwr = new BufferedWriter(new FileWriter("./medialab/"+scenario_id+ ".txt"));
            bwr.write(difficulty+"\n"+numofmines+"\n"+time_limit+"\n"+ (supermine?1:0));
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try
                {if (bwr != null) {bwr.close();}
                }
            catch (IOException e) {
            }
        }
        ((Stage)(difficulty_textfield).getScene().getWindow()).close();
    }

    public void load_button_popup(){
        try{
            Parent root = FXMLLoader.load(getClass().getResource("Load.fxml"));
            Stage load_popup = new Stage();
            load_popup.initModality(Modality.APPLICATION_MODAL);
            load_popup.setTitle("Load");
            Scene scene1= new Scene(root);
            load_popup.setScene(scene1);
            load_popup.showAndWait();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public void load_scenario(){
        String mypath = load_scenario_textfield.getText();
        try{
            MineSweeper.minefield = new Minefield("./medialab/"+ mypath+".txt");
            ((Stage)(load_scenario_textfield).getScene().getWindow()).close();
        }
        catch (FileNotFoundException e){
            load_scenario_label.setText("Scenario not found");
        }
        catch(InvalidDescriptionException e){
            load_scenario_label.setText("Invalid Scenario");
        }
        catch (InvalidValueException e){
            load_scenario_label.setText("Invalid values");
        }
    }

    public void exit_button(){
        Platform.exit();
    }

    public void rounds_button_popup(){
        try{

            FXMLLoader loader = new FXMLLoader(getClass().getResource("Rounds.fxml"));
            Parent root = loader.load();
            Controller myController = (Controller)loader.getController();
            Stage rounds_popup = new Stage();
            rounds_popup.initModality(Modality.APPLICATION_MODAL);
            rounds_popup.setTitle("Details");
            Scene scene1= new Scene(root);
            rounds_popup.setScene(scene1);

            //set the columns
            myController.mines_column.setCellValueFactory(new PropertyValueFactory<Myrow,Integer>("total_mines"));
            myController.tries_column.setCellValueFactory(new PropertyValueFactory<Myrow,Integer>("tries"));
            myController.time_column.setCellValueFactory(new PropertyValueFactory<Myrow,Integer>("time"));
            myController.winner_column.setCellValueFactory(new PropertyValueFactory<Myrow,String>("winner"));
            myController.tableView.setItems(getPastGames());

            rounds_popup.showAndWait();

        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public ObservableList<Myrow> getPastGames(){
        ObservableList<Myrow> result = FXCollections.observableArrayList();
        String line = null;
        try{
            BufferedReader br = new BufferedReader(new FileReader("pastGames.txt"));
            while((line = br.readLine()) != null) { //read next line until eof
                if(line.length() == 0) //Skip empty lines
                    continue;
                String[] numbers = line.split(",");
                int[] ints = new int[3];
                for(int i = 0; i < 3; i++) ints[i] = Integer.parseInt(numbers[i]);
                Myrow row = new Myrow(ints[0],ints[1],ints[2],numbers[3]);
                result.add(row);
            }
        }
        catch (IOException e){
            result=null;
        }
        return result;
    }

    public void solution_button(){
        gameOver(false);
    }



}
