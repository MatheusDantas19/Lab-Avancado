package concurrency.time;

import java.awt.*;
import java.applet.*;

public class TimedProducerConsumer extends Applet {
    public void init() {new ProducerConsumer();}
}

class ProducerConsumer {
  TimeManager clock = new TimeManager(1000);
  Producer producer = new Producer(2);
  Consumer consumer = new Consumer(3);
  Ticker ticker = new Ticker();

  ProducerConsumer() {clock.start();}

  class Consumer implements Timed {
    int Tc,t; Object consuming = null;

    Consumer(int Tc) {
      this.Tc = Tc; t=1;
      clock.addTimed(this);
    }

    void item(Object x) throws TimeStop {
      System.out.println("Transfer");
      if (consuming!=null) throw new TimeStop();
      consuming = x;
    }

    public void pretick() {}

    public void tick() {
      System.out.println("Tick consumer "+(consuming!=null));
      if (consuming==null) return;
      if (t<Tc) { ++t; return;}
      if (t==Tc) {consuming=null; t=1;}
    }
  }

  class Producer implements Timed {
    int Tp,t;

    Producer(int Tp) {
      this.Tp = Tp;  t=1;
      clock.addTimed(this);
    }


    public void pretick() throws TimeStop {
      if (t==1) consumer.item(new Object());
    }

    public void tick() {
      System.out.println("Tick producer");
      if (t<Tp) { ++t;return;}
      if (t==Tp) {t=1;}
    }
  }

  class Ticker implements Timed {
    Ticker() {clock.addTimed(this);}
    public void pretick() throws TimeStop {}
    public void tick() {
      System.out.println("Tick start");
    }
  }

}

