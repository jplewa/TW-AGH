package ex1;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

class Buffer {

    private final Integer bufferSize;

    private ArrayList<Character> buffer;
    private ArrayList<Semaphore> semaphores;

    private Semaphore output = new Semaphore(1);

    Buffer(Integer bufferSize) {
        this.bufferSize = bufferSize;
        this.buffer = new ArrayList<>();
        this.semaphores = new ArrayList<>();
        this.semaphores.add(0, new Semaphore(bufferSize));
        this.buffer.add(0, 'A');
        for (int i = 1; i < bufferSize; i++) {
            this.semaphores.add(i, new Semaphore(0));
            this.buffer.add(i, 'A');
        }
        this.semaphores.add(bufferSize, new Semaphore(0));
    }

    void process(int index, char value) {
        this.buffer.set(index, value);
        try {
            output.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (char elem : buffer) {
            System.out.print(elem + " ");
        }
        System.out.println();
        output.release();
    }


    Integer getBufferSize() {
        return bufferSize;
    }

    void acquireSemaphoreAt(int index) {
        try {
            this.semaphores.get(index).acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void releaseSemaphoreAt(int index) {
        this.semaphores.get(index).release();
    }
}
