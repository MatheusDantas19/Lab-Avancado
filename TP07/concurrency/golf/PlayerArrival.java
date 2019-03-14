package concurrency.golf;

import java.awt.event.*; 

import java.awt.*;

class PlayerArrival extends Panel {

    GolfClub gc;
    private String names = "abcdefghijklnopqrstuvxyz";
    private int nextname = 0;

    Button p[];

    PlayerArrival(GolfClub g, int n) {
        super();
        gc=g;
        p = new Button[n];
        for(int i=0; i<p.length; i++) {
            p[i] = new Button("new Player("+(i+1)+")");
            p[i].addActionListener(new NewPlayer(i+1)); 
            add(p[i]);
        }
    }

	class NewPlayer implements ActionListener  {	
	   int nballs;
	
	   NewPlayer(int nb)  { nballs = nb; }
	   
	   public void actionPerformed(ActionEvent e) {
         Thread t = new Player(gc,nballs,names.substring(nextname,nextname+1));
         t.start();
         nextname = (nextname+1)%names.length();
       }
     }

}
