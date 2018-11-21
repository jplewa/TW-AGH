package ex2;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NaiveBuffer {

    private Integer capacity;
    private Integer occupied;

    private ArrayList<Character> contents;

    private Lock lock;
    private Condition availableForWriting;
    private Condition availableForReading;

    NaiveBuffer(Integer capacity) {
        this.lock = new ReentrantLock();
        this.availableForReading = this.lock.newCondition();
        this.availableForWriting = this.lock.newCondition();
        this.capacity = capacity;
        this.contents = new ArrayList<>(capacity);
        for (int i = 0; i < this.capacity; i++) {
            this.contents.add(i, '_');
        }
        this.occupied = 0;
    }

    long put(int len, Character value) {
        int lenCopy = len;
        this.lock.lock();
        long start = System.nanoTime();
        long end = start;
        try {
            while (len > capacity - occupied) {
                this.availableForWriting.await();
            }
            end = System.nanoTime();
            this.occupied += len;
            for (int i = 0; i < this.capacity && len > 0; i++) {
                if (this.contents.get(i).equals('_')) {
                    this.contents.set(i, value);
                    len--;
                }
            }
            this.availableForReading.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.print(this);
            System.out.println(" | ADDED " + lenCopy);
            this.lock.unlock();
        }
        return (end - start);
    }

    long take(int len) {
        //ArrayList<Character> results = new ArrayList<>();
        int lenCopy = len;
        this.lock.lock();
        long start = System.nanoTime();
        long end = start;
        try {
            while (len > occupied) {
                this.availableForReading.await();
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
