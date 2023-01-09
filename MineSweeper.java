import myExceptions.InvalidDescriptionException;
import myExceptions.InvalidValueException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static java.lang.Integer.parseInt;

public class MineSweeper {
    private static int difficulty,grid_size,mine_count,seconds,supermine;
    private static Minefield minefield;
    private static int[] ReadSettings(String path) throws InvalidDescriptionException {//return data from setting file in an int array
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

    private static void setParameters(int[] settings) throws InvalidValueException{
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

    public static void main(String[] args){
        try{
            int[] settings;
            settings = ReadSettings("./medialab/SCENARIO1.txt"); //throws InvalidDescriptionException
            setParameters(settings); //throws InvalidValueException
        }
        catch (InvalidDescriptionException e){
            e.printStackTrace();
        }
        catch(InvalidValueException e){
            e.printStackTrace();
        }
        minefield = new Minefield(grid_size, difficulty);
        minefield.setMinefield(mine_count);
//        minefield.setMinefield();
    }
}
