package ex3;

public class MsgConsumer implements Runnable {
    MsgBuffer msgBuffer;

    public MsgConsumer(MsgBuffer msgBuffer) {
        this.msgBuffer = msgBuffer;
    }

    public void run() {
        for (int i = 0; i < 50; i++) {
            String message = msgBuffer.take();
        }
    }
}

