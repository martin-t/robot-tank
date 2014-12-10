import lejos.nxt.MotorPort;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.AccelHTSensor;

public class Turret {

	BtReceiver receiver;
	NXTRegulatedMotor motorUpDown;
	NXTRegulatedMotor motorWinder;
	NXTRegulatedMotor motorLatch;
	TouchSensor sensorTouch;
	AccelHTSensor accel;
	UltrasonicSensor usForward;

	boolean ready = true; // fire is first command
	int tachoStart = 0;

	public Turret(BtReceiver receiver) {
		motorUpDown = new NXTRegulatedMotor(MotorPort.A);
		motorWinder = new NXTRegulatedMotor(MotorPort.B);
		motorWinder.setStallThreshold(10, 50);
		motorLatch = new NXTRegulatedMotor(MotorPort.C);
		motorLatch.setStallThreshold(10, 50);
		sensorTouch = new TouchSensor(SensorPort.S1);
		accel = new AccelHTSensor(SensorPort.S3);
		usForward = new UltrasonicSensor(SensorPort.S2);
		this.receiver = receiver;
		motorUpDown.stop();
		tachoStart = motorWinder.getTachoCount();
		
		//fire(); // open latch and load\
		load(); ready = false;
		
		Thread setupThread = new Thread(new Runnable() {
			@Override
			public void run() {
				motorUpDown.setStallThreshold(50, 200);
				motorUpDown.setSpeed(50);
				motorUpDown.forward();
			}
		});
		setupThread.setName("SetupThread");
		setupThread.start();
	}

	public synchronized void load() {
		Thread loader = new Thread(new Runnable() {
			@Override
			public void run() {
				loadThread();
			}
		});
		loader.setName("Loader");
		loader.start();
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
		motorLatch.rotate(-120);
		Utils.sleep(100);

		// unwind
		motorWinder.backward();
		motorWinder.setSpeed(1000);
		while (motorWinder.getTachoCount() > tachoStart && !Thread.interrupted()) {
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
	
	public void getSensors(){
		receiver.send(Constants.SENSOR_TURRET_US_FORWARD + " " + usForward.getDistance());
		receiver.send(Constants.SENSOR_TURRET_ACCEL + " " + accel.getXAccel() + " " + accel.getYAccel() + " " + accel.getZAccel());
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
		
		motorLatch.rotate(120);
		Utils.sleep(100);
		ready = false;

		load();
	}
}
