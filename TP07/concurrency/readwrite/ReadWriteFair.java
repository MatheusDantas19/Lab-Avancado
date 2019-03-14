package concurrency.readwrite;

//
// The Read Write Monitor Class - fair version
//
class ReadWriteFair implements ReadWrite {

    private int readers =0;
    private boolean writing = false;
    private int waitingW = 0; // no of waiting Writers.
    private boolean readersturn = false;

    synchronized public void acquireRead() throws InterruptedException {
        while (writing || (waitingW>0 && !readersturn)) wait();
        ++readers;
      }

    synchronized public void releaseRead() {
        --readers;
        readersturn=false;
        if(readers==0) notifyAll();
    }

    synchronized public void acquireWrite() throws InterruptedException {
        ++waitingW;
        while (readers>0 || writing) wait();
        --waitingW;
        writing = true;
      }

    synchronized public void releaseWrite() {
        writing = false;
        readersturn=true;
        notifyAll();
    }

 }
