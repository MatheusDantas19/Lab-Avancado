//@author: j.n.magee
//updated: daniel.sykes 2013

package concurrency.readwrite;

import java.awt.*;
import java.applet.*;
import concurrency.display.*;

/********************************************************/

class Reader implements Runnable {

    ReadWrite lock_;

    Reader(ReadWrite lock) {
        lock_ = lock;
    }

    public void run() {
      try {
        while(true)  {
            while(!ThreadPanel.rotate());
            // begin critical section
            lock_.acquireRead();
            while(ThreadPanel.rotate());
            lock_.releaseRead();
        }
      } catch (InterruptedException e){}
    }
}

/********************************************************/

class Writer implements Runnable {

    ReadWrite lock_;

    Writer(ReadWrite lock) {
        lock_ = lock;
    }

    public void run() {
      try {
        while(true)  {
            while(!ThreadPanel.rotate());
            // begin critical section
            lock_.acquireWrite();
            while(ThreadPanel.rotate());
            lock_.releaseWrite();
        }
      } catch (InterruptedException e){}
    }
}

/********************************************************/

public class ReadersWriters extends Applet {

    ThreadPanel read1;
    ThreadPanel read2;
    ThreadPanel write1;
    ThreadPanel write2;
    StringCanvas display;
    String rwClass;

    public void init() {
        rwClass = getParameter("rwClass");
        setLayout(new BorderLayout());
        add("Center", display=new StringCanvas(rwClass == null  ? "ReadWriteSafe" : rwClass)); //== null added 2013
        display.setSize(630,100);
        Panel p = new Panel();
        p.add(read1 =new ThreadPanel("Reader 1",Color.blue,true));
        p.add(read2 =new ThreadPanel("Reader 2",Color.blue,true));
        p.add(write1=new ThreadPanel("Writer 1",Color.yellow,true));
        p.add(write2=new ThreadPanel("Writer 2",Color.yellow,true));
        add("South",p);
		setBackground(Color.lightGray);
    }

    public void start() {
        DisplayReadWrite lock;
        if (rwClass == null || rwClass.equals("ReadWriteSafe")) //== null added 2013
           lock = new DisplayReadWrite(display,new ReadWriteSafe());
        else if (rwClass.equals("ReadWritePriority"))
           lock = new DisplayReadWrite(display,new ReadWritePriority());
        else if (rwClass.equals("ReadWriteFair"))
           lock = new DisplayReadWrite(display,new ReadWriteFair());
        else
            lock = new DisplayReadWrite(display,new ReadWriteSafe());
        read1.start(new Reader(lock));
        read2.start(new Reader(lock));
        write1.start(new Writer(lock));
        write2.start(new Writer(lock));
    }

    public void stop() {
        read1.stop();
        read2.stop();
        write1.stop();
        write2.stop();
     }

}

