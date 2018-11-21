package ex3;

class CoupleMember implements Runnable {
    private final Integer coupleID;
    private final Character personID;
    private final Waiter waiter;

    CoupleMember(Waiter waiter, Integer coupleID, Character personID) {
        this.waiter = waiter;
        this.coupleID = coupleID;
        this.personID = personID;
    }

    private void mindOwnBusiness() {
        long mindingOwnBusinessTime = (long) (Math.random() * 5000);
        System.out.format("<%d%s> minding own business for %d s...%n", this.coupleID, this.personID, mindingOwnBusinessTime / 1000);
        try {
            Thread.sleep(mindingOwnBusinessTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void askForTable() {
        System.out.format("<%d%s> asking for a table%n", this.coupleID, this.personID);
        try {
            waiter.request(this.coupleID);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.format("<%d%s> got a table%n", this.coupleID, this.personID);
    }

    private void haveDinner() {
        long eatingTime = (long) (Math.random() * 5000);
        System.out.format("<%d%s> enjoying the food for %d seconds%n", this.coupleID, this.personID, eatingTime / 1000);
        try {
            Thread.sleep(eatingTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void goHome() {
        System.out.format("<%d%s> leaving%n", this.coupleID, this.personID);
        waiter.leave();
    }

    public void run() {
        while (true) {
            mindOwnBusiness();
            askForTable();
            haveDinner();
            goHome();
        }
    }
}
