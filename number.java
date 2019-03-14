import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class number extends Applet implements ActionListener {
	String on = "FM108";
	Number2 display;// = new Number2("Matheus");
	int cont=0,stop=0;
	//Number3 display2;
	public void init() {
		add(display=new Number2("RADIO"));
    	display.setSize(300,300	);
    	display.setvalue("OFF");

		button1 = new Button("On");
		add(button1);
		button1.addActionListener(this);

		button2 = new Button("Scan");
		add(button2);
		button2.addActionListener(this);

		button3 = new Button("Reset");
		add(button3);
		button3.addActionListener(this);

		button4 = new Button("Off");
		add(button4);
		button4.addActionListener(this);

		button5 = new Button("Lock");
		add(button5);
		button5.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == button1 && stop==0) {
			//System.out.println("On");
			display.setvalue(on);
			cont=1;
		}
		else if(e.getSource() == button2){
			if(cont==1 && stop==0){
				display.setvalue("FM107");
			}else if(cont==2 && stop==0){
				display.setvalue("FM106");
			}else if(cont==3 && stop==0){
				display.setvalue("FM105");
			}else if(cont==4 && stop==0){
				display.setvalue("FM104");
			}else if(cont==5 && stop==0){
				display.setvalue("FM103");
			}else if(cont==6 && stop==0){
				display.setvalue("FM102");
			}else if(cont==7 && stop==0){
				display.setvalue("FM108");
			}
			if(cont==7){
				cont=1;
			}else{
				cont++;
			}

			//System.out.println("Scan");
		}else if(e.getSource() == button3 && cont>0){
			display.setvalue("FM108");
			cont=1;
			//System.out.println("Reset");
		}else if(e.getSource() == button4){
			if(cont>=0){
				display.setvalue("OFF");
				cont=0;
			}
			display.setvalue("OFF");
			cont=0;
			//System.out.println("Off");
		}else if(e.getSource() == button5 && cont>0){
			//display.setvalue("LOCK");
			stop=1;
			//System.out.println("Lock");
		}
	}

	Button button1, button2,button3,button4,button5;
}
