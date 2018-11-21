package ex2;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Consumer implements Runnable {

    private Integer maxLen;
    private NaiveBuffer naiveBuffer;
    private FairBuffer fairBuffer;
    private int id;

    Consumer(NaiveBuffer naiveBuffer, int maxLen) {
        this.maxLen = maxLen;
        this.naiveBuffer = naiveBuffer;
        this.id = -1;
    }

    Consumer(FairBuffer fairBuffer, int maxLen, int id) {
        this.maxLen = maxLen;
        this.fairBuffer = fairBuffer;
        this.id = id;
    }

    @Override
    public void run() {
        while(true){
            int len = (int) (Math.random() * maxLen);
            long time;
            if (id == -1) time = naiveBuffer.take((int) (Math.random() * maxLen));
            else time = fairBuffer.take((int) (Math.random() * maxLen), this.id);
            //System.out.println("K," + len + "," + time);

        }
    }
}
