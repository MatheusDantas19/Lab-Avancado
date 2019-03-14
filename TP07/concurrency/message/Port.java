/*
@author  j.n.magee
*/
package concurrency.message;

import java.awt.*;
import java.util.*;
import java.applet.*;

/* ********************Port**************************** */
// The definition of channel assumes that there can be many sender
// but only one receiver

class Port<T> extends Selectable{

    Queue<T> queue = new LinkedList<T>();

    public synchronized void send(T v) {
        queue.add(v);
        signal();
    }

    public synchronized T receive() throws InterruptedException {
        block();
        clearReady();
        return queue.remove();
    }
}

