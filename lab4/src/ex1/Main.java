package ex1;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        Buffer buffer = new Buffer(50);

        BufferProcessor processor1 = new BufferProcessor(0, buffer, 'B');
        BufferProcessor processor2 = new BufferProcessor(1, buffer, 'C');
        BufferProcessor processor3 = new BufferProcessor(2, buffer, 'D');
        BufferProcessor processor4 = new BufferProcessor(3, buffer, 'E');
        BufferProcessor processor5 = new BufferProcessor(4, buffer, 'F');
        BufferProcessor processor6 = new BufferProcessor(5, buffer, 'G');
        BufferProcessor processor7 = new BufferProcessor(6, buffer, 'H');

        Thread t1 = new Thread(processor1);
        Thread t2 = new Thread(processor2);
        Thread t3 = new Thread(processor3);
        Thread t4 = new Thread(processor4);
        Thread t5 = new Thread(processor5);
        Thread t6 = new Thread(processor6);
        Thread t7 = new Thread(processor7);

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        t6.start();
        t7.start();

        t1.join();
        t2.join();
        t3.join();
        t4.join();
        t5.join();
        t6.join();
        t7.join();
    }
}
