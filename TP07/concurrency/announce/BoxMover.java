/**
@author  j.n.magee
*/
package concurrency.announce;

import java.awt.event.*;

class BoxMover extends Thread {
  private BoxCanvas display;
  private int id, delay, MaxX, MaxY, x, y;
  private boolean hit=false;
  private MyListener listener = new MyListener();

  BoxMover(BoxCanvas d, int id, int delay) {
    display = d;
    display.addMouseListener(listener); // register action
    this.id = id;
    this.delay=delay;
    MaxX = d.getSize().width;
    MaxY = d.getSize().height;
  }

  synchronized void isHit(int mx, int my) {    //should be private but netscape 4.06
    hit = (mx>x && mx<x+display.BOXSIZE        //gives a security violation if it is.
        && my>y && my<y+display.BOXSIZE);
    if (hit) notify();
  }

  private synchronized boolean waitHit()
    throws InterruptedException {
    wait(delay);
    return hit;
  }

  public void run() {
    try {
      while (true) {
          if (waitHit()) break;
          x = (int)(MaxX*Math.random());
          y = (int)(MaxY*Math.random());
          display.moveBox(id,x,y);
      }
    } catch (InterruptedException e){}
    display.blackBox(id,x,y);
    display.removeMouseListener(listener); // deregister
  }


  class MyListener extends MouseAdapter {
    public void mousePressed(MouseEvent e) {
      isHit(e.getX(),e.getY());
    }
  }

}


