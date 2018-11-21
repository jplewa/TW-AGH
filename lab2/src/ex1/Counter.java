package ex1;

public class Counter {

    private int value;
    private BinarySemaphore binarySemaphore;

    Counter(BinarySemaphore binarySemaphore) {
        this.binarySemaphore = binarySemaphore;
        this.value = 0;
    }

    void inc() {
        binarySemaphore.lock();
        this.value++;
        System.out.println(this);
        binarySemaphore.unlock();
    }

    void dec() {
        binarySemaphore.lock();
        this.value--;
        System.out.println(this);
        binarySemaphore.unlock();
    }

    public String toString() {
        return "VALUE: " + String.valueOf(this.value);
    }
}
