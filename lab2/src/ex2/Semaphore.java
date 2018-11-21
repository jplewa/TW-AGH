package ex2;

public class Semaphore {
    private int capacity;
    private int counter;

    public Semaphore(int capacity){
        this.capacity = capacity;
        this.counter = capacity;
    }

    synchronized public void lock(){
        while (this.counter == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.counter--;
        notifyAll();
    }

    synchronized public void unlock(){
        while (this.counter == this.capacity) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.counter++;
        notifyAll();
    }

    public String toString(){
        return("remaining baskets: " + this.counter + "/" + this.capacity);
    }
}
