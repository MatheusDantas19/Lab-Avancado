/**
@author  j.n.magee
*/
package concurrency.sprite;

import java.awt.*;
import java.applet.*;
import java.util.*;
import concurrency.time.*;

/**************************************************************/

public class SpriteCanvas extends Canvas implements Timed  {

    Image backdrop;
    ImmutableList<Sprite> sprites = null;
    boolean ended= false;
    Font ff = new Font("Serif",Font.BOLD,36);

    private Dimension dc;

    public SpriteCanvas(Image backdrop) {
        this.backdrop = backdrop;
        requestFocusInWindow(); //added 2013
    }

    // clear all shapes
    public void reset() {
        ended = false;
        sprites = null;
    }


    void addSprite(Sprite p) {
        sprites = ImmutableList.add(sprites,p);
    }

    void removeSprite(Sprite p ){
        sprites = ImmutableList.remove(sprites,p);
    }

    public void pretick() throws TimeStop{
      repaint();
    }

    public void tick() {}

    public void paint(Graphics g) {
        update(g);
    }

    public void endGame(){
        ended=true;
        repaint();
    }

    Image offscreen;
    Dimension offscreensize;
    Graphics offgraphics;

    public  void update(Graphics g){
        dc = getSize();
	    if ((offscreen == null) || (dc.width != offscreensize.width)
	                            || (dc.height != offscreensize.height)) {
	        offscreen = createImage(dc.width, dc.height);
	        offscreensize = dc;
	        offgraphics = offscreen.getGraphics();
	        offgraphics.setFont(ff);
	    }
	    offgraphics.drawImage(backdrop,0,0,this);
        if (ended) {
            offgraphics.setColor(Color.red);
            offgraphics.drawString("The End",200,150);
        } else {
             // Display sprites
          if (sprites != null)
            for (Sprite s : sprites)
              s.paint(offgraphics);
        }
        g.drawImage(offscreen, 0, 0, null);
     }
}


/**************************************************************/
