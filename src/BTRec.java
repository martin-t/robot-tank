import java.io.DataInputStream;
import java.io.DataOutputStream;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.comm.*;

/* This class provides communication between NXT and Android. 
 * public methods: connect(), disconnect(), send() and read()
 * Note: after creating an instance of this class and calling connect() method,
 * a thread BTRecT will be created
 */

public class BTRec {
	NXTConnection BTC;
	BTRecT BTRT;
	DataInputStream dis;
	DataOutputStream dos;
	public void connect(){
		//trying to establish a connection until it's done
		while(!establishConnection()){
			;
		}
	}
	private boolean establishConnection(){
		//trying to establish a connection only once
		try {
			LCD.clear();
			LCD.drawString("Waiting for connection", 0, 0);
			BTC = Bluetooth.waitForConnection();
			if(BTC==null){
				LCD.drawString("Connection error!", 0, 1);
				LCD.drawString("Press enter...", 0, 2);
				while(Button.waitForAnyPress() != Button.ID_ENTER){
					;
				}
				LCD.clear();
				return false;
			}
			LCD.drawString("Opening streams", 0, 1);
			dis = BTC.openDataInputStream();
			dos = BTC.openDataOutputStream();
			BTRecT BTRT = new BTRecT(dis);
			BTRT.start();
			LCD.drawString("Streams opened", 0, 2);
			LCD.drawString("Connection established", 0, 3);
			LCD.drawString("Press enter...", 0, 4);
			while(Button.waitForAnyPress() != Button.ID_ENTER){
				;
			}
			LCD.clear();
			return true;
		} catch (Exception e) {
			LCD.drawString("Connection error:", 0, 1);
			LCD.drawString(e.getMessage(), 0, 2);
			LCD.drawString("Press enter...", 0, 3);
			while(Button.waitForAnyPress() != Button.ID_ENTER){
				;
			}
			LCD.clear();
			return false;
		}
	}
	public void disconnect(){
		//close all streams and BRRecT thread
		BTRecTData.terminate();
		try {
			dis.close();
			dos.close();
			BTC.close();
		}
		catch (Exception e) {
			LCD.clear();
			LCD.drawString("Disconnecting error:", 0, 0);
			LCD.drawString(e.getMessage(), 0, 1);
		}
	}
	public void send(String data){
		//sends data into Android 
		try{	
			dos.writeUTF(data);
			dos.flush();
		} 
		catch (Exception e) {
			LCD.clear();
			LCD.drawString("Sending error:", 0, 0);
			LCD.drawString(e.getMessage(), 0, 1);
		}
	}
}
