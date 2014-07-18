import lejos.nxt.MotorPort;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;

public class Turret {

	NXTRegulatedMotor motorUpDown;
	NXTRegulatedMotor motorWinder;
	NXTRegulatedMotor motorLatch;
	TouchSensor sensorTouch;

	boolean ready = false;
	int tachoStart = 0;

	public Turret() {
		motorUpDown = new NXTRegulatedMotor(MotorPort.A);
		motorWinder = new NXTRegulatedMotor(MotorPort.B);
		motorWinder.setStallThreshold(10, 50);
		motorLatch = new NXTRegulatedMotor(MotorPort.C);
		motorLatch.setStallThreshold(10, 50);
		sensorTouch = new TouchSensor(SensorPort.S1);
		motorUpDown.stop();
		tachoStart = motorWinder.getTachoCount();
	}

	public synchronized void load() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				loadThread();
			}
		}).start();
	}

	private synchronized void loadThread() {
		if (ready)
			return;

		// stretch
		Utils.print("Tacho: " + Integer.toString(tachoStart), 1);
		motorWinder.forward();
		motorWinder.setSpeed(1000);
		while (!sensorTouch.isPressed()) {
			Utils.print(
					"Tacho: " + Integer.toString(motorWinder.getTachoCount()),
					0);
			Utils.sleep(1);
		}
		motorWinder.stop();
		Utils.sleep(100);

		// lock
		motorLatch.rotate(-90);
		Utils.sleep(100);

		// unwind
		motorWinder.backward();
		motorWinder.setSpeed(1000);
		while (motorWinder.getTachoCount() > tachoStart) {
			Utils.print(Integer.toString(motorWinder.getTachoCount()), 0);
			Utils.sleep(1);
		}
		motorWinder.stop();
		Utils.sleep(100);

		ready = true;
	}

	public void fire() {
		if (!ready)
			return;
		fireSync();
	}

	public void up(int speed) {
		if (speed == 0) {
			motorUpDown.setSpeed(0);
			motorUpDown.stop();
		}
		motorUpDown.setSpeed(speed);
		motorUpDown.backward();
	}

	public void down(int speed) {
		if (speed == 0) {
			motorUpDown.setSpeed(0);
			motorUpDown.stop();
		}
		motorUpDown.setSpeed(speed);
		motorUpDown.forward();
	}

	private synchronized void fireSync() {
		if (!ready)
			return;

		// release
		motorLatch.rotate(90);
		Utils.sleep(100);
		ready = false;

		load();
	}
}
