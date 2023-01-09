public class MineSweeper extends Thread{
    private static Minefield minefield;

    public static void main(String[] args){
        minefield = new Minefield("./medialab/SCENARIO1.txt");
        minefield.setMinefield();
//        CountDown temp = new CountDown(minefield.getSettings()[3]);
        CountDown temp = new CountDown(5);
        Thread mytemp = temp.t;
        while(true){
            if(mytemp.isAlive()){
                System.out.println("Alive");
                try{
                    Thread.sleep(500);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
            else{
                System.out.println("Dead");
                break;
            }
        }
    }
}
