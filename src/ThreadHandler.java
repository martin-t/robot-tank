import java.lang.Thread.UncaughtExceptionHandler;

import lejos.nxt.Button;
import lejos.nxt.LCD;

public class ThreadHandler implements UncaughtExceptionHandler {

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		System.out.println("Thread: exception");
		System.out.println(t.getName());
		System.out.println(e.toString());
		System.out.print(e.getMessage());
		Utils.sleep(1000);
		Button.waitForAnyPress();
		System.exit(0);
	}
}
