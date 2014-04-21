import java.io.IOException;
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
		Motor.A.backward();
		Motor.A.setSpeed(10);
		try {
			Thread.sleep(50000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Motor.A.stop();
		while(true);
		//Cannon.fire();
	}

}
