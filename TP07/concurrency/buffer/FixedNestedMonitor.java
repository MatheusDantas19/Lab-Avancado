/*
@author  j.n.magee
//updated: daniel.sykes 2013
*/
package concurrency.buffer;

import java.awt.*;
import java.applet.*;
import concurrency.semaphore.*;

/*********************SEMABUFFER*****************************/

class FixedSemaBuffer<E> implements Buffer<E> {
  protected E[] buf;
  protected int in = 0;
  protected int out= 0;
  protected int count= 0; //only used for display purposes
  protected int size;

  Semaphore full;  //counts number of items
  Semaphore empty; //counts number of spaces

  FixedSemaBuffer(int size) {
    this.size = size; buf = (E[])new Object[size];
    full = new Semaphore(0);
    empty= new Semaphore(size);
  }

  public void put(E o) throws InterruptedException {
    empty.down();
    synchronized(this){
      buf[in] = o; ++count; in=(in+1)%size;
    }
    full.up();
  }

  public E get() throws InterruptedException{
    full.down(); E o;
    synchronized(this){
      o =buf[out]; buf[out]=null; --count; out=(out+1)%size;
    }
    empty.up();
    return (o);
  }
}



public class FixedNestedMonitor extends BoundedBuffer {


    public void start() {
        Buffer<Character> b = new DisplayFixedSemaBuffer(buffDisplay,5);
        // Create Thread
        prod.start(new Producer(b));
        cons.start(new Consumer(b));
    }


}

/**************************************************************/

class DisplayFixedSemaBuffer extends FixedSemaBuffer<Character> {
   BufferCanvas disp_;
	char[] tmp;

    DisplayFixedSemaBuffer(BufferCanvas disp,int size) {
        super(size);
        disp_ = disp;
		tmp = new char[size];
    }

    public void put(Character c) throws InterruptedException {
        int oldin = in;
        super.put(c);
        synchronized(this) {
          tmp[oldin]= c;
          disp_.setvalue(tmp,in,out);
          Thread.sleep(400);
		}
    }

    public Character get() throws InterruptedException {
        int oldout = out;
        Character c = super.get();
        synchronized(this) {
		  tmp[oldout]=' ';
          disp_.setvalue(tmp,in,out);
        }
        return (c);
    }
}

