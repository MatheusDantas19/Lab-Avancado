
/*
@author  j.n.magee
//updated: daniel.sykes 2013
*/
package concurrency.buffer;

import java.awt.*;
import java.applet.*;
import concurrency.display.*;
import concurrency.semaphore.*;

/*********************SEMABUFFER*****************************/

class SemaBuffer<E> implements Buffer<E> {
  protected E[] buf;
  protected int in = 0;
  protected int out= 0;
  protected int count= 0;
  protected int size;

  Semaphore full; //counts number of items
  Semaphore empty;//counts number of spaces

  SemaBuffer(int size) {
    this.size = size; buf = (E[])new Object[size];
    full = new Semaphore(0);
    empty= new Semaphore(size);
  }

  synchronized public void put(E o)
              throws InterruptedException {
    empty.down();
    buf[in] = o;
    ++count;
    in=(in+1) % size;
    full.up();
  }

  synchronized public E get()
               throws InterruptedException{
    full.down();
    E o =buf[out];
    buf[out]=null;
    --count;
    out=(out+1) % size;
    empty.up();
    return (o);
  }
}


public class NestedMonitor extends BoundedBuffer {


    public void start() {
        Buffer<Character> b = new DisplaySemaBuffer(buffDisplay,5);
        // Create Thread
        prod.start(new Producer(b));
        cons.start(new Consumer(b));
    }


}

/**************************************************************/

class DisplaySemaBuffer extends SemaBuffer<Character> {
    BufferCanvas disp_;
	char[] tmp;

    DisplaySemaBuffer(BufferCanvas disp,int size) {
        super(size);
        disp_ = disp;
		tmp = new char[size];
    }

    synchronized public void put(Character c) throws InterruptedException {
		int oldin = in;
        super.put(c);
		tmp[oldin]= c;
        disp_.setvalue(tmp,in,out);
        Thread.sleep(400);
    }

    synchronized public Character get() throws InterruptedException {
		int oldout = out;
        Character c = super.get();
		tmp[oldout]=' ';
        disp_.setvalue(tmp,in,out);
        return (c);
    }
}
