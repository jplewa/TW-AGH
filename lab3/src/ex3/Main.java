package ex3;

import java.util.ArrayList;

class Main {
    public static void main(String[] args) throws InterruptedException {
        Waiter waiter = new Waiter();
        ArrayList<Thread> clientThreads = new ArrayList<>();

        clientThreads.add(new Thread(new CoupleMember(waiter, 1, 'A')));
        clientThreads.add(new Thread(new CoupleMember(waiter, 1, 'B')));

        clientThreads.add(new Thread(new CoupleMember(waiter, 2, 'A')));
        clientThreads.add(new Thread(new CoupleMember(waiter, 2, 'B')));

        clientThreads.add(new Thread(new CoupleMember(waiter, 3, 'A')));
        clientThreads.add(new Thread(new CoupleMember(waiter, 3, 'B')));

        clientThreads.add(new Thread(new CoupleMember(waiter, 4, 'A')));
        clientThreads.add(new Thread(new CoupleMember(waiter, 4, 'B')));

        clientThreads.add(new Thread(new CoupleMember(waiter, 5, 'A')));
        clientThreads.add(new Thread(new CoupleMember(waiter, 6, 'B')));

        for (Thread clientThread : clientThreads) {
            clientThread.start();
        }
        for (Thread clientThread : clientThreads) {
            clientThread.join();
        }
    }
}
