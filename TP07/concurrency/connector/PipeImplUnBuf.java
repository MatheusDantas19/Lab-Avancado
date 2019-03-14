package concurrency.connector;

import concurrency.message.*;

public class PipeImplUnBuf<T> implements Pipe<T> {
  Channel<T> chan = new Channel<T>();

  public void put(T o)
    throws InterruptedException {
    chan.send(o);
  }

  public T get()
    throws InterruptedException {
    return chan.receive();
  }
}