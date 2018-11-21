package ex1;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class Main {
    public static void main(String[] args) throws InterruptedException {
        Buffer buffer = new Buffer();

        List<Thread> consumers =
                Stream
                        .generate(() -> 100)
                        .limit(10)
                        .map(x -> new Thread(new Consumer(buffer, x)))
                        .collect(Collectors.toList());

        List<Thread> producers =
                IntStream
                        .range(0, 10)
                        .boxed()
                        .limit(10)
                        .map(x -> new Thread(new Producer(buffer, 100, x)))
                        .collect(Collectors.toList());

        for (Thread producer : producers) {
            producer.start();
        }
        for (Thread consumer : consumers) {
            consumer.start();
        }
        for (Thread producer : producers) {
            producer.join();
        }
        for (Thread consumer : consumers) {
            consumer.join();
        }
    }
}
