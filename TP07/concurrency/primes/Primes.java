/*
@author  j.n.magee
*/
package concurrency.primes;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import concurrency.connector.*;

public class Primes extends Applet {
    PrimesCanvas display;
    Button goBuf,goNoBuf;
    Generator gen;
    Filter filter[] = new Filter[N];
    static int N = 7; // number of filters
    static int EOS = -1;

    public void init() {
        setLayout(new BorderLayout());
        display = new PrimesCanvas("Primes Sieve", Color.cyan, N+1);
        add("Center",display);
        Panel p0= new Panel();
        p0.add(goNoBuf = new Button(" Go - unbuffered "));
        goNoBuf.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
             if (ended()) go(false); 
           }
        });		
        p0.add(goBuf = new Button(" Go - buffered "));
        goBuf.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              if (ended()) go(true);
            }
         });
        goBuf.setFont(new Font("Helvetica",Font.BOLD,24));
        goNoBuf.setFont(new Font("Helvetica",Font.BOLD,24));
        add("South",p0);
		setBackground(Color.lightGray);
    }


  private void go(boolean buffered) {
    display.clear();
    // create channels
    ArrayList<Pipe<Integer>> pipes = new ArrayList<Pipe<Integer>>();
    for (int i=0; i<N; ++i)
      if (buffered)
        pipes.add(new PipeImplBuf<Integer>());
      else
        pipes.add(new PipeImplUnBuf<Integer>());
    //create threads
    gen = new Generator(pipes.get(0),display);
    for (int i=0; i<N; ++i)
      filter[i] =new Filter(pipes.get(i),i<N-1?pipes.get(i+1):null,i+1,display);
      gen.start();
      for (int i=0; i<N; ++i) filter[i].start();
  }

  private boolean ended() {
    for (int i=0; i<N; ++i)
        if (filter[i]!=null && filter[i].isAlive()) return false;
    if (gen!=null && gen.isAlive()) return false;
    return true;
  }

    public void stop() {
        for (int i=0; i<N; ++i)
          if (filter[i]!=null && filter[i].isAlive()) {
            filter[i].interrupt();
          }
        if (gen!=null && gen.isAlive())
          {gen.interrupt();
        }
        display.clear();
    }

 }

