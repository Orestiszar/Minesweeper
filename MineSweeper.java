public class MineSweeper {
    private static Minefield minefield;

    public static void main(String[] args){
        minefield = new Minefield("./medialab/SCENARIO1.txt");
        minefield.setMinefield();
//        minefield.setMinefield();
    }
}
