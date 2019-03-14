/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author matheus
 */
import java.lang.Runnable;
import java.lang.Thread;
class MainRadio implements Runnable {
    Thread objThread;
    Radio objRadio;
    On objOn;
    Menu objMenu;
    Scan objScan;
    Scan1 objScan1;
    Scan2 objScan2;
    Scan3 objScan3;
    Scan4 objScan4;
    Scan5 objScan5;
    Scan6 objScan6;
    Lock objLock;
    Reset objReset;
    Stop2 objStop2;
    
    public void start(){
        objRadio = new Radio();
        objOn = new On();
        objMenu = new Menu();
        objScan = new Scan();
        objScan1 = new Scan1();
        objScan2 = new Scan2();
        objScan3 = new Scan3();
        objScan4 = new Scan4();
        objScan5 = new Scan5();
        objScan6 = new Scan6();
        objLock = new Lock();
        objReset = new Reset();
        objStop2 = new Stop2();
        objThread = new Thread(this);
        objThread.start();
    }
    public void run(){
        while(true){
            objRadio.on();
            objOn.fm108();
            objMenu.scan();
            objScan.fm107();
            objMenu.scan();
            objScan1.fm106();
            objMenu.scan();
            objScan2.lock();
            objLock.reset();
            objOn.fm108();
            objMenu.scan();
            objScan.fm107();
            objMenu.scan();
            objScan1.fm106();
            objMenu.scan();
            objScan2.fm105();
            objMenu.scan();
            objScan3.fm104();
            objMenu.scan();
            objScan4.fm103();
            objMenu.scan();
            objScan5.fm102();
            objScan6.fm108();
            
            return;
        }
    }
    public static void main(String[]args){
        MainRadio objMainRadio = new MainRadio();
        objMainRadio.start();
    }
}
