import javafx.event.ActionEvent;
import myExceptions.InvalidDescriptionException;
import myExceptions.InvalidValueException;

import java.io.*;

import static java.lang.Integer.min;
import static java.lang.Integer.parseInt;

public class Minefield {
    private int difficulty,grid_size,mine_count,seconds,supermine;
    private Tile[][] minefield;

    public Tile getTile(int row, int col){
        return minefield[row][col];
    }

    public void setTileOpened(int row, int col){
        minefield[row][col].opened=true;
    }

    public void setTileFlag(int row, int col, boolean state){
        minefield[row][col].flagged=state;
    }

    public int getNumOfMines(int row, int col){
        int total = 0;
        for(int i=-1;i<2;i++){
            if(row+i<0 || row+i>grid_size-1){
                continue;
            }
            for(int j=-1;j<2;j++){
                if(col+j<0 || col+j>grid_size-1){
                    continue;
                }
                if(minefield[row+i][col+j].mine==1){
                    total++;
                }
            }
        }
        return total;
    }

    public int[] getSettings(){
        int[] result = new int[5];
        result[0] = difficulty;
        result[1] = grid_size;
        result[2] = mine_count;
        result[3] = seconds;
        result[4] = supermine;
        return result;
    }

    private int[] ReadSettings(String path) throws InvalidDescriptionException {//return data from setting file in an int array
        BufferedReader br = null;
        String line;
        int data[] = new int[4];
        int i = 0;
        try {
            /* Create BufferedReader to read file */
            br = new BufferedReader(new FileReader(path));
            while((line = br.readLine()) != null) { //read next line until eof
                if(line.length() == 0) //Skip empty lines
                    continue;
                data[i] = parseInt(line);
                i++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {if (br != null) {br.close();}
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(i<4){
            throw new InvalidDescriptionException("Description file invalid");
        }
        return data;
    }

    private void setParameters(int[] settings) throws InvalidValueException {
        difficulty = settings[0];
        mine_count = settings[1];
        seconds = settings[2];
        supermine = settings[3];

        if(difficulty<1 || difficulty>2){
            throw new InvalidValueException("Difficulty should be an integer of value 1 or 2.");
        }

        if(difficulty == 1 && (mine_count<9 || mine_count > 11)){
            throw new InvalidValueException("Invalid mine count for selected difficulty. Bounds are 9-11.");
        }

        if(difficulty == 2 && (mine_count<35 || mine_count > 45)){
            throw new InvalidValueException("Invalid mine count for selected difficulty. Bounds are 35-45.");
        }

        if(difficulty == 1 && (seconds<120 || seconds > 180)){
            throw new InvalidValueException("Invalid time limits for selected difficulty. Bounds are 120-180.");
        }

        if(difficulty == 2 && (seconds<240 || seconds > 360)){
            throw new InvalidValueException("Invalid time limits for selected difficulty. Bounds are 240-360.");
        }

        if(supermine<0 || supermine>1){
            throw new InvalidValueException("Supermine value should be 0 or 1");
        }

        if(difficulty == 1 && supermine==1){
            throw new InvalidValueException("Supermines are only available in difficulty 2.");
        }
        //set grid_size
        if (difficulty==1) grid_size = 9;
        else grid_size = 16;
    }

    public void setMinefield(){
        int x,y;
        int[] minex = new int[mine_count];//collect the mines to decide a supermine
        int[] miney = new int[mine_count];

        for(int i=0;i<mine_count;i++){
            do{
                x = (int)Math.floor(Math.random() * (grid_size)); //(max - min + 1) + min);
                y = (int)Math.floor(Math.random() * (grid_size));
            }
            while(minefield[x][y].mine == 1); //mine already at that position
            minefield[x][y].mine = 1;
            minex[i] = x;
            miney[i] = y;
        }

        if(difficulty==2){//select the supermine
            int index = (int)Math.floor(Math.random() * (mine_count));
            x = minex[index];
            y = miney[index];
            minefield[x][y].supermine = 1;
        }

        saveMinefield();
    }

    private void saveMinefield(){
        BufferedWriter bwr = null;
        try {
            bwr = new BufferedWriter(new FileWriter("./mines.txt"));
            for(int i=0;i<grid_size;i++){
                for(int j=0;j<grid_size;j++){
                    if(minefield[i][j].mine==1){
                        String towrite = i+","+j+","+minefield[i][j].supermine+"\n";
                        bwr.write(towrite);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {if (bwr != null) {bwr.close();}
            }
            catch (IOException e) {
            }
        }
    }

    public void showMinefield(){ //for debugging
        for(int i=0;i<grid_size;i++){
            for(int j=0;j<grid_size;j++){
                System.out.print(minefield[i][j].mine + " ");
            }
            System.out.println();
        }
    }

    public boolean gameWon(){
        for(int i=0;i<grid_size;i++){
            for(int j=0;j<grid_size;j++){
                if(!minefield[i][j].opened && minefield[i][j].mine==0) return false;
            }
        }
        return true;
    }

    public Minefield(String path){
        try{
            int[] settings;
            settings = ReadSettings(path); //throws InvalidDescriptionException
            setParameters(settings); //throws InvalidValueException
        }
        catch (InvalidDescriptionException e){
            e.printStackTrace();
        }
        catch(InvalidValueException e){
            e.printStackTrace();
        }

        minefield = new Tile[grid_size][grid_size];
        for(int i=0;i<grid_size;i++){
            for(int j=0;j<grid_size;j++){
                Tile tile = new Tile();
                minefield[i][j] = tile;
            }
        }
    }

}
