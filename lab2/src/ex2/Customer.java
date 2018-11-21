package ex2;

public class Customer extends Thread{
    private int customerId;
    private Semaphore semaphore;
    private int shoppingTrips;

    Customer(int customerId, int shoppingTrips, Semaphore semaphore){
        this.customerId = customerId;
        this.shoppingTrips = shoppingTrips;
        this.semaphore = semaphore;
    }

    public void run() {
        for (int i = 0; i < this.shoppingTrips; i++) {
            semaphore.lock();
            System.out.println(customerId + " took a basket | " + semaphore);
            try {
                Thread.sleep((long)(Math.random() * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            semaphore.unlock();
            System.out.println(customerId + " returned a basket | " + semaphore);
        }
    }
}
