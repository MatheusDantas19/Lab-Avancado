package concurrency.bridge;

import java.awt.*;

class Bridge {

    synchronized void redEnter() throws InterruptedException {}

    synchronized void redExit(){}

    synchronized void blueEnter() throws InterruptedException {}

    synchronized void blueExit(){}
}
