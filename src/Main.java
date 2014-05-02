import java.io.IOException;

import lejos.addon.keyboard.Keyboard;
import lejos.nxt.Button;
import lejos.nxt.Motor;

public class Main {

	public static void main(String[] args) throws IOException {
		/*LCD.drawString("Program has started", 0, 0);
		BTRec BTR = new BTRec();
		LCD.drawString("Press enter...", 0, 1);
		while(Button.waitForAnyPress() != Button.ID_ENTER){
			;
		}
		BTR.connect();
		while(true){
			;
		}*/
		
		Cannon cannon = new Cannon();
		cannon.load();
		
		while(true){
			Button.waitForAnyPress();
			cannon.fire();
			cannon.load();
		}
		
		//while(true);
	}

}
