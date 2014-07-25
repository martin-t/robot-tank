import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Sound;

public class Main {

	public static void main(String[] args) {
		try {
			Thread.setDefaultUncaughtExceptionHandler(new ThreadHandler());
			Sound.playTone(2000, 300);
			BtReceiver btReceiver = new BtReceiver();
			btReceiver.connect();
		} catch (Exception e) {
			System.out.println("Main: exception");
			System.out.println(e.toString());
			System.out.print(e.getMessage());
			Utils.sleep(1000);
			Button.waitForAnyPress();
			System.exit(0);
		}
	}
}
