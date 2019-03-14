package concurrency.time;

class TimedCountDown implements Timed {
  TimeManager clock;
  int i;

  TimedCountDown(int N, TimeManager clock) {
    i = N; this.clock = clock;
    clock.addTimed(this);
  }

  public void pretick() throws TimeStop {
    if(i==0) {
        //do beep action
        clock.removeTimed(this);
    }
  }

  public void tick(){
    --i;
  }
}

