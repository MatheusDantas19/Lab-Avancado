/*
@author  j.n.magee
//updated: daniel.sykes 2013
*/

package concurrency.invaders;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.util.*;
import concurrency.sprite.*;
import concurrency.time.*;

public class SpaceInvaders extends Applet implements ActionListener {

    Font ff = new Font("Serif",Font.BOLD,14);
    Scrollbar bar;
    Button go;
    Image rocket,rocket_hit, backdrop;
    Image missile, alien;
    Image explosion[] = new Image[16];

    static SpriteCanvas theDisplay;
    static TimeManager  theTicker;
    static MissileLauncher launcher;
    static AlienGenerator aliens;
    static CollisionDetector detector;
    static SpaceShip  ship;
    static ScorePanel score;

    public void init() {
        setSize(480, 348); //added 2013
        setLayout(new BorderLayout());
        MediaTracker mt;
        mt = new MediaTracker(this);
        backdrop = getImage(getDocumentBase(),"image/space.gif");
        rocket = getImage(getDocumentBase(),"image/rocket.gif");
        rocket_hit = getImage(getDocumentBase(),"image/rocket_hit.gif");
        missile = getImage(getDocumentBase(),"image/missile.gif");
        alien = getImage(getDocumentBase(),"image/alien.gif");
        for (int i = 1; i<=explosion.length; ++i)
              explosion[i-1] = getImage(getDocumentBase(),"image/explosion"+i+".gif");
        mt.addImage(backdrop, 0);
        mt.addImage(rocket, 1);
        mt.addImage(rocket_hit, 2);
        mt.addImage(missile, 3);
        mt.addImage(alien,4);
        for (int i = 1; i<=explosion.length; ++i) mt.addImage(explosion[i-1], 4+i);
        try {
            for(int i=0; i<11;++i) mt.waitForID(i);
         } catch (java.lang.InterruptedException e) {
            System.out.println("Couldn't load one of the images");
         }
        SpaceShip.rocket = rocket;
        SpaceShip.rocket_hit = rocket_hit;
        MissileLauncher.missile = missile;
        Explosion.explosion = explosion;
        AlienGenerator.alien = alien;
        Explosion.rumble = getAudioClip(getDocumentBase(),"sound/explosion.au");
        MissileLauncher.shoot = getAudioClip(getDocumentBase(),"sound/shoot.au");
        ScorePanel.win = getAudioClip(getDocumentBase(),"sound/win.au");
        ScorePanel.lose = getAudioClip(getDocumentBase(),"sound/lose.au");
        theDisplay = new SpriteCanvas(backdrop);
        theDisplay.setSize(backdrop.getWidth(null),backdrop.getHeight(null));
        add("Center",theDisplay);
        bar = new Scrollbar(Scrollbar.VERTICAL,40,1,0,200);
        add("East",bar);
        Panel p = new Panel();
        go = new Button("  New Game  ");
        go.addActionListener(this);
        go.setFont(ff);
        p.add(go);
        score = new ScorePanel(ff,5,this);
        p.add(score);
        add("South",p);
    }

    public void start() {
       score.clear();
       theDisplay.reset();
       theDisplay.requestFocus();
       theTicker = new TimeManager(40);
       bar.addAdjustmentListener(theTicker);
       theTicker.addTimed(theDisplay);
       theTicker.start();
       ship = new SpaceShip (
                theDisplay.getSize().width/2,
                theDisplay.getSize().height-rocket.getHeight(null)-1,
                theDisplay,theTicker);
       aliens = new AlienGenerator(ship);
       launcher = new MissileLauncher(ship);
       detector = new CollisionDetector();
     }


     public void stop() {
	 	if (theTicker!=null) {                
		   theDisplay.endGame();
           theTicker.removeTimed(theDisplay);
           theTicker.removeTimed(aliens);
           theTicker.removeTimed(launcher);
           theTicker.removeTimed(detector);
           ship.remove();
           theTicker.interrupt();
		   theTicker = null;
	 	}
     }

     public void actionPerformed(ActionEvent e) {
        stop();
        start();
     }


}


class CollisionDetector implements Timed {
    ImmutableList<Sprite> aliens = null;
    ImmutableList<Sprite> missiles = null;

    CollisionDetector(){
        SpaceInvaders.theTicker.addTimed(this);
    }

    public void addAlien(Sprite a) {
        aliens = ImmutableList.add(aliens,a);
    }

    public void removeAlien(Sprite a) {
        aliens = ImmutableList.remove(aliens,a);
    }

    public void addMissile(Sprite m) {
        missiles = ImmutableList.add(missiles,m);
    }

    public void removeMissile(Sprite m) {
        missiles = ImmutableList.remove(missiles,m);
    }

    public void pretick(){
	  if (aliens != null)
	  for (Sprite alien : aliens )  {
          if (alien.collide(SpaceInvaders.ship)) {
            SpaceInvaders.ship.hit();
            alien.hit();
           }
		   if (missiles != null)
		   for (Sprite missile : missiles) {
            if (alien.collide(missile)) {
                alien.hit();
                missile.hit();
            }
           }
      }
    }

    public void tick() {}
}

/**
*  -- Creates missiles
*/


class MissileLauncher implements Timed {
    boolean launchMissile = false;
    Sprite owner;
    static AudioClip shoot;
    static Image missile;

    MissileLauncher(Sprite owner) {
      SpaceInvaders.theDisplay.addKeyListener(new KeyPress());
      SpaceInvaders.theTicker.addTimed(this);
      this.owner = owner;
    }

    public void pretick() {}

    public void tick() {
        if (launchMissile) {
            launchMissile = false;
            SpaceInvaders.detector.addMissile(new Missile(owner));
            //shoot.play(); //removed 2013 - deadlock on linux?
        }
    }


    class KeyPress extends KeyAdapter {
      public void keyPressed(KeyEvent k) {
        System.out.println("Key "+k.getKeyCode()+" pressed"); //2013
        if (k.getKeyCode()==KeyEvent.VK_SPACE)
           launchMissile = true;
      }
    }

   class Missile extends Sprite {
     int speed = 10;

     Missile(Sprite owner) {
        super(missile,owner.centerX()-missile.getWidth(null)/2,owner.posY(),owner);
        setIncrement(speed);
    }

    public void hit() {
        SpaceInvaders.detector.removeMissile(this);
        remove();
    }

    public void tick() {
      move(NORTH);
      if (posY()<=0) {
        SpaceInvaders.detector.removeMissile(this);
        remove();
      }
    }
  }
}


class SpaceShip extends Sprite  {
    static Image rocket;
    static Image rocket_hit;
    int speed = 6;
    int hitpause = 0;
    boolean left = false;
    boolean right = false;
    boolean up = false;
    boolean down = false;

    SpaceShip(int x, int y, SpriteCanvas disp, TimeManager tick) {
        super(rocket,x,y,disp,tick);
        setIncrement(speed);
        display.addKeyListener(new KeyPress());
    }

    public void hit(){
        hitpause = 10;
        setImage(rocket_hit);
        SpaceInvaders.score.shieldHit();
    }

    public void tick() {
        if (hitpause>0) {
            --hitpause;
            if (hitpause==0) setImage(rocket);
            return;
        }
        if (left) move(WEST);
        if (right) move(EAST);
        if (up) move(NORTH);
        if (down) move(SOUTH);
    }

    class KeyPress extends KeyAdapter {
      public void keyPressed(KeyEvent k) {
        int code = k.getKeyCode();
        System.out.println("Key "+code+" pressed"); //2013
        
        if (code != KeyEvent.VK_SPACE)
        {
          left = false; //added 2013
          right = false; //
          up = false; //
          down = false; //
        }
        
        if (code ==KeyEvent.VK_LEFT)
           left = true;
        else if (code == KeyEvent.VK_RIGHT)
           right = true;
        else if (code ==KeyEvent.VK_UP)
           up = true;
        else if (code == KeyEvent.VK_DOWN)
           down = true;
      }
      public void keyReleased(KeyEvent k) {
        int code = k.getKeyCode();
        System.out.println("Key "+code+" released"); //2013
        if (code ==KeyEvent.VK_LEFT)
           left = false;
        else if (code == KeyEvent.VK_RIGHT)
           right = false;
        else if (code ==KeyEvent.VK_UP)
           up = false;
        else if (code == KeyEvent.VK_DOWN)
           down = false;
      }
    }
}

class Explosion extends Sprite {
    static Image explosion[];
    static AudioClip rumble;
    int currentFrame=0;

    Explosion(Sprite parent) {
        super(explosion[0],parent);
    }

    public void tick() {
		if (currentFrame==0) rumble.play();
        ++currentFrame;
        if (currentFrame<explosion.length)
              setImage(explosion[currentFrame]);
        else
           remove();
    }
}


class AlienGenerator implements Timed {
    int maxX =  SpaceInvaders.theDisplay.getSize().width-1;
    int delay = 50;
    Sprite target;
    static Image alien;

    AlienGenerator(Sprite target) {
        SpaceInvaders.theTicker.addTimed(this);
        this.target = target;
    }

    public void pretick() {}

    public void tick() {
        --delay;
        if (delay<=0) {
            Alien a = new Alien(20+(int)((maxX-20)*Math.random()));
            SpaceInvaders.detector.addAlien(a);
            delay = (int)(50*Math.random());
        }
    }

    class Alien extends Sprite {
        int dir;

        Alien(int x) {
            super(alien,x,0,SpaceInvaders.theDisplay,SpaceInvaders.theTicker);
            setIncrement(1+(int)(Math.random()*10.0));
            int choice  = (int) (Math.random()*10.0);
            if (choice<3) dir = SOUTHWEST;
            else if (choice<7) dir = SOUTH;
            else dir = SOUTHEAST;
        }

        public void hit() {
            new Explosion(this);
            SpaceInvaders.score.alienHit();
            SpaceInvaders.detector.removeAlien(this);
            remove();
        }

        public void tick() {
            move(dir);
            if ((posY()>=AREA.height-height-1)||(posX()>=AREA.width-width-1)){
               SpaceInvaders.detector.removeAlien(this);
               remove();
            }
        }
    }
}

class ScorePanel extends Panel {

    Label t1     = new Label(" Shield:");
    Label shield =  new Label("       ");;
    Label t2     = new Label(" Aliens:");
    Label alien  = new Label("       ");
    int shields;
    int aliens;
    int lives;
    static AudioClip win,lose;
    Applet game;

    ScorePanel(Font ff, int initShield, Applet game) {
        this.game = game;
         lives=shields= initShield;
         aliens = 0;
         add(t1); t1.setFont(ff);
         add(shield); shield.setFont(ff); shield.setBackground(Color.lightGray);
         add(t2); t2.setFont(ff);
         add(alien); alien.setFont(ff); alien.setBackground(Color.lightGray);
         clear();
    }

    void shieldHit() {
        --shields;
        shield.setText("   "+shields+"   ");
        if (shields<=0) {
            if (aliens>20)
                win.play();
            else
                lose.play();
            game.stop();
        }
    }

    void alienHit() {
        ++aliens;
        alien.setText("   "+aliens+"   ");
    }

    void clear() {
        shields = lives; aliens = 0;
        shield.setText("   "+shields+"   ");
        alien.setText("   "+aliens+"   ");
    }
}



