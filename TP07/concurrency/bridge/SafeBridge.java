package concurrency.bridge;

import java.awt.*;

class SafeBridge extends Bridge {

    private int nred  = 0;
    private int nblue = 0;

    synchronized void redEnter() throws InterruptedException {
        while (nblue>0) wait();
        ++nred;
    }

    synchronized void redExit(){
        --nred;
        if (nred==0)
            notifyAll();
    }

    synchronized void blueEnter() throws InterruptedException {
        while (nred>0) wait();
        ++nblue;
    }

    synchronized void blueExit(){
        --nblue;
        if (nblue==0)
            notifyAll();
    }
}
