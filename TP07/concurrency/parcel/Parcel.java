package concurrency.parcel;

import java.awt.*;

class Parcel {
    final static int LEFT  = -1;
    final static int DOWN  = 0;
    final static int RIGHT = 1;
    final static int SIZE  = 20;
    final static int INC = 2;

    int x,y, destination;
    Color c;
    boolean displayIt = true;
    ParcelCanvas display;

    Parcel(int d, Color c, ParcelCanvas display) {
        this.c = c;
        this.destination= d;
        this.display = display;
        display.addParcel(this);
        Dimension dc = display.getSize();
        x = (dc.width-SIZE)/2;
        y = 0;
    }

    void move(int dir) {
        x +=dir*INC;
        y +=INC;
    }

    void remove() {
       display.removeParcel(this);
    }

    void hide(boolean b) {displayIt = b;}

}
