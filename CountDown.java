import javafx.application.Platform;

public class CountDown implements Runnable{
    public static Thread mythread;
    private int seconds;
    private Controller controller;
    public void run()
    {
        try{
//            System.out.println("Thread Started Running...");
            while (!Thread.interrupted()){
                if(seconds==0){
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            controller.gameOver(false);
                        }
                    });
                    return;
                }
                mythread.sleep(1000);

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        controller.timer_label.setText(Integer.toString(seconds));
                    }
                });
                seconds--;
            }
        }
        catch (InterruptedException e){
        }

    }
    public CountDown(int seconds, Controller controller){
        this.seconds=seconds;
        this.controller=controller;
    }
}