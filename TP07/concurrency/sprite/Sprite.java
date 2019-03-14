package concurrency.sprite;

import java.awt.*;
import java.applet.*;
import java.util.*;
import concurrency.time.*;

public abstract class Sprite implements Timed {
    protected int x;
    protected int y;
    protected boolean show = true;
    protected SpriteCanvas display;
    protected TimeManager ticker;
    protected  Dimension AREA;
    protected int height;
    protected int width;
    protected Image icon;
    protected int incVal = 2;
    public final static int NORTH = 0;
    public final static int NORTHEAST = 1;
    public final static int EAST = 2;
    public final static int SOUTHEAST = 3;
    public final static int SOUTH = 4;
    public final static int SOUTHWEST = 5;
    public final static int WEST = 6;
    public final static int NORTHWEST = 7;

    public Sprite(Image icon, Sprite parent) {
        this(icon, parent.centerX()-icon.getWidth(null)/2,
                   parent.centerY()-icon.getHeight(null)/2,parent);
    }

    public Sprite(Image icon, int x, int y, Sprite parent) {
        this(icon,x,y,parent.display,parent.ticker);
    }

    public Sprite(Image icon, int x, int y, SpriteCanvas disp, TimeManager tick) {
        this.x = x;
        this.y = y;
        this.icon = icon;
        this.display = disp;
        this.ticker = tick;
        ticker.addTimed(this);
        display.addSprite(this);
        AREA = display.getSize();
        height = icon.getHeight(null);
        width = icon.getWidth(null);
    }


    public void tick() {} // override this
    public void pretick() throws TimeStop{}

    public void setIncrement(int val){ incVal = val;}

    public void move(int dir) {
        switch(dir) {
        case NORTH:      y-=incVal; break;
        case NORTHEAST:  y-=incVal; x+=incVal; break;
        case EAST:       x+=incVal; break;
        case SOUTHEAST:  y+=incVal; x+=incVal; break;
        case SOUTH:      y+=incVal; break;
        case SOUTHWEST:  y+=incVal; x-=incVal; break;
        case WEST:       x-=incVal; break;
        case NORTHWEST:  y-=incVal; x-=incVal; break;
        default: ;
        }
        if (x<0) x=0;
        if (x>=(AREA.width-width)) x = AREA.width-width - 1;
        if (y<0) y=0;
        if (y>=(AREA.height-height)) y = AREA.height-height - 1;
    }

    public void paint(Graphics g) {
        if ( show )
           g.drawImage(icon,x,y,display);
    }

    public void flash() {show = !show;}

    // return (x,y) for centre of sprite
    public int centerX() { return x + width/2; }
    public int centerY() { return y + height/2; }


    public int posX() { return x ; }
    public int posY() { return y ; }

    public void remove() {
        ticker.removeTimed(this);
        display.removeSprite(this);
    }

    public void setImage(Image i) { icon = i; }

    public boolean collide (Sprite target) {
        return ( (centerX()>target.x && centerX()<target.x+target.width)
               && (centerY()>target.y && centerY()<target.y+target.height));
    }

    public void hit() {} //override this
}






