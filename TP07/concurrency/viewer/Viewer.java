package concurrency.viewer;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.net.URL;
import java.util.*;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.image.ColorModel;
import java.io.*;

class Viewer extends Frame {

    MenuBar mb;
    Menu example;
    Menu chapter[] = new Menu[12];
    MenuItem about, cruise,countdown,threaddemo,garden,carpark,semademo,
             buffer,nestedmonitor, fixednestedmonitor,
             diners,fixeddiners,bridge,readwrite,readwritepriority,readwritefair,
             golfclub,golfclubfifo,golfclubbounded,joindemo,
             syncmsg,selmsg,asyncmsg,rendezvous,
             fixedcruise,primes,supwork,announce,parcel,invaders,quit;
    Applet applet = null;
    MyAppletStub stub;
    MyAppletContext context;


    public Viewer() {
        mb = new MenuBar();
        example = new Menu("Example");
        mb.add(example);
        for (int i=0; i<12 ; ++i) {
            chapter[i] = new Menu("Chapter "+(i+1));
            example.add(chapter[i]);
        }
        about = newMenuItem("About Example Viewer");
        example.add(about);
        /* Chapter 1 */
        cruise = newMenuItem("Cruise Control");
        chapter[0].add(cruise);
        /* Chapter 2 */
        countdown = newMenuItem("CountDown");
        chapter[1].add(countdown);
        /* Chapter 3 */
        threaddemo = newMenuItem("Thread Demo");
        chapter[2].add(threaddemo);
        /* Chapter 4 */
        garden = newMenuItem("Garden");
        chapter[3].add(garden);
        /* Chapter 5 */
        carpark = newMenuItem("Car Park");
        chapter[4].add(carpark);
        semademo = newMenuItem("Semaphore Demo");
        chapter[4].add(semademo);
        buffer = newMenuItem("Bounded Buffer");
        chapter[4].add(buffer);
        nestedmonitor = newMenuItem("Nested Monitor");
        chapter[4].add(nestedmonitor);
        fixednestedmonitor = newMenuItem("Fixed Nested Monitor");
        chapter[4].add(fixednestedmonitor);
        /* Chapter 6 */
        diners = newMenuItem("Dining Philosophers");
        chapter[5].add(diners);
        fixeddiners = newMenuItem("Fixed Dining Philosophers");
        chapter[5].add(fixeddiners);
        /* Chapter 7 */
        bridge = newMenuItem("Single Lane Bridge");
        chapter[6].add(bridge);
        readwrite = newMenuItem("Readers Writers");
        chapter[6].add(readwrite);
        readwritepriority = new MenuItem("Readers Writers-Priority");
        chapter[6].add(readwritepriority);
        readwritefair = newMenuItem("Readers Writers Fair");
        chapter[6].add(readwritefair);
        /* Chapter 8 */
        fixedcruise = newMenuItem("Fixed Cruise Control");
        chapter[7].add(fixedcruise);
        /* Chapter 9 */
        golfclub = newMenuItem("Golf Club");
        chapter[8].add(golfclub);
        golfclubfifo = newMenuItem("Golf Club FiFo");
        chapter[8].add(golfclubfifo);
        golfclubbounded = new MenuItem("Golf Club Bounded Overtaking");
        chapter[8].add(golfclubbounded);
        joindemo = newMenuItem("Master - Slave");
        chapter[8].add(joindemo);
        /* Chapter 10 */
        syncmsg = newMenuItem("Synchronous Message");
        chapter[9].add(syncmsg);
        selmsg = newMenuItem("Selective Receive");
        chapter[9].add(selmsg);
        asyncmsg = newMenuItem("Asynchronous Message");
        chapter[9].add(asyncmsg);
        rendezvous = newMenuItem("Rendezvous");
        chapter[9].add(rendezvous);
        /* Chapter 11 */
        primes = newMenuItem("Primes Sieve");
        chapter[10].add(primes);
        supwork = newMenuItem("Supervisor - Worker");
        chapter[10].add(supwork);
        announce = newMenuItem("Announcer - Listener");
        chapter[10].add(announce);
        /* Chapter 12 */
        parcel = newMenuItem("Parcel Router");
        chapter[11].add(parcel);
        invaders = newMenuItem("Space Invaders");
        chapter[11].add(invaders);
        quit = newMenuItem("Quit");
        example.add(quit);
        setMenuBar(mb);
        context = new MyAppletContext();
        stub = new MyAppletStub(context);
        create(about);
		addWindowListener(new WindowAdapter()  {
			public void windowClosing(WindowEvent e)  {
			  System.exit(0);
			}
		});
    }

    void create(MenuItem mi) {
        if (applet!=null) {
            applet.stop();
            remove(applet);
        }
        setSize(600,400);
        if (mi==about) {
            applet = new Initial();
        /* Chapter 1 */
        } else if (mi == cruise) {
            stub.setParameter("fixed","FALSE");
            applet = new concurrency.cruise.CruiseControl();
        /* Chapter 2 */
        } else if (mi == countdown) {
            applet = new concurrency.CountDown();
        /* Chapter 3 */
        } else if (mi == threaddemo) {
            applet = new concurrency.ThreadDemo();
        /* Chapter 4 */
        } else if (mi == garden) {
            applet = new concurrency.garden.Garden();
        /* Chapter 5 */
        } else if (mi == carpark) {
            applet = new concurrency.carpark.CarPark();
        } else if (mi == semademo) {
            applet = new concurrency.semaphore.SemaDemo();
        } else if (mi == buffer) {
            applet = new concurrency.buffer.BoundedBuffer();
        } else if (mi == nestedmonitor) {
            applet = new concurrency.buffer.NestedMonitor();
        } else if (mi == fixednestedmonitor) {
            applet = new concurrency.buffer.FixedNestedMonitor();
        /* Chapter 6 */
        } else if (mi == diners) {
            stub.setParameter("Version","First");
            applet = new concurrency.diners.Diners();
        } else if (mi == fixeddiners) {
            stub.setParameter("Version","FIXED");
            applet = new concurrency.diners.Diners();
        /* Chapter 7 */
        } else if (mi == bridge) {
            applet = new concurrency.bridge.SingleLaneBridge();
        } else if (mi == readwrite){
            stub.setParameter("rwClass","ReadWriteSafe");
            applet = new concurrency.readwrite.ReadersWriters();
        } else if (mi == readwritepriority){
            stub.setParameter("rwClass","ReadWritePriority");
            applet = new concurrency.readwrite.ReadersWriters();
        } else if (mi == readwritefair){
            stub.setParameter("rwClass","ReadWriteFair");
            applet = new concurrency.readwrite.ReadersWriters();
        /* Chapter 8 */
        } else if (mi == fixedcruise) {
            stub.setParameter("fixed","TRUE");
            applet = new concurrency.cruise.CruiseControl();
        /* Chapter 9 */
        } else if (mi == golfclub){
            stub.setParameter("allocatorClass","SimpleAllocator");
            applet = new concurrency.golf.GolfClub();
        } else if (mi == golfclubfifo){
            stub.setParameter("allocatorClass","FairAllocator");
            applet = new concurrency.golf.GolfClub();
        } else if (mi == golfclubbounded){
            stub.setParameter("allocatorClass","BoundedOvertakingAllocator");
            applet = new concurrency.golf.GolfClub();
        } else if (mi == joindemo) {
            applet = new concurrency.JoinDemo();
        /* Chapter 10 */
        } else if (mi == syncmsg) {
            applet = new concurrency.message.SynchMsgDemo();
        } else if (mi == selmsg) {
            applet = new concurrency.message.SelectMsgDemo();
        } else if (mi == asyncmsg) {
            applet = new concurrency.message.AsynchMsgDemo();
        } else if (mi == rendezvous) {
            applet = new concurrency.message.EntryDemo();
        /* Chapter 11 */
        } else if (mi == primes) {
            applet= new concurrency.primes.Primes();
        } else if (mi == supwork) {
            applet= new concurrency.supwork.SupervisorWorker();
        } else if (mi == announce) {
            applet= new concurrency.announce.EventDemo();
        /* Chapter 12 */
        } else if (mi == parcel) {
            applet = new concurrency.parcel.ParcelRouter();
        } else if (mi == invaders) {
            applet = new concurrency.invaders.SpaceInvaders();
        } else if (mi == quit) {
            System.exit(0);
        }
        setTitle(mi.getLabel());
        add("Center",applet);
        applet.setStub(stub);
        applet.init();
        pack();
        setVisible(true);
        applet.start();
    }

    
	MenuItem newMenuItem(String name)  {
		MenuItem m = new MenuItem(name);
		m.addActionListener(new MenuAction(m));
		return m;
	}
	
	
	class MenuAction implements ActionListener {
		MenuItem mine;
		MenuAction(MenuItem m)  { mine = m;
		}
		
		public void actionPerformed(ActionEvent e) {
 		   create(mine);
		}
	}


  public static void main (String args[]) {
    Frame frame = new Viewer();
  }

}



class MyAppletStub implements AppletStub {

    HashMap<String,String> parameters = new HashMap<String,String>();
    AppletContext ac;

    MyAppletStub(AppletContext a) {
        ac = a;
    }


     public boolean isActive(){
        return true;
     }

     public URL getDocumentBase(){
		URL url= this.getClass().getResource("../../concurrency.html");
//		System.out.println(url.toString());
		return url;
     }
	 /*
        try {
            URL url = new URL("file:"+System.getProperty("user.dir")+"/");
            System.out.println(url.toString());
            return url;
           } catch (java.net.MalformedURLException e) {
  			System.out.println("badly formed URL:" + System.getProperty("user.dir"));
           return null;
        }
     }
	 */

     public URL getCodeBase(){
        return getDocumentBase();
     }

     public String getParameter(String name) {
        return (String)parameters.get(name);
     }

     public void setParameter(String name, String value) {
        parameters.put(name,value);
     }

     public AppletContext getAppletContext(){
        return ac ;
     }

    public void appletResize(int width, int height){
    }

}

class MyAppletContext implements AppletContext {

    public AudioClip getAudioClip(URL url) {
        return new MyAudioClip(url);
    }

    public Image getImage(URL url) {
      try {
        java.awt.image.ImageProducer prod = (java.awt.image.ImageProducer)url.getContent();
	    return Toolkit.getDefaultToolkit().createImage(prod);
	  } catch (java.io.IOException e) {
	    System.out.println("Image not found at:"+url);
	  }
	  return null;
    }

    public Applet getApplet(String name) {return null;}
    public Enumeration<Applet> getApplets() {return null;}
    public void showDocument(URL url){ }
    public void showDocument(URL url, String target) {}

    public void showStatus(String status){}
	
	public void setStream(String key, InputStream stream) throws IOException  {}
	public InputStream getStream(String key)  {return null;}
	public Iterator<String> getStreamKeys()  {return null;}
}


class MyAudioClip implements AudioClip {

	 AudioClip mine;


     MyAudioClip(URL u) {
        mine = java.applet.Applet.newAudioClip(u);
     }


     public void play(){
	 	mine.play();
      }

     public void loop(){ mine.loop();}

     public void stop(){
        mine.stop();
     }

}


class Initial extends Applet {

        String msg =
          "\n          EXAMPLE VIEWER\n\n" +
          "  A quick way of executing the demonstration\n"+
          "  applets from:\n\n"+
          "  Concurrency: State Models and Java Programs.\n\n"+
          "  Use the <Example> menu to select an example.\n\n"+
          "  For the instructions on running an example, \n"+
          "  use a Web brower by clicking on the button below.\n";

        Button browser;

        public void init() {
            setLayout(new BorderLayout());
            TextArea tx = new TextArea(msg,14,45,TextArea.SCROLLBARS_NONE);
			tx.setBackground(Color.white);
            add("Center",tx);
            tx.setEditable(false);
            add("South",browser = new Button("Run examples in Web Browser"));
			browser.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                 try {
                    Runtime rt = Runtime.getRuntime();
                    String cmd[] = new String[3];
                    cmd[0] = "rundll32.exe";
                    cmd[1] = "url.dll,FileProtocolHandler";
                    cmd[2] = System.getProperty("user.dir")+java.io.File.separator+"concurrency.html";
                   rt.exec(cmd);
                  System.out.println(cmd[0]+cmd[1]+cmd[2]);
                 } catch(java.io.IOException ie) {}            
                }
            });
        }

}