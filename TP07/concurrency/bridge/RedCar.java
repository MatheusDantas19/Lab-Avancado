package concurrency.bridge;

import java.awt.*;

class RedCar implements Runnable {

    BridgeCanvas display;
    Bridge control;
    int id;

    RedCar(Bridge b, BridgeCanvas d, int id) {
        display = d;
        this.id = id;
        control = b;
    }

    public void run() {
      try {
        while(true) {
            while (!display.moveRed(id));  // not on bridge
            control.redEnter();
            while (display.moveRed(id));   // move over bridge
            control.redExit();
        }
      } catch (InterruptedException e){}
    }

}

