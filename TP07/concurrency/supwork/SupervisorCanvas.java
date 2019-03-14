/*
@author  j.n.magee
*/
package concurrency.supwork;

import java.awt.*;
import java.applet.*;


/**************************************************************/

 class SupervisorCanvas extends Canvas {
    String title_;
    Font f1 = new Font("Helvetica",Font.ITALIC+Font.BOLD,24);
    Font f2 = new Font("Helvetica",Font.BOLD,18);
    final static int Nslice = 32; // number of curve slices
    boolean drawSlice[] = new boolean[Nslice];
    Color colorSlice [] = new Color[Nslice];
    double area = 0.0;
    Function func = null;
    private final int Xmax = 256;
    private final int Ymax = 200;
    private final int delta = Xmax/Nslice;


    public SupervisorCanvas(String title, Color c) {
        super();
        setSize(Xmax+50,Ymax+100);
        title_=title;
        setBackground(c);
        for (int i=0; i<Nslice; i++) drawSlice[i]=false;
  	}

    //display rectangle i with color c, add a to area field
    synchronized void setSlice(int i,double a, Color c) {
        drawSlice[i]=true;
        colorSlice[i]=c;
        area+=a;
        repaint();
    }

    //rest display to clear rectangles and draw curve for f
    synchronized void reset(Function f) {
        func = f;
        for (int i=0; i<Nslice; i++) drawSlice[i]=false;
        area=0.0;
        repaint();
    }

    public void paint(Graphics g) {
        update(g);
    }

    Image offscreen;
    Dimension offscreensize;
    Graphics offgraphics;

    public synchronized void update(Graphics g){
        Dimension d = getSize();
	    if ((offscreen == null) || (d.width != offscreensize.width)
	                            || (d.height != offscreensize.height)) {
	        offscreen = createImage(d.width, d.height);
	        offscreensize = d;
	        offgraphics = offscreen.getGraphics();
	        offgraphics.setFont(getFont());
	    }

	    offgraphics.setColor(getBackground());
	    offgraphics.fillRect(0, 0, d.width, d.height);

         // Display the title
        offgraphics.setColor(Color.black);
        offgraphics.setFont(f1);
        FontMetrics fm = offgraphics.getFontMetrics();
        int w = fm.stringWidth(title_);
        int h = fm.getHeight();
        int x = (getSize().width - w)/2;
        int y = h;
        offgraphics.drawString(title_, x, y);
        x = (getSize().width - Xmax)/2;
        drawFunction(offgraphics,x,y+20);
        offgraphics.setColor(Color.black);
        offgraphics.setFont(f2);
        fm = offgraphics.getFontMetrics();
        String as = "Area: "+area;
        if (as.length()>12)
            as = as.substring(0,12);
        w = fm.stringWidth(as);
        h = fm.getHeight();
        int x1 = (getSize().width - w)/2;
        int y1 = y+Ymax+h;
        offgraphics.drawString(as, x1, y1+25);
        offgraphics.drawString("0.0",x-fm.stringWidth("0.0")/2,y1+20);
        offgraphics.drawString("1.0",x+Xmax-fm.stringWidth("1.0")/2,y1+20);
        g.drawImage(offscreen, 0, 0, null);
     }

     private void drawFunction(Graphics g, int X, int Y) {
        g.setColor(Color.white);
        g.fillRect(X,Y,Xmax,Ymax);

        //draw ruler
        g.setColor(Color.black);
        g.drawLine(X-1,Y,X-1,Y+Ymax);
        g.drawLine(X-2,Y,X-2,Y+Ymax);
        g.drawLine(X,Y+Ymax,X+Xmax,Y+Ymax);
        g.drawLine(X,Y+Ymax+1,X+Xmax,Y+Ymax+1);
        for (int i=X;i<=Xmax+X;i+=32)
            g.drawLine(i,Y+Ymax,i,Y+Ymax+5);

        if (func==null) return;
        double scaleY = Ymax/Math.max(func.fn(1.0),func.fn(0.0));
        double deltaX = 1.0/Xmax;

        //draw computed areas
        for (int i = 0; i<Nslice ; ++i) {
          if (drawSlice[i]) {
              g.setColor(colorSlice[i]);
              int x = i*delta;
              int Ylen = (int)(func.fn(deltaX*(x+delta/2))*scaleY);
              g.fillRect(X+x,Y+Ymax-Ylen,delta,Ylen);
          }
        }

        //draw curve
        g.setColor(Color.black);
        for (int x = 0; x < Xmax ; x++) {
            double dx = deltaX*x;
	    g.drawLine(X+x, Y+Ymax-(int)(func.fn(dx)*scaleY), X+x + 1, Y+Ymax-(int)(func.fn(dx+deltaX)*scaleY));
        }


     }

}


/**************************************************************/
