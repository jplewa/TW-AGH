package ex3;

public class Producer implements Runnable {
    private Buffer buffer;

    public Producer(Buffer buffer) {
        this.buffer = buffer;
    }

    public void run() {
        for (int i = 0; i < 100; i++) {
            buffer.put("message " + i);
            System.out.format("MESSAGE SENT: %s%n", "message " + i);
        }

    }
}