package concurrency.supwork;

import java.awt.*;
import concurrency.connector.*;

class Worker extends Thread {
  WorkerCanvas display;
  Function func;
  TupleSpace bag;
  int processingTime = (int)(6000*Math.random());

  Worker(WorkerCanvas d, TupleSpace b, Function f)
    { display = d; bag = b; func = f; }

  public void run () {
    double deltaX = 1.0/SupervisorCanvas.Nslice;
    try {
      while(true){
        // get new task from tuple space
        Integer task = (Integer)bag.in("task");
        int slice = task.intValue();
        if (slice <0) {  // stop if negative
            bag.out("task",task);
            break;
        }
        display.setTask(slice);
        sleep(processingTime);
        double area
          = deltaX*func.fn(deltaX*slice+deltaX/2);
        // output result to tuple space
        bag.out( "result",
           new Result(slice,area,display.worker));
      }
    } catch (InterruptedException e){}
  }
}
