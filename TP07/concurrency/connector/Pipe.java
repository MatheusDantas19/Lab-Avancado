package concurrency.connector;

public interface Pipe<T> {

  public void put(T o)
    throws InterruptedException; //put object into buffer

  public T get()
     throws InterruptedException;//get an object from buffer
}