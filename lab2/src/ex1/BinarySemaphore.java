package ex1;

public class BinarySemaphore {
    private boolean unlocked;

    public BinarySemaphore(){
        this.unlocked = true;
    }

    synchronized public void lock(){
        while (!unlocked) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.unlocked = false;
        //System.out.println(this);
        notifyAll();
    }

    synchronized public void unlock(){
        while (unlocked) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.unlocked = true;
        //System.out.println(this);
        notifyAll();
    }

    public String toString(){
        if (unlocked) return("unlocked");
        return("locked");
    }
}
