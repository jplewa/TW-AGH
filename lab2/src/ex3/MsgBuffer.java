package ex3;

import ex1.BinarySemaphore;

public class MsgBuffer {
    private String message;
    private BinarySemaphore binarySemaphore;

    public MsgBuffer(BinarySemaphore binarySemaphore) {
        this.message = "";
        this.binarySemaphore = binarySemaphore;
    }

    synchronized void put(String message) {
        binarySemaphore.lock();
        this.message = message;
        System.out.println("SENT: " + message);
        binarySemaphore.unlock();
    }

    synchronized String take() {
        binarySemaphore.lock();
        String foundMessage = this.message;
        System.out.println("RECEIVED: " + foundMessage);
        binarySemaphore.unlock();
        return (foundMessage);
    }
}
