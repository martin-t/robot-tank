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
	
	public Turret(){
		motorUpDown = new NXTRegulatedMotor(MotorPort.A);
		motorWinder = new NXTRegulatedMotor(MotorPort.B);
		motorLatch = new NXTRegulatedMotor(MotorPort.C);
		sensorTouch = new TouchSensor(SensorPort.S1);
	}
	
	public void load() {
		if(ready) return;
		
		// stretch
		int start = motorWinder.getTachoCount();
		Utils.print(Integer.toString(start), 1);
		motorWinder.forward();
		motorWinder.setSpeed(1000);
		while(!sensorTouch.isPressed()){
			Utils.print(Integer.toString(motorWinder.getTachoCount()), 0);
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
		while(motorWinder.getTachoCount() > start){
			Utils.print(Integer.toString(motorWinder.getTachoCount()), 0);
			Utils.sleep(1);
		}
		motorWinder.stop();
		Utils.sleep(100);
		
		ready = true;
	}
	
	public void fire() {
		if(!ready) return;
		
		// release
		motorLatch.rotate(90);
		Utils.sleep(100);
		ready = false;
		
		load(); // TODO new thread
	}
}
