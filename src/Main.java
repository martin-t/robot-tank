import java.io.IOException;

import lejos.nxt.Button;
import lejos.nxt.LCD;

public class Main {

	public static void main(String[] args) throws IOException {
		LCD.drawString("Program has started", 0, 0);
		BTRec BTR = new BTRec();
		LCD.drawString("Press enter...", 0, 1);
		while(Button.waitForAnyPress() != Button.ID_ENTER){
			;
		}
		BTR.connect();
		while(true){
			;
		}
	}

}
