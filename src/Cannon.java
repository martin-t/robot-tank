import lejos.nxt.MotorPort;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;

public class Cannon {

	NXTRegulatedMotor motorUpDown;
	NXTRegulatedMotor motorWinder;
	NXTRegulatedMotor motorLatch;
	TouchSensor sensorTouch;
	
	boolean ready = false;
	
	public Cannon(){
		motorUpDown = new NXTRegulatedMotor(MotorPort.A);
		motorWinder = new NXTRegulatedMotor(MotorPort.B);
		motorLatch = new NXTRegulatedMotor(MotorPort.C);
		sensorTouch = new TouchSensor(SensorPort.S1);
	}
	
	public void load() {
		if(ready) return;
		
		// stretch
		Utils.print("stretch");
		int start = motorWinder.getTachoCount();
		//print(start);
		motorWinder.forward();
		motorWinder.setSpeed(500);
		while(!sensorTouch.isPressed()){
			Utils.print("stretch " + motorWinder.getTachoCount());
			Utils.sleep(1);
		}
		Utils.print("sensor pressed");
		motorWinder.stop();
		Utils.sleep(100);
		
		// lock
		Utils.print("lock");
		motorLatch.rotate(-90);
		Utils.sleep(100);
		//motorLatch.stop();
		
		// unwind
		Utils.print("unwind");
		motorWinder.backward();
		motorWinder.setSpeed(1000);
		while(motorWinder.getTachoCount() > start){
			Utils.sleep(1);
		}
		motorWinder.stop();
		Utils.sleep(100);
		
		ready = true;
	}
	
	public void fire() {
		// release
		Utils.print("release");
		motorLatch.setAcceleration(Integer.MAX_VALUE);
		motorLatch.rotate(90);
		Utils.sleep(100);
		ready = false;
	}
}
