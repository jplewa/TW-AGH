package ex2;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FairBuffer {

    private Integer capacity;
    private Integer occupied;

    private ArrayList<Character> contents;

    private Lock lock;
    private Condition availableForWriting;
    private Condition availableForReading;
    private Condition firstProducer;
    private Condition firstConsumer;

    //private boolean firstProducerWaiting;
    //private boolean firstConsumerWaiting;

    private LinkedList<Integer> producerQueue;
    private LinkedList<Integer> consumerQueue;

    FairBuffer(Integer capacity) {
        this.lock = new ReentrantLock();
        this.availableForReading = this.lock.newCondition();
        this.availableForWriting = this.lock.newCondition();
        this.firstConsumer = this.lock.newCondition();
        this.firstProducer = this.lock.newCondition();
        //this.firstConsumerWaiting = false;
        //this.firstProducerWaiting = false;
        this.consumerQueue = new LinkedList<>();
        this.producerQueue = new LinkedList<>();

        this.capacity = capacity;
        this.contents = new ArrayList<>(capacity);
        for (int i = 0; i < this.capacity; i++) {
            this.contents.add(i, '_');
        }
        this.occupied = 0;
    }

    long put(int len, Character value, int id) {
        int lenCopy = len;
        this.lock.lock();
        long start = System.nanoTime();
        long end = start;
        try {
            this.producerQueue.add(id);
            while (!producerQueue.getFirst().equals(id)){
                this.availableForWriting.await();
            }
            while (len > capacity - occupied) {
                this.firstProducer.await();
            }
            end = System.nanoTime();
            this.occupied += len;
            for (int i = 0; i < this.capacity && len > 0; i++) {
                if (this.contents.get(i).equals('_')) {
                    this.contents.set(i, value);
                    len--;
                }
            }
            this.producerQueue.removeFirst();
            this.firstConsumer.signal();
            this.availableForWriting.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.print(this);
            System.out.println(" | ADDED " + lenCopy);
            this.lock.unlock();
        }
        return (end - start);
    }

    long take(int len, int id) {
        //ArrayList<Character> results = new ArrayList<>();
        int lenCopy = len;
        this.lock.lock();
        long start = System.nanoTime();
        long end = start;
        try {
            this.consumerQueue.add(id);
            while (!consumerQueue.getFirst().equals(id)){
                this.availableForReading.await();
            }
            while (len > occupied) {
                this.firstConsumer.await();
            }
            end = System.nanoTime();
            occupied -= len;
            for (int i = 0; i < this.capacity && len > 0; i++) {
                if (!this.contents.get(i).equals('_')) {
                    //results.add(this.contents.get(i));
                    this.contents.set(i, '_');
                    len--;
                }
            }
            this.consumerQueue.removeFirst();
            this.firstProducer.signal();
            this.availableForReading.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.print(this);
            System.out.println(" | TAKEN " + lenCopy);
            this.availableForWriting.signalAll();
            this.lock.unlock();
        }
        //return results;
        return (end - start);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < this.capacity; i++) {
            stringBuilder.append(this.contents.get(i)).append(" ");
        }
        return stringBuilder.toString();
    }

    int getOccupied(){
        return this.occupied;
    }
}
