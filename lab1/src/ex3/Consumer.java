package ex3;

public class Consumer implements Runnable {
    private Buffer buffer;

    public Consumer(Buffer buffer) {
        this.buffer = buffer;
    }

    public void run() {
        for (int i = 0; i < 50; i++) {
            String message = buffer.take();
            System.out.format("MESSAGE RECEIVED: %s%n", message);
        }
    }
}

