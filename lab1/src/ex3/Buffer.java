package ex3;

class Buffer {
    private String message;
    private boolean full;

    Buffer() {
        this.message = "";
        this.full = false;
    }

    synchronized void put(String message) {
        while (full) {
            try {
                wait();
            } catch (InterruptedException e) {
                //Thread.currentThread().interrupt();
            }
        }
        this.full = true;
        this.message = message;
        notifyAll();
    }

    synchronized String take() {
        while (!full) {
            try {
                wait();
            } catch (InterruptedException e) {
                //Thread.currentThread().interrupt();
            }
        }
        this.full = false;
        String found_message = this.message;
        notifyAll();
        return (found_message);
    }
}
