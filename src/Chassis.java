import lejos.nxt.LCD;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;

public class Chassis {

	NXTRegulatedMotor left;
	NXTRegulatedMotor right;
	NXTRegulatedMotor turret;
	UltrasonicSensor usForward;
	UltrasonicSensor usDownFront;
	UltrasonicSensor usDownBack;

	BtReceiver receiver;
	boolean overrideFailSafe = false;
	boolean canForward = true;
	boolean canBackward = true;
	final int FAILSAFE_DISTANCE = 20;
	Thread failSafe;

	public Chassis(BtReceiver receiver) {
		left = new NXTRegulatedMotor(MotorPort.A);
		right = new NXTRegulatedMotor(MotorPort.C);
		turret = new NXTRegulatedMotor(MotorPort.B);
		usForward = new UltrasonicSensor(SensorPort.S4);
		usDownFront = new UltrasonicSensor(SensorPort.S3);
		usDownBack = new UltrasonicSensor(SensorPort.S2);
		this.receiver = receiver;
		startFailSafeMode();
	}

	public void setFailsafeOverride(boolean overrideFailSafe) {
		this.overrideFailSafe = overrideFailSafe;
	}

	public void getSensors() {
		receiver.send(Constants.SENSOR_CHASSIS_US_FORWARD + " "
				+ usForward.getDistance());
		receiver.send(Constants.SENSOR_CHASSIS_US_DOWN_FRONT + " "
				+ usDownFront.getDistance());
		receiver.send(Constants.SENSOR_CHASSIS_US_DOWN_BACK + " "
				+ usDownBack.getDistance());
	}

	public void startFailSafeMode() {
		failSafe = new Thread(new Runnable() {
			@Override
			public void run() {
				while (!Thread.interrupted()) {
					if (usDownFront.getDistance() > FAILSAFE_DISTANCE
							&& !overrideFailSafe) {
						if (canForward) {
							canForward = false;
							forward(0);
						}
					} else {
						canForward = true;
					}
					if (usDownBack.getDistance() > FAILSAFE_DISTANCE
							&& !overrideFailSafe) {
						if (canBackward) {
							canBackward = false;
							backward(0);
						}
					} else {
						canBackward = true;
					}
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						System.out.println("FS Interrupted");
						return;
					}
				}
			}
		});
		failSafe.setName("Chassis");
		failSafe.start();
	}

	public void stopFailSafeMode() {
		if (failSafe != null) {
			failSafe.interrupt();
		}
	}

	public synchronized void forward(int speed) {
		if (speed == 0) {
			left.setSpeed(0);
			right.setSpeed(0);
			left.stop();
			right.stop();
		}
		if (!canForward)
			return;
		left.setSpeed(speed);
		left.backward();
		right.setSpeed(speed);
		right.backward();
	}

	public synchronized void backward(int speed) {
		if (speed == 0) {
			left.setSpeed(0);
			right.setSpeed(0);
			left.stop();
			right.stop();
		}
		if (!canBackward)
			return;
		left.setSpeed(speed);
		left.forward();
		right.setSpeed(speed);
		right.forward();
	}

	public synchronized void chassisLeft(int speed) {
		if (!canForward || !canBackward)
			return;
		if (speed == 0) {
			left.setSpeed(0);
			right.setSpeed(0);
			left.stop();
			right.stop();
		}
		left.setSpeed(speed);
		left.backward();
		right.setSpeed(speed);
		right.forward();
	}

	public synchronized void chassisRight(int speed) {
		if (!canForward || !canBackward)
			return;
		if (speed == 0) {
			left.setSpeed(0);
			right.setSpeed(0);
			left.stop();
			right.stop();
		}
		left.setSpeed(speed);
		left.forward();
		right.setSpeed(speed);
		right.backward();
	}

	public synchronized void turretLeft(int speed) {
		if (speed == 0) {
			turret.setSpeed(0);
			turret.stop();
		}
		turret.setSpeed(speed);
		turret.backward();
	}

	public synchronized void turretRight(int speed) {
		if (speed == 0) {
			turret.setSpeed(0);
			turret.stop();
		}
		turret.setSpeed(speed);
		turret.forward();
	}
	
	public synchronized void setSpeedLeft(int speed) {
		if (speed < 0) {
			left.backward();
		} else {
			left.forward();
		}
		left.setSpeed(speed);
	}
	
	public synchronized void setSpeedRight(int speed) {
		if (speed < 0) {
			right.backward();
		} else {
			right.forward();
		}
		right.setSpeed(speed);
	}
}
