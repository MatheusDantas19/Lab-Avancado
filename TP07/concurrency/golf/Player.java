package concurrency.golf;

class Player extends Thread {

    GolfClub gc;
    String name;
    int nballs;

    Player(GolfClub g, int n, String s) {
        gc = g;
        name = s;
        nballs =n;
    }

    public void run() {
      try {
        gc.getGolfBalls(nballs,name);
        Thread.sleep(gc.playTime);
        gc.relGolfBalls(nballs,name);
      } catch (InterruptedException e){}
    }

}