package concurrency.bridge;

class BlueCar implements Runnable {

    BridgeCanvas display;
    Bridge control;
    int id;

    BlueCar(Bridge b, BridgeCanvas d, int id) {
        display = d;
        this.id = id;
        control = b;
    }

    public void run() {
      try {
        while (true) {
            while (!display.moveBlue(id));  // not on bridge
            control.blueEnter();
            while (display.moveBlue(id));   // move over bridge
            control.blueExit();
         }
      } catch (InterruptedException e){}
    }

}
