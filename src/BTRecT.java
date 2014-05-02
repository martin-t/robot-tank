import java.io.DataInputStream;

import lejos.nxt.LCD;

/* This thread reads data sent from Android and saves them into BTRecTData static class
 * until it's requested stop doing it 
 */

/*public class BTRecT extends Thread {
	private DataInputStream dis;
	
	public BTRecT(DataInputStream DIS){
		dis = DIS;
	}
	
	public String[] split(String spliter, String S){
		int charID = 0;
		int nSubS = 0;
		while(S.indexOf(spliter,charID) != -1){
			nSubS++;
			charID = S.indexOf(spliter,charID)+1;
		}
		charID = 0;
		int sID = 0;
		String[] command = new String[nSubS+1];
		for(int i = 0;i<S.indexOf(" ");i++){
			if(S.charAt(i) == spliter.charAt(0)){
				command[sID] = (String) S.subSequence(charID, i);
				charID = i;
				sID++;
			}
		}
		for(int i = charID;i<S.length();i++){
			if(S.charAt(i) == spliter.charAt(0)){
				command[sID] = (String) S.subSequence(charID+1, i);
				charID = i;
				sID++;
			}
		}
		command[sID] = (String) S.subSequence(charID, S.length());
		return command;
	}
	
	public void run(){
		while(BTRecTData.isAlive()){
			try {
				
			} catch (Exception e) {
				LCD.clear();
				LCD.drawString("Recieving error:", 0, 0);
				LCD.drawString(e.getMessage(), 0, 1);
			}
			
		}
	}
}*/
