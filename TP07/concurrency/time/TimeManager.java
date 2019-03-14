package concurrency.time;

import java.util.*;
import java.awt.event.*;


public class TimeManager extends Thread
  implements AdjustmentListener{
  volatile int delay;
  volatile ImmutableList<Timed> clocked = null;

  public TimeManager(int d) {delay = d;}

  public void addTimed(Timed el) {
    clocked = ImmutableList.add(clocked,el);
  }

  public void removeTimed(Timed el) {
    clocked = ImmutableList.remove(clocked,el);
  }

  public void adjustmentValueChanged(AdjustmentEvent e) {
    delay = e.getValue();
  }

  public void run () {
    try {
      while(true) {
        try {
          for (Timed e: clocked) e.pretick(); //pretick broadcast
          for (Timed e :clocked) e.tick();    //tick broadcast
        } catch (TimeStop s) {
            System.out.println("*** TimeStop");
            return;
        }
        Thread.sleep(delay);
      }
    } catch (InterruptedException e){}
  }
}

