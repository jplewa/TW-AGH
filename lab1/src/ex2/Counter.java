package ex2;

public class Counter {

    private int value;

    Counter() {
        this.value = 0;
    }

    synchronized void inc() {
        this.value++;
    }

    synchronized void dec() {
        this.value--;
    }

    public synchronized String toString() {
        return String.valueOf(this.value);
    }
}
