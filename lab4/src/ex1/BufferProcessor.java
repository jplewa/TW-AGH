package ex1;

public class BufferProcessor implements Runnable {

    private Integer index;
    private Character result;
    private Buffer buffer;
    private Integer processed;
    private Long processingTime;

    BufferProcessor(Integer index, Buffer buffer, Character result) {
        this.result = result;
        this.index = index;
        this.buffer = buffer;
        this.processed = 0;
        this.processingTime = (long) (Math.random() * 2000);
    }

    public void run() {
        while (!this.processed.equals(this.buffer.getBufferSize())) {
            try {
                buffer.acquireSemaphoreAt(index);
                Thread.sleep(processingTime);
                buffer.process(processed, result);
                processed++;
                buffer.releaseSemaphoreAt(index + 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
