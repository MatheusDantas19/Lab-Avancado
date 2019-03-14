/*
@author  j.n.magee
*/
package concurrency.announce;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;

public class EventDemo extends Applet {
    final static int MAX = 8;
    final static int FAST = 600;
    final static int SLOW = 1200;
    BoxCanvas display;
    Thread movers[] = new Thread[MAX];
    Button goFast,goSlow,end;
    Label gameClicks;
    int clicks = 0;

    Font ff = new Font("Serif",Font.BOLD,14);

    public void init() {
        setLayout(new BorderLayout());
        add("Center",display=new BoxCanvas());
        display.setSize(300,240);
        display.addMouseListener(new MyListener());
        Panel p = new Panel();
        p.add(goFast= new Button("Go Fast"));
        p.add(goSlow= new Button("Go Slow"));
        p.add(gameClicks = new Label("  0  "));
        p.add(end = new Button("End"));
		
        goFast.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              stop();
			  go(FAST);
            }
         });
		
		goSlow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              stop();
			  go(SLOW);
            }
         });

		end.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              stop();
            }
         });

        goFast.setFont(ff);
        goSlow.setFont(ff);
        end.setFont(ff);
        gameClicks.setFont(ff);
        gameClicks.setBackground(Color.lightGray);
        setBackground(Color.magenta);
        add("South",p);
    }

    public void go(int speed) {
      display.reset();
      clicks=0;
      gameClicks.setText("  "+clicks+"  ");
      for (int i=0; i<MAX; ++i) {
            movers[i] = new BoxMover(display,i,speed);
            movers[i].start();
        }
    }

    public void stop() {
     for (int i=0; i<MAX; ++i) {
        if (movers[i]!=null && movers[i].isAlive()) {
            movers[i].interrupt();
        }
     }
    }

    class MyListener extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            ++clicks;
            gameClicks.setText("  "+clicks+"  ");
        }
    }
}
