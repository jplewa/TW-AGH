package ex2;

class PersonThatWantsToPrintStuff implements Runnable {
    private final PrintingMonitor printingMonitor;
    private final int personID;

    PersonThatWantsToPrintStuff(PrintingMonitor printingMonitor, int personID) {
        this.printingMonitor = printingMonitor;
        this.personID = personID;
    }

    private void newPrintJob() {
        System.out.format("<%d> creating a new print job%n", this.personID);
    }

    private int getPrinter() {
        int printerID = printingMonitor.use();
        System.out.format("<%d> booked printer #%d%n", this.personID, printerID);
        return printerID;
    }

    private void printStuff(int printerID) {
        long sleepTime = (long) (Math.random() * 5000);
        System.out.format("<%d> using printer #%d for %d s%n", personID, printerID, sleepTime / 1000);
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void freePrinter(int printerID) {
        printingMonitor.free(printerID);
        System.out.format("<%d> freed printer #%d%n", personID, printerID);
    }

    @Override
    public void run() {
        int printerID;
        while (true) {
            newPrintJob();
            printerID = getPrinter();
            printStuff(printerID);
            freePrinter(printerID);
        }
    }
}
