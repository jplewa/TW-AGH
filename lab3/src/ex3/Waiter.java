package ex3;

import java.util.HashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Waiter {
    private final Lock lock;
    private final Condition tableOccupied;
    private final HashMap<Integer, CoupleState> coupleState;
    private final HashMap<Integer, Condition> coupleCondition;
    private Integer coupleAtTheTableID;
    //private Boolean tableAvailable;

    Waiter() {
        this.lock = new ReentrantLock();
        this.tableOccupied = lock.newCondition();
        this.coupleCondition = new HashMap<>();
        this.coupleState = new HashMap<>();
        this.coupleAtTheTableID = -1;
        //this.tableAvailable = true;
    }

    void request(Integer coupleID) throws InterruptedException {
        lock.lock();
        // a couple's first visit to the restaurant
        // initialize condition and add empty state
        if (!this.coupleState.keySet().contains(coupleID)) {
            this.coupleState.put(coupleID, CoupleState.BOTH_LEFT);
            this.coupleCondition.put(coupleID, lock.newCondition());
        }
        // the other person is still at the table since the previous visit,
        // wait for them to leave in order to begin a new visit
        if (this.coupleState.get(coupleID) == CoupleState.ONE_LEFT) {
            this.coupleCondition.get(coupleID).await();
        }
        // if you're the first one to arrive at the restaurant
        if (this.coupleState.get(coupleID) == CoupleState.BOTH_LEFT) {
            // announce you're waiting
            this.coupleState.put(coupleID, CoupleState.ONE_WAITING);
            // wait until they get the table for the two of you
            if (!this.coupleAtTheTableID.equals(coupleID)) {
                this.coupleCondition.get(coupleID).await();
            }
            // record that you're both at the table
            this.coupleState.put(coupleID, CoupleState.ENJOYING_DINNER);
        }
        // if you're the second person arriving at the restaurant
        else if (this.coupleState.get(coupleID) == CoupleState.ONE_WAITING) {
            // wait for the table
            while (this.coupleAtTheTableID != -1) {
                this.tableOccupied.await();
            }
            this.coupleAtTheTableID = coupleID;
            // let the other person know you got the table
            coupleCondition.get(coupleID).signal();
        }
        lock.unlock();
    }

    void leave() {
        lock.lock();
        // if you're the first person leaving
        if (this.coupleState.get(this.coupleAtTheTableID) == CoupleState.ENJOYING_DINNER) {
            this.coupleState.put(this.coupleAtTheTableID, CoupleState.ONE_LEFT);
        }
        // if you're the last one to leave
        else if (this.coupleState.get(this.coupleAtTheTableID) == CoupleState.ONE_LEFT) {
            this.coupleState.put(this.coupleAtTheTableID, CoupleState.BOTH_LEFT);
            // make sure the other person knows they can return to the restaurant for a new visit
            this.coupleCondition.get(this.coupleAtTheTableID).signal();
            // free the table and let other couples know
            this.coupleAtTheTableID = -1;
            this.tableOccupied.signalAll();
        }
        lock.unlock();
    }
}
