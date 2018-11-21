package ex1;

public class Counter {

    private int value;

    Counter() {
        this.value = 0;
    }

    void inc() {
        this.value++;
    }

    void dec() {
        this.value--;
    }

    public String toString() {
        return String.valueOf(this.value);
    }
}
