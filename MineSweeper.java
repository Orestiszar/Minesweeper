import myUI.*;
public class MineSweeper extends Thread{
    private static Minefield minefield;
    private static MyUI ui;

    public static void main(String[] args){
        minefield = new Minefield("./medialab/SCENARIO1.txt");
        minefield.setMinefield();
//        minefield.showMinefield();
//        CountDown temp = new CountDown(minefield.getSettings()[3]);
//        CountDown temp = new CountDown(5);
//        Thread mytemp = temp.t;
//        mytemp.join();
//        System.out.println("End");
        ui = new MyUI();
        ui.launch(args);
    }
}
