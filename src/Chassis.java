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

	public void overrideBlockable(boolean overrideFailSafe) {
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
				while (true) {
					if (usDownFront.getDistance() > FAILSAFE_DISTANCE
							&& !overrideFailSafe) {
						if (canForward) {
							left.setSpeed(0);
							right.setSpeed(0);
							left.stop();
							right.stop();
							canForward = false;
						}
					} else {
						canForward = true;
					}
					if (usDownBack.getDistance() > FAILSAFE_DISTANCE
							&& !overrideFailSafe) {
						if (canBackward) {
							left.setSpeed(0);
							right.setSpeed(0);
							left.stop();
							right.stop();
							canBackward = false;
						}
					} else {
						canBackward = true;
					}
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						return;
					}
				}
			}
		});
		failSafe.start();
	}

	public void stopFailSafeMode() {
		if (failSafe != null) {
			failSafe.interrupt();
		}
	}

	public void forward(int speed) {
		if (!canForward)
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
		right.backward();
	}

	public void backward(int speed) {
		if (!canBackward)
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
		right.forward();
	}

	public void chassisLeft(int speed) {
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

	public void chassisRight(int speed) {
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

	public void turretLeft(int speed) {
		if (speed == 0) {
			turret.setSpeed(0);
			turret.stop();
		}
		turret.setSpeed(speed);
		turret.backward();
	}

	public void turretRight(int speed) {
		if (speed == 0) {
			turret.setSpeed(0);
			turret.stop();
		}
		turret.setSpeed(speed);
		turret.forward();
	}
}
