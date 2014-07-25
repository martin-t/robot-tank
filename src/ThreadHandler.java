import java.lang.Thread.UncaughtExceptionHandler;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Sound;

public class ThreadHandler implements UncaughtExceptionHandler {

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		Sound.buzz();
		System.out.println("Thread: exception");
		System.out.println(t.getName());
		System.out.println(e.toString());
		System.out.print(e.getMessage());
		Utils.sleep(1000);
		Button.waitForAnyPress();
		System.exit(0);
	}
}
