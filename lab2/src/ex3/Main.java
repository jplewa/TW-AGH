package ex3;

import ex1.BinarySemaphore;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        BinarySemaphore binarySemaphore = new BinarySemaphore();
        
        MsgBuffer msgBuffer = new MsgBuffer(binarySemaphore);
        MsgProducer msgProducer = new MsgProducer(msgBuffer);
        MsgConsumer msgConsumer1 = new MsgConsumer(msgBuffer);
        MsgConsumer msgConsumer2 = new MsgConsumer(msgBuffer);

        Thread consumerThread1 = new Thread(msgConsumer1);
        Thread consumerThread2 = new Thread(msgConsumer2);
        Thread producerThread = new Thread(msgProducer);

        producerThread.start();
        consumerThread1.start();
        consumerThread2.start();

        producerThread.join();
        consumerThread1.join();
        consumerThread2.join();

    }
}
