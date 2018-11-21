package ex1;

public class IncThread extends Thread {
    private Counter counter;

    IncThread(Counter counter) {
        this.counter = counter;
    }

    public void run() {
        for (int i = 0; i < 100000000; i++) {
            this.counter.inc();
        }
    }
}
