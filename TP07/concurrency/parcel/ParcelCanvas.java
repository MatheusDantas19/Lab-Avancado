/**
@author  j.n.magee
*/
package concurrency.parcel;

import java.awt.*;
import java.applet.*;
import java.util.*;
import concurrency.time.*;

/**************************************************************/

class ParcelCanvas extends Canvas implements Timed  {

    final static int MAX = 16;

    Applet controller;
    Image router;

    ArrayList<Parcel> boxes;
    Polygon  gate[][]    = new Polygon[2][3];
    int gatedir[]        = new int[3];

    private Dimension dc;

    ParcelCanvas(Applet c) {
        boxes = new ArrayList<Parcel>();
        // get background image
        this.controller = c;
        MediaTracker mt;
        mt = new MediaTracker(this);
        router = controller.getImage(controller.getDocumentBase(), "image/router.gif");
        mt.addImage(router, 0);

        try {
            mt.waitForID(0);
        } catch (java.lang.InterruptedException e) {
            System.out.println("Couldn't load one of the images");
        }
        setSize(router.getWidth(null),router.getHeight(null));
        for (int i = 0; i<gatedir.length; ++i) gatedir[i]=0;
        gate[0][0] = genGate(193,127,1);
        gate[1][0] = genGate(193,127,-1);
        gate[0][1] = genGate(280,214,1);
        gate[1][1] = genGate(280,214,-1);
        gate[0][2] = genGate(107,212,1);
        gate[1][2] = genGate(107,212,-1);


    }

    // clear all boxes
    void reset() {
        boxes = new ArrayList<Parcel>();
    }

    void setGate(int id, int dir) {
        gatedir[id]=dir;
    }

    void addParcel(Parcel p) {
        boxes.add(p);
    }

    void removeParcel(Parcel p ){
        boxes.remove(p);
    }

    public void pretick() {
      repaint();
    }

    public void tick(){}

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
	    offgraphics.drawImage(router,0,0,this);

	    // Display gates
	    offgraphics.setColor(Color.orange);
	    for (int i = 0; i<gatedir.length; ++i)
	       offgraphics.fillPolygon(gate[gatedir[i]][i]);

         // Display boxes
		for (Parcel b: boxes) {
             if (b.displayIt) {
              offgraphics.setColor(b.c);
              offgraphics.fillRect(b.x,b.y,Parcel.SIZE,Parcel.SIZE);
            }
        }
        g.drawImage(offscreen, 0, 0, null);
     }

     Polygon genGate(int x, int y, int sign) {
         int XP[] = {x,x+40*sign,x+40*sign,x};
         int YP[] = {y,y-40,y-35,y+5};
         return new Polygon(XP,YP,4);
     }

}


/**************************************************************/
