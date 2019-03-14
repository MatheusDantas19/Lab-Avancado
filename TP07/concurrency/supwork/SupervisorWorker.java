/*
@author  j.n.magee
//updated: daniel.sykes 2013
*/

package concurrency.supwork;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import concurrency.connector.*;

public class SupervisorWorker extends Applet {
    SupervisorCanvas display;
    WorkerCanvas red,green,yellow,blue;
    Button fn1,fn2,fn3;
    Font buttonFont = new Font("TimesRoman",Font.ITALIC,18);
    Font titleFont  = new Font("SanSerif",Font.ITALIC+Font.BOLD,24);
    Thread supervisor, redWork,greenWork, yellowWork,blueWork;


    public void init() {
        setSize(600, 330); //added 2013
        setLayout(new BorderLayout());
        display = new SupervisorCanvas("Supervisor", Color.cyan);
        add("Center",display);
        Panel p0= new Panel();
		
        p0.add(fn1 = new Button("f(x) = 1 - x*x"));
        fn1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              if (ended()) go(new OneMinusXsquared());
            }
         });
		
        p0.add(fn2 = new Button("f(x) = 1-x*x*x"));
        fn2.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                  if (ended()) go(new OneMinusXcubed());
                }
             });
		
        p0.add(fn3 = new Button("f(x) = x*x+0.1"));
        fn3.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                  if (ended()) go(new XsquaredPlusPoint1());
                }
             });
		
        fn1.setFont(buttonFont);
        fn2.setFont(buttonFont);
        fn3.setFont(buttonFont);
        add("South",p0);
        Panel p1 = new Panel();
        p1.setBackground(Color.cyan);
        p1.setLayout(new GridLayout(6,1));
        Label w = new Label("Workers");
        w.setFont(titleFont);
        p1. add(w);
        p1. add(red = new WorkerCanvas(Color.red));
        p1. add(green = new WorkerCanvas(Color.green));
        p1. add(yellow = new WorkerCanvas(Color.yellow));
        p1. add(blue = new WorkerCanvas(Color.blue));
        add("East",p1);
    }

    int slice=0;

    private void go(Function fn) {
        display.reset(fn);
        TupleSpace bag = new TupleSpaceImpl();
        redWork =  new Worker(red,bag,fn);
        greenWork = new Worker(green,bag,fn);
        yellowWork = new Worker(yellow,bag,fn);
        blueWork = new Worker(blue,bag,fn);
        supervisor = new Supervisor(display,bag);
        redWork.start();
        greenWork.start();
        yellowWork.start();
        blueWork.start();
        supervisor.start();
     }

    private boolean ended() {
      if (redWork!=null && redWork.isAlive()) return false;
      if (greenWork!=null && greenWork.isAlive()) return false;
      if (yellowWork!=null && yellowWork.isAlive()) return false;
      if (blueWork!=null && blueWork.isAlive()) return false;
      if (supervisor!=null && supervisor.isAlive()) return false;
      return true;
    }

   public void stop() {
      if (redWork!=null && redWork.isAlive())
        {redWork.interrupt();}
      if (greenWork!=null && greenWork.isAlive())
        {greenWork.interrupt();}
      if (yellowWork!=null && yellowWork.isAlive())
        {yellowWork.interrupt();}
      if (blueWork!=null && blueWork.isAlive())
        {blueWork.interrupt();}
      if (supervisor!=null && supervisor.isAlive())
        {supervisor.interrupt();}
    }

 }

