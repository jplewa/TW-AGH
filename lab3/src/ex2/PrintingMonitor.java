package ex2;

import java.util.HashSet;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


class PrintingMonitor {
    private final Lock lock;
    private final Condition notAllPrintersUsed;
    private final HashSet<Integer> availablePrinters;

    PrintingMonitor(Integer numberOfPrinters) {
        this.lock = new ReentrantLock();
        this.notAllPrintersUsed = lock.newCondition();
        this.availablePrinters =
                IntStream
                        .range(0, numberOfPrinters)
                        .boxed()
                        .collect(Collectors.toCollection(HashSet::new));
    }

    Integer use() {
        lock.lock();
        while (this.availablePrinters.size() == 0) {
            try {
                notAllPrintersUsed.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Integer foundPrinter = this.availablePrinters.iterator().next();
        this.availablePrinters.remove(foundPrinter);
        lock.unlock();
        return foundPrinter;
    }

    void free(int printerNumber) {
        lock.lock();
        this.availablePrinters.add(printerNumber);
        if (this.availablePrinters.size() == 1) {
            notAllPrintersUsed.signal();
        }
        lock.unlock();
    }
}
