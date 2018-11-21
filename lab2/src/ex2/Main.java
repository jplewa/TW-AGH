package ex2;

public class Main {

    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(3);

        Customer customer1 = new Customer(1, 10000, semaphore);
        Customer customer2 = new Customer(2, 10000, semaphore);
        Customer customer3 = new Customer(3, 10000, semaphore);


        customer1.start();
        customer2.start();
        customer3.start();

        try {
            customer1.join();
            customer2.join();
            customer3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
