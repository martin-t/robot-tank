/* In this static class are variables shared between BTRec class and BTRecT thread 
 */
public class BTRecTData {
	private static boolean alive = true;
	public static void setAlive(boolean Ready){
		alive = Ready;
	}
	public static boolean isAlive(){
		return alive;
	}
	public static void terminate(){
		//terminates BRRecT thread
		alive = false;
	}
}
