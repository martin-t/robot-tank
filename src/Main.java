import java.io.IOException;

import lejos.addon.keyboard.Keyboard;
import lejos.nxt.Button;
import lejos.nxt.Motor;

public class Main {

	public static void main(String[] args) throws IOException {
		BtReceiver btReceiver = new BtReceiver();
		btReceiver.connect();
		
		/*Cannon cannon = new Cannon();
		cannon.load();
		
		while(true){
			Button.waitForAnyPress();
			cannon.fire();
			cannon.load();
		}*/
		
		//while(true);
	}

}
