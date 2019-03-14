/*
@author  j.n.magee
//updated: daniel.sykes 2013
*/

package concurrency.golf;

import java.awt.*;
import java.applet.*;
import concurrency.display.*;

public class GolfClub extends Applet {
    String allocatorClass;
    SlotCanvas waiting;
    SlotCanvas playing;
    SlotCanvas starting;
    SlotCanvas ending;
    StringCanvas allocDisplay;
    Allocator alloc;
    final static int playTime = 10000;
    final static int Nballs = 5;

    public void init() {
        allocatorClass = getParameter("allocatorClass");
        setLayout(new BorderLayout());
        setSize(700, 270); //added 2013
        allocDisplay = new StringCanvas(allocatorClass == null ? "SimpleAllocator" : allocatorClass); //==null added 2013
        allocDisplay.setSize(430,80);
        starting = new SlotCanvas("new",Color.green,1);
        ending   = new SlotCanvas("end",Color.red,1);
        Panel top = new Panel();
        top.setLayout(new BorderLayout());
        top.add("Center",allocDisplay);
        top.add("West",starting);
        top.add("East",ending);
        add("North",top);
        waiting = new SlotCanvas("wait golf balls",Color.yellow,5);
        playing = new SlotCanvas("playing",Color.green,5);
        Panel p1 = new Panel();
        p1.add(waiting);
        p1.add(playing);
        add("Center",p1);
        add("South",new PlayerArrival(this,Nballs));
		setBackground(Color.lightGray);
    }

    void getGolfBalls(int n, String name)
              throws InterruptedException {
        String s = name+n;
        starting.enter(s);
        Thread.sleep(500);
        starting.leave(s);
        waiting.enter(s);
        alloc.get(n);
        waiting.leave(s);
        playing.enter(s);
    }

    void relGolfBalls(int n, String name)
              throws InterruptedException {
        String s = name+n;
        alloc.put(n);
        playing.leave(s);
        ending.enter(s);
        Thread.sleep(500);
        ending.leave(s);
    }

    public void start(){
       if (allocatorClass == null || allocatorClass.equals("SimpleAllocator")) //==null added 2013
           alloc = new DisplayAllocator(5,allocDisplay, new SimpleAllocator(5));
        else if (allocatorClass.equals("FairAllocator"))
           alloc = new DisplayAllocator(5,allocDisplay, new FairAllocator(5));
        else if (allocatorClass.equals("BoundedOvertakingAllocator"))
           alloc = new DisplayAllocator(5,allocDisplay, new BoundedOvertakingAllocator(5,3));
        else
           alloc = new DisplayAllocator(5,allocDisplay, new SimpleAllocator(5));
    }


}

