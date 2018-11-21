package ex1;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Producer implements Runnable {
    private final Buffer buffer;
    private final Integer messagesToProduce;
    private final Integer producerNumber;

    Producer(Buffer buffer, Integer messagesToProduce, Integer producerNumber) {
        this.buffer = buffer;
        this.messagesToProduce = messagesToProduce;
        this.producerNumber = producerNumber;
    }

    public void run() {
        for (Integer messageNumber : IntStream.range(0, this.messagesToProduce).boxed().collect(Collectors.toList())) {
            buffer.put(String.format("message #%d from producer #%d ", messageNumber, producerNumber));
        }
    }
}