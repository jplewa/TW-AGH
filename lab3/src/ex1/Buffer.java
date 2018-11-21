package ex1;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Buffer {
    private final Lock lock;
    private final Condition notFull;
    private final Condition notEmpty;
    private Boolean full;
    private String message;

    Buffer() {
        this.message = "";
        this.full = false;
        this.lock = new ReentrantLock();
        this.notFull = lock.newCondition();
        this.notEmpty = lock.newCondition();
    }

    void put(String message) {
        lock.lock();
        try {
            while (full) {
                notFull.await();
            }
            this.full = true;
            this.message = message;
            System.out.format("SENT: %s%n", message);
            notEmpty.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    String take() {
        lock.lock();
        String found_message = "";
        try {
            while (!full) {
                notEmpty.await();
            }
            this.full = false;
            found_message = this.message;
            notFull.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        System.out.format("RECEIVED: %s%n", found_message);
        return found_message;
    }
}
