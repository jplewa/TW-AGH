package ex1;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Consumer implements Runnable {
    private final Buffer buffer;
    private final Integer messagesToReceive;

    Consumer(Buffer buffer, Integer messagesToReceive) {
        this.buffer = buffer;
        this.messagesToReceive = messagesToReceive;
    }

    public void run() {
        IntStream
                .range(0, this.messagesToReceive)
                .boxed()
                .collect(Collectors.toList())
                .forEach(messageNumber -> buffer.take());
    }
}

