package ex2;

public class Main {

    public static void main(String[] args) {
        Counter counter = new Counter();
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
        System.out.println(counter);
    }
}
