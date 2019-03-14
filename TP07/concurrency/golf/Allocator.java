
//@author: j.n.magee

package concurrency.golf;

public interface Allocator {

    public void get(int n) throws InterruptedException;
    public void put(int n);
}


