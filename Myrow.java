import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Myrow {
    public SimpleIntegerProperty total_mines,tries,time;
    public SimpleStringProperty winner;

    public Myrow(int total_mines, int tries, int time, String winner) {
        this.total_mines = new SimpleIntegerProperty(total_mines);
        this.tries = new SimpleIntegerProperty(tries);
        this.time = new SimpleIntegerProperty(time);
        this.winner = new SimpleStringProperty(winner);
    }

    public int getTotal_mines() {
        return total_mines.get();
    }

    public SimpleIntegerProperty total_minesProperty() {
        return total_mines;
    }

    public int getTries() {
        return tries.get();
    }

    public SimpleIntegerProperty triesProperty() {
        return tries;
    }

    public int getTime() {
        return time.get();
    }

    public SimpleIntegerProperty timeProperty() {
        return time;
    }

    public String getWinner() {
        return winner.get();
    }

    public SimpleStringProperty winnerProperty() {
        return winner;
    }
}
