import lejos.nxt.LCD;

public class Utils {
	public static void print(int i){
		LCD.clear();
		LCD.drawInt(i, 0, 0);
	}
	
	public static void print(String s){
		LCD.clear();
		LCD.drawString(s, 0, 0);
	}
	
	public static void sleep(int t){
		try {
				Thread.sleep(t);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
