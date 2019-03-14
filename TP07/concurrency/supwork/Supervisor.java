package concurrency.supwork;

import concurrency.connector.*;

import java.awt.*;

class Result {
  int task;
  Color worker;
  double area;
  Result(int s, double a, Color c)
    {task =s; worker=c; area=a;}
}

class Supervisor extends Thread {
  SupervisorCanvas display;
  TupleSpace bag;
  Integer stop = new Integer(-1);

  Supervisor(SupervisorCanvas d, TupleSpace b)
    { display = d; bag = b; }

  public void run () {
    try {
      // output tasks to tuplespace
      for (int i=0; i<SupervisorCanvas.Nslice; ++i)
        bag.out("task",new Integer(i));
      // collect results
      for (int i=0; i<display.Nslice; ++i) {
        Result r = (Result)bag.in("result");
        display.setSlice(r.task,r.area,r.worker);
      }
      // output stop tuple
      bag.out("task",stop);
    } catch (InterruptedException e){}
  }
}





