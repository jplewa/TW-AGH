/*package ex2;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Simulation {

    private int M;
    private int P;
    private int K;

    Simulation(int M, int P, int K){
        this.M = M;
        this.P = P;
        this.K = K;
    }

    void runNaiveSimulation() throws InterruptedException, IOException {
        NaiveBuffer naiveBuffer = new NaiveBuffer(2 * M);

        ArrayList<Producer> producers = new ArrayList<>();
        ArrayList<Consumer> consumers = new ArrayList<>();

        ArrayList<Thread> threads = new ArrayList<>();


        for (int i = 0; i < P; i++){
            producers.add((new Producer(naiveBuffer, 'A', M / 2)));
            threads.add(new Thread(producers.get(i)));
        }

        for (int i = 0; i < K; i++){
            consumers.add(new Consumer(naiveBuffer, M / 2));
            threads.add(new Thread((consumers.get(i))));
        }

       for (int i = 0; i < P + K; i++){
           threads.get(i).start();
       }

        Thread.sleep(10000);

        for (int i = 0; i < P + K; i++){
            threads.get(i).stop();
        }


        int pSum = 0;
        for (int i = 0; i < P; i++){
            pSum += producers.get(i).times.size();
        }

        int cSum = 0;
        for (int i = 0; i < K; i++){
            cSum += consumers.get(i).times.size();
        }

        double pAverage = 0;
        for (int i = 0; i < P; i++){
            for (int j = 0; j < producers.get(i).times.size(); j++) {
                pAverage += (double) producers.get(i).times.get(j) / pSum;
            }
        }

        double cAverage = 0;
        for (int i = 0; i < K; i++){
            for (int j = 0; j < consumers.get(i).times.size(); j++) {
                cAverage += (double) consumers.get(i).times.get(j) / cSum;
            }

        }
        BufferedWriter writer = new BufferedWriter(new FileWriter("naive.txt", true));
        //writer.write("NAIVE\n");
        writer.write("M" + M + " P" + P + " K" + K + "\n");
        writer.write("P: " + pAverage + "\n");
        writer.write("K: " + cAverage + "\n");

        writer.close();
        //System.out.println();
        //System.out.println("P: " + pAverage);
        //System.out.println();
    }


    void runFairSimulation() throws InterruptedException, IOException {
        FairBuffer fairBuffer = new FairBuffer(2 * M);

        ArrayList<Producer> producers = new ArrayList<>();
        ArrayList<Consumer> consumers = new ArrayList<>();

        ArrayList<Thread> threads = new ArrayList<>();


        for (int i = 0; i < P; i++){
            producers.add((new Producer(fairBuffer, 'A', M / 2, i)));
            threads.add(new Thread(producers.get(i)));
        }

        for (int i = 0; i < K; i++){
            consumers.add(new Consumer(fairBuffer, M / 2, i));
            threads.add(new Thread((consumers.get(i))));
        }

        for (int i = 0; i < P + K; i++){
            threads.get(i).start();
        }

        Thread.sleep(10000);

        for (int i = 0; i < P + K; i++){
            threads.get(i).stop();
        }


        int pSum = 0;
        for (int i = 0; i < P; i++){
            pSum += producers.get(i).times.size();
        }

        int cSum = 0;
        for (int i = 0; i < K; i++){
            cSum += consumers.get(i).times.size();
        }

        double pAverage = 0;
        for (int i = 0; i < P; i++){
            for (int j = 0; j < producers.get(i).times.size(); j++) {
                pAverage += (double) producers.get(i).times.get(j) / pSum;
            }
        }

        double cAverage = 0;
        for (int i = 0; i < K; i++){
            for (int j = 0; j < consumers.get(i).times.size(); j++) {
                cAverage += (double) consumers.get(i).times.get(j) / cSum;
            }
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter("fair.txt", true));
        //writer.write("FAIR\n");
        writer.write("M" + M + " P" + P + " K" + K + "\n");
        writer.write("P: " + pAverage + "\n");
        writer.write("K: " + cAverage + "\n");
    }
}

*/