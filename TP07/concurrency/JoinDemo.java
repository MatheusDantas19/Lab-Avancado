/*
@author: j.n.magee
updated: daniel.sykes 2013
*/

package concurrency;

import java.awt.*;
import java.applet.*;
import concurrency.display.*;

public class JoinDemo extends Applet {

    ThreadPanel ms;
    ThreadPanel sl;
    SlotCanvas resultdisp;

    public void init() {
        add(ms =new ThreadPanel("Master",Color.blue,true));
        add(resultdisp = new SlotCanvas("res",Color.cyan,1));
        add(sl =new ThreadPanel("Slave",Color.blue,true));
		setBackground(Color.lightGray);
     }

    public void start() {
        ms.start(new Master(sl,resultdisp));
    }

    public void stop() {
        ms.stop();
    }

}



/*******************MASTER************************/

class Master implements Runnable {

  ThreadPanel slaveDisplay;
  SlotCanvas  resultDisplay;

  Master(ThreadPanel tp, SlotCanvas sc)
    {slaveDisplay=tp; resultDisplay=sc;}

  public void run() {
    try {
      String res=null;
      while(true) {
        while (!ThreadPanel.rotate());
        if (res!=null) resultDisplay.leave(res);
        // create new slave thread
        Slave s = new Slave();
        Thread st = slaveDisplay.start(s,false);
        // continue execution
        while (ThreadPanel.rotate());
        // wait for slave termination
        st.join();
        // get and display result from slave
        res = String.valueOf(s.result());
        resultDisplay.enter(res);
      }
    } catch (InterruptedException e){}
  }
}


/*******************SLAVE************************/

class Slave implements Runnable {
  int rotations = 0;

  public void run() {
    try {
      while (!ThreadPanel.rotate()) ++rotations;
    } catch (InterruptedException e){}
  }

  int result(){
    return rotations;
  }
}
