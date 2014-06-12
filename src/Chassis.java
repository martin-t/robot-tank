import lejos.nxt.MotorPort;
import lejos.nxt.NXTRegulatedMotor;

public class Chassis {
	
	NXTRegulatedMotor left;
	NXTRegulatedMotor right;
	NXTRegulatedMotor turret;
	
	public Chassis() {
		left = new NXTRegulatedMotor(MotorPort.A);
		right = new NXTRegulatedMotor(MotorPort.C);
		turret = new NXTRegulatedMotor(MotorPort.B);
	}
	
	public void forward(int speed) {
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
