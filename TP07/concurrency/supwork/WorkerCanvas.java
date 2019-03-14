/********************************************************/
// j.n.magee
package concurrency.supwork;

import java.awt.*;
import java.applet.*;

class WorkerCanvas extends Panel {

    Label title = new Label(" Task:");
    Label value = new Label("       ");
    Font f1 = new Font("Helvetica",Font.BOLD,18);
    Color worker;

    WorkerCanvas(Color c) {
        worker = c;
        setBackground(c);
        setLayout(new GridLayout(1,2));
        add(title); title.setFont(f1);
        add(value); value.setFont(f1); value.setBackground(Color.lightGray);
    }

    // display current task number val
    synchronized void setTask(int val) {
        value.setText("   "+val+"   ");
    }



}
