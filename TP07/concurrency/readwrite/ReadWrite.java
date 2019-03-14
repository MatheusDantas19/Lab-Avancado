//@author: j.n.magee
package concurrency.readwrite;

//************************************************************
// the interface for ReadWrite monitor implementations

interface ReadWrite {

   public void acquireRead() throws InterruptedException;

   public void releaseRead();

   public void acquireWrite() throws InterruptedException;

   public void releaseWrite();
 }


