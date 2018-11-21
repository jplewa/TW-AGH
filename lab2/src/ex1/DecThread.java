package ex1;

public class DecThread extends Thread {
    private Counter counter;

    DecThread(Counter counter) {
        this.counter = counter;
    }

    public void run() {
        for (int i = 0; i < 100; i++) {
            this.counter.dec();
        }
    }
}
