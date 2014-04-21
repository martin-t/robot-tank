import lejos.nxt.LCD;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;

public class Cannon {

	public static void fire() {
		NXTRegulatedMotor motor1 = new NXTRegulatedMotor(MotorPort.A);
		NXTRegulatedMotor motor2 = new NXTRegulatedMotor(MotorPort.B);
		TouchSensor sensor = new TouchSensor(SensorPort.S1);
		
		// stretch
		print("stretch");
		int start = motor1.getTachoCount();
		//print(start);
		motor1.forward();
		motor1.setSpeed(500);
		while(!sensor.isPressed()){
			print("stretch " + motor1.getTachoCount());
			sleep(1);
		}
		print("sensor pressed");
		motor1.stop();
		//print(motor1.getTachoCount());
		/*int stop = motor1.getTachoCount();
		int diff = stop - start;*/
		
		// hold
		print("hold");
		motor2.rotate(-90);
		motor2.stop();
		
		// unwind
		print("unwind");
		motor1.backward();
		motor1.setSpeed(500);
		while(motor1.getTachoCount() > start){
			sleep(1);
		}
		motor1.stop();
		
		// release
		print("release");
		motor2.rotate(90);
		
		// end
		while(true){
			
		}
	}
	
	private static void print(int i){
		LCD.clear();
		LCD.drawInt(i, 0, 0);
	}
	
	private static void print(String s){
		LCD.clear();
		LCD.drawString(s, 0, 0);
	}
	
	private static void sleep(int t){
		try {
				Thread.sleep(t);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

}
