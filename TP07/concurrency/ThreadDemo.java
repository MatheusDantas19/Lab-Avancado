/* j.n.magee
//updated: daniel.sykes 2013
 */

package concurrency;

import java.awt.*;
import java.applet.*;
import concurrency.display.*;


class Rotator implements Runnable {

  public void run() {
    try {
      while(true) ThreadPanel.rotate();
    } catch(InterruptedException e) {}
  }
}


public class ThreadDemo extends Applet {
  ThreadPanel A;
  ThreadPanel B;
  ThreadPanel C;
  ThreadPanel D;

  public void init() {
    A = new ThreadPanel("Thread A",Color.blue);
    B = new ThreadPanel("Thread B",Color.blue);
    C = new ThreadPanel("Thread C",Color.blue);
    D = new ThreadPanel("Thread D",Color.blue);
    add(A);
    add(B);
    add(C);
    add(D);
	setBackground(Color.lightGray);
  }

  public void start() {
    A.start(new Rotator());
    B.start(new Rotator());
    C.start(new Rotator());
    D.start(new Rotator());
  }

  public void stop() {
    A.stop();
    B.stop();
    C.stop();
    D.stop();
  }
}

