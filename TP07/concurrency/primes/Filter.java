/*
@author  j.n.magee
*/
package concurrency.primes;

import concurrency.connector.*;

class Filter extends Thread {
  private PrimesCanvas display;
  private Pipe<Integer> in,out;
  private int index;

  Filter(Pipe<Integer> i, Pipe<Integer> o, int id, PrimesCanvas d)
    {in = i; out=o;display = d; index = id;}

  public void run() {
    int i,p;
    try {
      p = in.get();
      display.prime(index,p);
      if (p==Primes.EOS && out!=null) {
        out.put(p); return;
      }
      while(true) {
        i= in.get();
        display.print(index,i);
        sleep(1000);
        if (i==Primes.EOS) {
          if (out!=null) out.put(i); break;
        } else if (i%p!=0 && out!=null)
          out.put(i);
      }
    } catch (InterruptedException e){}
  }
}