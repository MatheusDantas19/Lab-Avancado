/*
@author  j.n.magee
//updated: daniel.sykes 2013
*/

package concurrency.parcel;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.util.*;
import concurrency.time.*;

public class ParcelRouter extends Applet implements ActionListener {
    ParcelCanvas display;
    Panel red; //changed 2013
    Panel blue, green, magenta; //changed 2013
    Font ff = new Font("Serif",Font.BOLD,14);
    TimeManager ticker;
    Vector clocked;
    Scrollbar bar;


    /*private Button makeButton(Color c) {
        Button b = new Button("        ");
        b.setBackground(c);
        b.setFont(ff);
        b.addActionListener(this);
        return b;
    }*/
    
    private Panel makeButton(Color c) { //replaced 2013
      final Panel b = new Panel();
      b.setBackground(c);
      Dimension size = new Dimension(45, 25);
      b.setMinimumSize(size);
      b.setMaximumSize(size);
      b.setPreferredSize(size);
      b.addMouseListener(new MouseAdapter()
        {
          public void mouseClicked(MouseEvent e)
          {
            ParcelRouter.this.actionPerformed(new ActionEvent(b, 0, "click"));
          }
        });
      return b;
  }

    public void init() {
      setSize(400, 348); //added 2013
        setLayout(new BorderLayout());
        add("Center",display=new ParcelCanvas(this));
        Panel p = new Panel(new FlowLayout(FlowLayout.CENTER,40,5));
        p.add(red= makeButton(Color.red));
        p.add(blue= makeButton(Color.blue));
        p.add(green= makeButton(Color.green));
        p.add(magenta= makeButton(Color.magenta));
        setBackground(Color.lightGray);
        add("South",p);
        bar = new Scrollbar(Scrollbar.VERTICAL,50,5,5,90);
        add("East",bar);

    }

    ParcelMover first;

    ParcelMover makeStage(ParcelMover left, ParcelMover right,
                           int fallDir, int level, int gate) {
        Chute a = new Chute(16,fallDir);
        ticker.addTimed(a);
        SensorController s = new SensorController(level);
        Chute b = new Chute(15,fallDir);
        ticker.addTimed(b);
        Switch g = new Switch(12,fallDir,gate,display);
        ticker.addTimed(g);
        a.next = s;
        s.next = b;
        s.controlled = g;
        b.next = g;
        g.left = left;
        g.right = right;
        return a;
    }

    ParcelMover makeDest(int fallDir, int destination) {
        Chute leg1 = new Chute(12,fallDir);
        ticker.addTimed(leg1);
        Chute leg2 = new Chute(12,fallDir);
        ticker.addTimed(leg2);
        Chute leg3 = new Chute(16,Parcel.DOWN);
        ticker.addTimed(leg3);
        Chute leg4 = new Chute(16,Parcel.DOWN);
        ticker.addTimed(leg4);
        DestinationBin bin = new DestinationBin(destination);
        ticker.addTimed(bin);
        leg1.next = leg2;
        leg2.next = leg3;
        leg3.next = leg4;
        leg4.next = bin;
        return leg1;
    }

    public void start() {
        ticker = new TimeManager(bar.getValue());
        bar.addAdjustmentListener(ticker);
        ParcelMover dest[] = new ParcelMover[4];
        dest[0] = makeDest(Parcel.LEFT,0);
        dest[1] = makeDest(Parcel.RIGHT,1);
        dest[2] = makeDest(Parcel.LEFT,2);
        dest[3] = makeDest(Parcel.RIGHT,3);
        ParcelMover left = makeStage(dest[0],dest[1],Parcel.LEFT,0,2);
        ParcelMover right= makeStage(dest[2],dest[3],Parcel.RIGHT,0,1);
        first = makeStage(left,right,Parcel.DOWN,1,0);
        ticker.addTimed(display);
        ticker.start();
    }

    public void stop() {
        if (ticker!=null) {
            ticker.interrupt();
        }
    }

    public void actionPerformed(ActionEvent e) {
        Parcel p=null;
        try{
            if (e.getSource()==red)
                   first.enter(p = new Parcel(0,Color.red,display));
            else if (e.getSource()==blue)
                   first.enter(p = new Parcel(1,Color.blue,display));
            else if (e.getSource()==green)
                   first.enter(p = new Parcel(2,Color.green,display));
            else
               first.enter(p = new Parcel(3,Color.magenta,display));
        } catch(TimeStop s) {p.remove();}
     }

}

/**
*------------ParcelMover------------
*/

interface ParcelMover {
    void enter(Parcel p) throws TimeStop;
}

/**
*------------SwitchControl------------
*/

interface SwitchControl {
    void setSwitch(int direction);
}

/**
*------------CHUTE-----------------
*/

class Chute implements ParcelMover,Timed {
    protected int i,T,direction;
    protected Parcel current = null;
    ParcelMover next = null;

    Chute(int len, int dir) {
        T = len;
        direction = dir;
    }

    // package enters chute
    public void enter(Parcel p) throws TimeStop {
        if (current!=null) throw new TimeStop();
        current = p; i = 0;
    }

    public void pretick() throws TimeStop {
        if (current==null) return;
        if (i == T) {
           next.enter(current); // package leaves chute
           current = null;
        }
    }

    public void tick(){
        if (current==null) return;
        ++i;
        current.move(direction);
    }

}

/**
*----------------SensorController-------------------
*/

class SensorController implements ParcelMover {
  ParcelMover  next;
  SwitchControl controlled;
  protected int level;

  SensorController(int level){this.level=level;}

  // package enters and leaves within one clock cycle
  public void enter(Parcel p) throws TimeStop {
    route(p.destination);
    next.enter(p);
  }

  protected void route(int destination) {
    int dir = (destination>>level) & 1;
    controlled.setSwitch(dir);
  }
}

/**
*----------------Switch -------------------
*/

class Switch extends Chute implements SwitchControl {
    ParcelMover left  = null;
    ParcelMover right = null;
    private ParcelCanvas display;
    private int gate;

    Switch(int len, int dir, int g, ParcelCanvas d )
         { super(len,dir); display = d; gate = g;}

    public void setSwitch(int direction) {
        if (current==null) {  // nothing passing through switch
            display.setGate(gate,direction);
            if (direction==0)
               next = left;
            else
               next = right;
        }
    }

}

/**
*----------------DestinationBin -------------------
*/

class DestinationBin implements ParcelMover, Timed {

    private Parcel current;
    private boolean flash = false;
    private boolean off = false;
    private int tickCount = 0;
    private int destination;
    static int FLASHPERIOD = 10;

    DestinationBin(int destination)
      {this.destination = destination;}

    public void enter(Parcel p) throws TimeStop {
      if (current!=null) current.remove();
      current = p;
      flash = !(p.destination == destination);
    }

    public void pretick(){};

    public void tick() {
       tickCount = (tickCount+1)%FLASHPERIOD;
       if (tickCount==0 && flash) {
          off = !off;
          current.hide(off);
       }
    }
}










