package concurrency.connector;

import concurrency.buffer.*;

public class PipeImplBuf<T> implements Pipe<T> {
  Buffer<T> buf = new BufferImpl<T>(10);

  public void put(T o)
    throws InterruptedException {
    buf.put(o);
  }

  public T get()
    throws InterruptedException {
    return buf.get();
  }
}