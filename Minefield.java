import static java.lang.Integer.parseInt;

public class Minefield {
    private int grid_size,difficulty;

    private Tile[][] minefield;

    public void setMinefield(int mine_count){
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
//        BufferedWriter bwr = null;
//        try {
//            bwr = new BufferedWriter(new FileWriter("./mines.txt"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        finally {
//            try {if (bwr != null) {bwr.close();}
//            }
//            catch (IOException e) {
//            }
//        }

        for(int i=0;i<grid_size;i++){
            for(int j=0;j<grid_size;j++){
                if(minefield[i][j].mine==1){
                    String towrite = i+","+j+","+minefield[i][j].supermine+"\n";
                    System.out.print(towrite);
                }
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
    public Minefield(int grid_size, int difficulty){
        this.difficulty = difficulty;
        this.grid_size = grid_size;
        minefield = new Tile[grid_size][grid_size];
        for(int i=0;i<grid_size;i++){
            for(int j=0;j<grid_size;j++){
                Tile tile = new Tile();
                minefield[i][j] = tile;
            }
        }
    }
}
