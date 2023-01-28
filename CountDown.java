public class CountDown implements Runnable{
    public static Thread mythread;
    public int seconds;
    public void run()
    {
        try{
            System.out.println("Thread Started Running..."+seconds);
            while (true){
                if(seconds==0){
                    Controller.timer_label.setText("You Lose :(");
                    return;
                }
                mythread.sleep(1000);
                Controller.timer_label.setText(Integer.toString(seconds));
                seconds--;
            }
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }

    }
    public CountDown(int seconds){
        this.seconds=seconds;
    }
}