//@author: j.n.magee
//updated: daniel.sykes 2013

package concurrency.semaphore;

import java.awt.*;
import java.applet.*;
import concurrency.display.*;

/********************************************************/

class MutexLoop implements Runnable {

    Semaphore mutex;

    MutexLoop (Semaphore sema) {mutex=sema;}

    public void run() {
      try {
        while(true)  {
           while(!ThreadPanel.rotate());
            // get mutual exclusion
            mutex.down();
            while(ThreadPanel.rotate()); //critical section
            //release mutual exclusion
            mutex.up();
        }
      } catch(InterruptedException e){}
    }
}

/********************************************************/

public class SemaDemo extends Applet {

    ThreadPanel thread1;
    ThreadPanel thread2;
    ThreadPanel thread3;
    NumberCanvas semaDisplay;

    public void init() {
        setLayout(new BorderLayout());
        semaDisplay = new NumberCanvas("Mutex");
        add("Center",semaDisplay);
        Panel p = new Panel();
        p.add(thread1=new ThreadPanel("Thread 1",Color.blue,true));
        p.add(thread2=new ThreadPanel("Thread 2",Color.blue,true));
        p.add(thread3=new ThreadPanel("Thread 3",Color.blue,true));
        add("South",p);
        setBackground(Color.lightGray);
    }

    public void start() {
        Semaphore mutex = new DisplaySemaphore(semaDisplay,1);
        thread1.start(new MutexLoop(mutex));
        thread2.start(new MutexLoop(mutex));
        thread3.start(new MutexLoop(mutex));

    }

    public void stop() {
        thread1.stop();
        thread2.stop();
        thread3.stop();
    }
}

