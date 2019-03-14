//********************************************************
//
// FairAllocator Class
//
package concurrency.golf;

public class FairAllocator implements Allocator{

  private  int available;
  private  long turn = 0; //next ticket to be served
  private  long next = 0; //next ticket to be dispensed

  public FairAllocator(int n) { available = n; }

  synchronized public void get(int n)
          throws InterruptedException {
    long myturn = turn;
    ++turn;
    while (n>available || myturn != next)
       wait();
    ++next;
    available -= n;
    notifyAll();
  }

  synchronized public void put(int n) {
    available += n;
    notifyAll();
  }
}
