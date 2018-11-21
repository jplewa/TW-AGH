package ex2;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Producer implements Runnable {

    private Integer maxLen;
    private NaiveBuffer naiveBuffer;
    private FairBuffer fairBuffer;
    private Character value;
    private int id;

    Producer(NaiveBuffer naiveBuffer, Character value, int maxLen) {
        this.maxLen = maxLen;
        this.naiveBuffer = naiveBuffer;
        this.value = value;
        this.id = -1;
    }

    Producer(FairBuffer fairBuffer, Character value, int maxLen, int id) {
        this.maxLen = maxLen;
        this.fairBuffer = fairBuffer;
        this.value = value;
        this.id = id;
    }

    @Override
    public void run() {
        while(true){
            int len = (int) (Math.random() * maxLen);
            long time;
            if (id == -1) time = naiveBuffer.put(len, value);
            else time = fairBuffer.put(len, value, id);
            //System.out.println("P," + len + "," + time);
        }
    }
}
