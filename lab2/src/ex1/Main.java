package ex1;

public class Main {

    public static void main(String[] args) {
        BinarySemaphore binarySemaphore = new BinarySemaphore();
        Counter counter = new Counter(binarySemaphore);
        IncThread incThread = new IncThread(counter);
        DecThread decThread = new DecThread(counter);

        incThread.start();
        decThread.start();

        try {
            incThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            decThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
