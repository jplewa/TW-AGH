package ex3;

public class MsgProducer implements Runnable {
    private MsgBuffer msgBuffer;

    public MsgProducer(MsgBuffer msgBuffer) {
        this.msgBuffer = msgBuffer;
    }

    public void run() {
        for (int i = 0; i < 100; i++) {
            msgBuffer.put("message " + i);
        }
    }
}