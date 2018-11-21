package ex2;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Main {
    public static void main(String[] args) throws InterruptedException {

        PrintingMonitor printingMonitor = new PrintingMonitor(2);
        List<Thread> printingThreads =
                IntStream
                        .range(0, 10)
                        .boxed()
                        .map(id -> new Thread(new PersonThatWantsToPrintStuff(printingMonitor, id)))
                        .collect(Collectors.toList());

        for (Thread printingThread : printingThreads) {
            printingThread.start();
        }
        for (Thread printingThread : printingThreads) {
            printingThread.join();
        }
    }
}
