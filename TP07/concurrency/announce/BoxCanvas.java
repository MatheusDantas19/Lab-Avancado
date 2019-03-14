/**
@author  j.n.magee
*/
package concurrency.announce;

import java.awt.*;
import java.applet.*;


/**************************************************************/

class BoxCanvas extends Canvas {

    final static int MAX = 16;
    static final int MAXCOLOR = 4;
    static final int BOXSIZE = 15;

    private static final int IDLE = 0;
    private static final int ACTIVE = 1;
    private static final int BLACK = 2;

    int boxX[] = new int[MAX];
    int boxY[] = new int[MAX];

    int boxState[]= new int[MAX];

    final Color boxColor[] = {Color.red, Color.blue, Color.green, Color.orange};

    private Dimension dc;

    BoxCanvas() {
        dc = getSize();
        setBackground(Color.white);
        for (int i= 0; i<MAX ; ++i) boxState[i]=IDLE;
  	}

    // clear all boxes
    synchronized void reset() {
        for (int i= 0; i<MAX ; ++i) boxState[i]=IDLE;
        repaint();
    }

    //draw colored box id at position x,y
    synchronized void moveBox(int id, int x, int y) {
        if (id<0 || id>=MAX) return;
        boxState[id] = ACTIVE;
        if (x>=0 && x+BOXSIZE<dc.width) boxX[id]=x;
        if (y>=0 && y+BOXSIZE<dc.height) boxY[id]=y;
        repaint();
    }

    //draw black box id at position x,y
    synchronized void blackBox(int id, int x, int y) {
        if (id<0 || id>=MAX) return;
        boxState[id] = BLACK;
        if (x>=0 && x+BOXSIZE<dc.width) boxX[id]=x;
        if (y>=0 && y+BOXSIZE<dc.height) boxY[id]=y;
        repaint();
    }


    public void paint(Graphics g) {
        update(g);
    }

    Image offscreen;
    Dimension offscreensize;
    Graphics offgraphics;

    public synchronized void update(Graphics g){
        dc = getSize();
	    if ((offscreen == null) || (dc.width != offscreensize.width)
	                            || (dc.height != offscreensize.height)) {
	        offscreen = createImage(dc.width, dc.height);
	        offscreensize = dc;
	        offgraphics = offscreen.getGraphics();
	        offgraphics.setFont(getFont());
	    }

	    offgraphics.setColor(getBackground());
	    offgraphics.fillRect(0, 0, dc.width, dc.height);

         // Display boxes
        for (int i=0; i<MAX; ++i){
            if (boxState[i]>IDLE) {
                if (boxState[i]==BLACK)
                  offgraphics.setColor(Color.black);
                else
                  offgraphics.setColor(boxColor[i%MAXCOLOR]);
                offgraphics.fillRect(boxX[i],boxY[i],BOXSIZE,BOXSIZE);
            }
        }
        g.drawImage(offscreen, 0, 0, null);
     }
}


/**************************************************************/
