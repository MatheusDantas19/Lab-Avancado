//@author: j.n.magee
package concurrency.golf;

import java.awt.*;
import java.applet.*;
import concurrency.display.*;

/********************************************************/

class DisplayAllocator implements Allocator {

    StringCanvas display_;
    Allocator alloc_;
    private int hiredout=0;
    private int available;

    DisplayAllocator(int n,StringCanvas t, Allocator a) {
        alloc_ = a;
        available=n;
        display_=t;
        display_.setcolor(Color.cyan);
        display();
    }

    private void display() {
        display_.setString("available= " + String.valueOf(available)
                        + "  hired out= " + String.valueOf(hiredout));
    }

    public void get(int n) throws InterruptedException {
         alloc_.get(n);
         available-=n;
         hiredout+=n;
         display();
       }

    public void put(int n) {
         alloc_.put(n);
         hiredout-=n;
         available+=n;
         display();
       }

 }

