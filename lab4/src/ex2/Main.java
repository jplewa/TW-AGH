package ex2;

import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        /*
        NaiveBuffer naiveBuffer = new NaiveBuffer(10);

        Producer p1 = new Producer(naiveBuffer, 'A', 5);
        Producer p2 = new Producer(naiveBuffer, 'B', 5);

        Consumer c1 = new Consumer(naiveBuffer, 5);
        Consumer c2 = new Consumer(naiveBuffer, 5);


        Thread t1 = new Thread(p1);
        Thread t2 = new Thread(p2);
        Thread t3 = new Thread(c1);
        Thread t4 = new Thread(c2);

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        t1.join();
        t2.join();
        t3.join();
        t4.join();
    */


        FairBuffer fairBuffer= new FairBuffer(10);

        Producer p1 = new Producer(fairBuffer, 'A', 5, 0);
        Producer p2 = new Producer(fairBuffer, 'B', 5, 1);

        Consumer c1 = new Consumer(fairBuffer, 5, 0);
        Consumer c2 = new Consumer(fairBuffer, 5, 1);

        Thread t1 = new Thread(p1);
        Thread t2 = new Thread(p2);
        Thread t3 = new Thread(c1);
        Thread t4 = new Thread(c2);

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        t1.join();
        t2.join();
        t3.join();
        t4.join();


        // int[] Ms = {1000, 10000, 100000};
        // int[] PKs = {10, 100, 1000};

        /*
        int M = 10000;
        int P = 100;
        int K = P;
        NaiveBuffer naiveBuffer = new NaiveBuffer(2 * M);
        ArrayList<Thread> threads = new ArrayList<>();
        for (int i = 0; i < P; i++){
            threads.add(new Thread(new Producer(naiveBuffer, 'A', M)));
        }

        for (int i = 0; i < K; i++){
            threads.add(new Thread(new Consumer(naiveBuffer, M)));
        }

        for (int i = 0; i < P + K; i++){
            threads.get(i).start();
        }

        for (int i = 0; i < P + K; i++){
            threads.get(i).join();
        }

        */


    }
}
