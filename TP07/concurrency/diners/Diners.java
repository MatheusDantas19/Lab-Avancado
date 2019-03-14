//updated: daniel.sykes 2013

package concurrency.diners;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;

public class Diners extends Applet {

    PhilCanvas display;
    Thread[] phil= new Thread[PhilCanvas.NUMPHILS];
    Fork[] fork = new Fork[PhilCanvas.NUMPHILS];
    Scrollbar slider;
    Button restart;
    Button freeze;
    boolean fixed = false;

    public void init() {
        setLayout(new BorderLayout());
        String s = getParameter("Version");
        fixed = (s != null && s.equals("FIXED")); //changed 2013
        display = new PhilCanvas(this);
        display.setSize(300,320);
        add("Center",display);
        slider = new Scrollbar(Scrollbar.HORIZONTAL, 50, 5, 0, 100);
		
        restart = new Button("Restart");	
		restart.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            if (display.deadlocked()) {
                stop();
                slider.setValue(50);
                start();
            }
            display.thaw();
          }
        });

        freeze = new Button("Freeze");		
		freeze.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            display.freeze();
          }
        });

        Panel p1 = new Panel();
        p1.setLayout(new BorderLayout());
        p1.add("Center",slider);
        p1.add("East",restart);
        p1.add("West",freeze);
        add("South",p1);
    }

    Thread makePhilosopher(Diners d, int id, Fork left, Fork right) {
        if (fixed)
            return new FixedPhilosopher(d,id,left,right);
         else
            return new Philosopher(d,id,left,right);
        }

    public int sleepTime() {
        return (slider.getValue()*(int)(100*Math.random()));
    }

    public int eatTime() {
        return (slider.getValue()*(int)(50*Math.random()));
    }

    public void start() {
       for (int i =0; i<display.NUMPHILS; ++i)
            fork[i] = new Fork(display,i);
       for (int i =0; i<display.NUMPHILS; ++i){
            phil[i] = makePhilosopher(this,i,
                        fork[(i-1+display.NUMPHILS)% display.NUMPHILS],
                        fork[i]);
            phil[i].start();
       }
    }

    public void stop() {
        for (int i =0; i<display.NUMPHILS; ++i) {
            phil[i].interrupt();
        }
    }

}
