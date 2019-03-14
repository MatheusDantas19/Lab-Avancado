/********************************************************/
//
// SimpleAllocator Class
//
package concurrency.golf;

public class SimpleAllocator implements Allocator{

    private  int available;

    public SimpleAllocator(int n) { available = n; }

    synchronized public void get(int n) throws InterruptedException {
        while (n>available) wait();
        available -= n;
      }

    synchronized public void put(int n) {
        available += n;
        notifyAll();
    }
 }
