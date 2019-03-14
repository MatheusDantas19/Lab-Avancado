/*
@author  j.n.magee
*/
package concurrency.primes;

import concurrency.connector.*;

class Generator extends Thread {
  private PrimesCanvas display;
  private Pipe<Integer> out;
  static int MAX = 50;

  Generator(Pipe<Integer> c, PrimesCanvas d)
    {out=c; display = d;}

  public void run() {
    try {
      for (int i=2;i<=MAX;++i) {
        display.print(0,i);
        out.put(i);
        sleep(500);
      }
      display.print(0,Primes.EOS);
      out.put(Primes.EOS);
    } catch (InterruptedException e){}
  }
}