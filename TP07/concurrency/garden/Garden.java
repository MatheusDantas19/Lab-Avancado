// j.n.magee
//updated: daniel.sykes 2013

package concurrency.garden;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import concurrency.display.*;

public class Garden extends Applet {

    Button goButton;
    Turnstile west;
    Turnstile east;
    Counter counter;
    NumberCanvas counterD;
    NumberCanvas westD;
    NumberCanvas eastD;
    Checkbox fixit;
    public final static int MAX = 20;


    public void init() {
        super.init();
		setBackground(Color.lightGray);
        // Set up Button and Checkbox
        Panel p0= new Panel();
        p0.add(goButton = new Button(" Go "));
        goButton.setFont(new Font("Helvetica",Font.BOLD,24));
		
        goButton.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
           if (west==null && east==null)
              go();
          else if (!west.isAlive() && !east.isAlive())
              go();         
          }
        });
		
        Panel p1= new Panel();
        p1.setLayout(new BorderLayout());
        p1.add("Center",p0);
        p1.add("East",fixit = new Checkbox("Fix It"));
        // Set up Display
        Panel p2 = new Panel();
        counterD = new NumberCanvas("Counter");
        westD   = new NumberCanvas("West",Color.green);
        eastD   = new NumberCanvas("East",Color.green);
        counterD.setSize(150,100);
        westD.setSize(100,100);
        eastD.setSize(100,100);
        p2.add(westD);
        p2.add(counterD);
        p2.add(eastD);
        // Arrange Applet display
        setLayout(new BorderLayout());
        add("Center",p2);
        add("South",p1);
    }

    private void go() {
        if (!fixit.getState())
            counter = new Counter(counterD);
        else
            counter = new SynchronizedCounter(counterD);
        west= new Turnstile(westD,counter);
        east= new Turnstile(eastD,counter);
        west.start();
        east.start();
    }

}

class Counter {

    int value=0;
    NumberCanvas display;

    Counter(NumberCanvas n) {
        display=n;
        display.setvalue(value);
    }

    void increment() {
        int temp = value;   //read[v]
        Simulate.HWinterrupt();
        value=temp+1;       //write[v+1]
        display.setvalue(value);
    }
}

class SynchronizedCounter extends Counter {

  SynchronizedCounter(NumberCanvas n)
     {super(n);}

   synchronized void increment() {
        super.increment();
   }
}


class Turnstile extends Thread {
  NumberCanvas display;
  Counter people;

  Turnstile(NumberCanvas n,Counter c)
    { display = n; people = c; }

  public void run() {
    try{
      display.setvalue(0);
      for (int i=1;i<=Garden.MAX;i++){
        Thread.sleep(500); //0.5 second
        display.setvalue(i);
        people.increment();
      }
    } catch (InterruptedException e) {}
  }
}

class Simulate {
    public static void HWinterrupt() {
        if (Math.random()<0.5)
           try{Thread.sleep(200);} catch(InterruptedException e){};
            //used instead of Thread.yield() to ensure portability
    }
}
