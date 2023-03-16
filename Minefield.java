import myExceptions.InvalidDescriptionException;
import myExceptions.InvalidValueException;

import java.io.*;

import static java.lang.Integer.parseInt;

public class Minefield {
    private int difficulty,grid_size,mine_count,seconds,supermine;
    private Tile[][] minefield;

    /**
     * Returns the Tile at the location specified by row and col.
     * @param row the row of the Tile in the minefield
     * @param col the column of the Tile in the minefield
     * @return the Tile object
     */
    public Tile getTile(int row, int col){
        return minefield[row][col];
    }

    /**
     * Sets the Tile at the specified location as opened.
     * @param row the row of the Tile in the minefield
     * @param col the column of the Tile in the minefield
     */
    public void setTileOpened(int row, int col){
        minefield[row][col].opened=true;
    }

    /**
     * Sets the flag state for the Tile in the specified location.
     * @param row the row of the Tile in the minefield
     * @param col the column of the Tile in the minefield
     * @param state the state of the Tile flag
     */
    public void setTileFlag(int row, int col, boolean state){
        minefield[row][col].flagged=state;
    }

    /**
     * Returns the number of mines around a Tile.
     * @param row the row of the Tile in the minefield
     * @param col the column of the Tile in the minefield
     * @return the number of mines around the Tile
     */
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

    /**
     * Get the current minefield settings.
     * @return array of settings.
     */
    public int[] getSettings(){
        int[] result = new int[5];
        result[0] = difficulty;
        result[1] = grid_size;
        result[2] = mine_count;
        result[3] = seconds;
        result[4] = supermine;
        return result;
    }

    /**
     * Reads a Scenario specified in the given path and makes sure it has exactly 4 parameters.
     * @param path the Scenario location
     * @return the scenario settings in an int array
     * @throws InvalidDescriptionException in case there is an invalid number of parameters
     * @throws FileNotFoundException in case the file is not found in the specified location
     */
    public int[] ReadSettings(String path) throws InvalidDescriptionException, FileNotFoundException {//return data from setting file in an int array
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
        }
        catch (FileNotFoundException e){
            throw new FileNotFoundException();
        }
        catch (IOException e) {
            System.out.println("IO Exception");
        }
        finally {
            try {if (br != null) {br.close();}
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(i<4){
            throw new InvalidDescriptionException("Invalid Scenario");
        }
        return data;
    }

    /**
     * Checks the settings provided in an array form. It then sets the correct grid size based on the difficulty level.
     * @param settings an array of integers representing the game difficulty, mine count, time limit, and the possibility of a supermine
     * @throws InvalidValueException in case the settings are invalid
     */
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

    /**
     * Sets up the minefield for a new game by resetting all tiles.
     * It then randomly selects mine positions based on the already set mine_count and grid_size values.
     * If the difficulty is '2', a random mine is selected as a supermine.
     * The mines are also saved locally using the saveMinefield() function.
     */
    public void setMinefield(){

        for(int i=0;i<grid_size;i++){
            for(int j=0;j<grid_size;j++){
                minefield[i][j].mine=0;
                minefield[i][j].supermine=0;
                minefield[i][j].opened=false;
                minefield[i][j].flagged=false;
            }
        }

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

    /**
     * Saves the mine positions to the file "mines.txt" for future use.
     * Each line of the file represents a mine.
     * The first two numbers represent its row and column in the grid.
     * The third number states if the mine is a supermine on not.
     */
    private void saveMinefield(){
        BufferedWriter bwr = null;
        try {

            File myObj = new File("./mines.txt");
            myObj.createNewFile();

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

    /**
     * Returns the number of flagged Tiles in the Minefield.
     * @return the number of flagged Tiles in the Minefield
     */
    public int getFlaggedMines(){
        int result = 0;
        for(int i=0;i<grid_size;i++){
            for(int j=0;j<grid_size;j++){
                if(minefield[i][j].flagged){
                    result++;
                }
            }
        }
        return result;
    }

    /**
     * Checks if the game has been won by the player by iterating over the Minefield.
     * Returns true if all non-mine cells have been opened, indicating that the game has been won.
     * Returns false otherwise.
     */
    public boolean gameWon(){
        for(int i=0;i<grid_size;i++){
            for(int j=0;j<grid_size;j++){
                if(!minefield[i][j].opened && minefield[i][j].mine==0) return false;
            }
        }
        return true;
    }

    /**
     * Constructs a new Minefield object by reading the Scenario settings from the specified file path,
     * validating them and setting them accordingly. The constructor also initializes the minefield
     * array with the appropriate size based on the selected difficulty.
     * @param path the path to the file scenario
     * @throws InvalidDescriptionException if the scenario has an invalid format
     * @throws InvalidValueException if the scenario has invalid values
     * @throws FileNotFoundException if the scenario file cannot be found
     */
    public Minefield(String path) throws InvalidDescriptionException, InvalidValueException, FileNotFoundException{

        int[] settings;
        settings = ReadSettings(path); //throws InvalidDescriptionException
        setParameters(settings); //throws InvalidValueException

        minefield = new Tile[grid_size][grid_size];
        for(int i=0;i<grid_size;i++){
            for(int j=0;j<grid_size;j++){
                Tile tile = new Tile();
                minefield[i][j] = tile;
            }
        }
    }

}
