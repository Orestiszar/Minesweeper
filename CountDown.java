public class CountDown implements Runnable{
    public Thread t;
    public int seconds;
    public void run()
    {
        try{
            while (true){
                System.out.println("Thread Started Running..."+seconds);
                if(seconds==0){
                    return;
                }
                t.sleep(1000);
                seconds--;
            }
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }

    }
    public CountDown(int seconds){
        this.seconds=seconds;
        t = new Thread(this,"Countdown");
        t.start();
    }
}