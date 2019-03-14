/********************************************************/
package concurrency.readwrite;

import concurrency.display.*;

class DisplayReadWrite implements ReadWrite {

    StringCanvas display_;
    ReadWrite lock_;
    private int readers = 0;
    private boolean writing = false;

    DisplayReadWrite(StringCanvas t, ReadWrite lock) {
        display_=t;
        lock_=lock;
        setdisplay();
    }

    private void setdisplay(){
	display_.setString("readers= " + String.valueOf(readers)
                        + "  writing= " + (new Boolean(writing)).toString());
    }

     public void acquireRead() throws InterruptedException{
         lock_.acquireRead();
         ++readers;
         setdisplay();
     }

     public void releaseRead() {
         lock_.releaseRead();
         --readers;
         setdisplay();
     }

     public void acquireWrite() throws InterruptedException {
         lock_.acquireWrite();
         writing = true;
         setdisplay();
       }

     public void releaseWrite() {
         lock_.releaseWrite();
         writing = false;
         setdisplay();
     }

 }

