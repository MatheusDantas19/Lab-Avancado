/*
@author  j.n.magee
*/
package concurrency.primes;

import java.awt.*;
import java.applet.*;


/**************************************************************/

class PrimesCanvas extends Canvas {
    String title_;
    int slots_;
    String[] buf_;
    String[] primes_;
    private final static String empty = "  ";

    Font f1 = new Font("Helvetica",Font.ITALIC+Font.BOLD,24);
    Font f2 = new Font("TimesRoman",Font.BOLD,36);

    PrimesCanvas(String title, Color c, int slots) {
        super();
        title_=title;
        slots_=slots;
        buf_ = new String[slots_];
        primes_ = new String[slots_];
        for(int i=0; i<buf_.length;i++) buf_[i]=primes_[i]=empty;
        setSize(20+70*slots_,190);
        setBackground(c);
  	}

    // display val in an upper box numbered index
    // boxes are numbered from the left
  	synchronized void print(int index, int val){
  	    if (index>=0 && index<slots_) buf_[index] = String.valueOf(val);
  	    repaint();
  	}

    //display val in a lower box numbered inndex
    //the lower box indexed by 0 is not displayed
    synchronized void prime(int index, int val){
  	    if (index>0 && index<slots_) primes_[index] = String.valueOf(val);
  	    repaint();
  	}

    // clear all boxes
    synchronized void clear() {
        for (int i=0;i<slots_;++i) buf_[i]=primes_[i]= empty;
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
        offgraphics.drawLine(x,y+3,x+w,y+3);
        y = getSize().height/2 - 50;
        offgraphics.drawString("Gen", 10, y-h+25);
        // Buffer Boxes
        offgraphics.setFont(f2);
        for(int i=0; i<slots_; i++) {
            int bx = 10+70*i;
            offgraphics.setColor(Color.lightGray);
            offgraphics.fillRect(bx,y,50,50);
            offgraphics.setColor(Color.black);
            offgraphics.drawRect(bx,y,50,50);
            if (buf_[i]!=null)
                offgraphics.drawString(buf_[i],bx+10,y+35);
            if (i<slots_-1)
                offgraphics.drawLine(bx+50,y+25,bx+70,y+25);
            if (i>0) {
                offgraphics.setColor(Color.white);
                offgraphics.fillRect(bx,y+70,50,50);
                offgraphics.setColor(Color.black);
                offgraphics.drawRect(bx,y+70,50,50);
                offgraphics.drawLine(bx+25,y+50,bx+25,y+70);
                if (primes_[i]!=null)
                  offgraphics.drawString(primes_[i],bx+10,y+105);
            }

        }
        g.drawImage(offscreen, 0, 0, null);
     }
}


/**************************************************************/
