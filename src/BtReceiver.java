import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.comm.*;

/* This class provides communication between NXT and Android. 
 * public methods: connect(), disconnect(), send() and read()
 * Note: after creating an instance of this class and calling connect() method,
 * a thread BTRecT will be created
 */

public class BtReceiver {
	NXTConnection nxtConnection;
	DataInputStream dis;
	DataOutputStream dos;
	boolean connected = false;
	public void connect(){
		//trying to establish a connection until it's done
		while(!establishConnection());
		
		Utils.print("established!");
		
		while(connected){
			try {
				Utils.print(dis.readUTF());
				send("444");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	private boolean establishConnection(){
		//trying to establish a connection only once
		try {
			Utils.print("Waiting for connection");
			nxtConnection = Bluetooth.waitForConnection();
			if(nxtConnection==null){
				LCD.drawString("Connection error!", 0, 1);
				LCD.drawString("Press enter...", 0, 2);
				while(Button.waitForAnyPress() != Button.ID_ENTER){
					;
				}
				LCD.clear();
				return false;
			}
			Utils.print("Opening streams");
			dis = nxtConnection.openDataInputStream();
			dos = nxtConnection.openDataOutputStream();
			Utils.print("Streams opened");
			connected = true;
			return true;
		} catch (Exception e) {
			Utils.print(e.getMessage());
			while(Button.waitForAnyPress() != Button.ID_ENTER){
				;
			}
			LCD.clear();
			return false;
		}
	}
	
	public void disconnect(){
		//close all streams and BRRecT thread
		try {
			dis.close();
			dos.close();
			nxtConnection.close();
		}
		catch (Exception e) {
			LCD.clear();
			LCD.drawString("Disconnecting error:", 0, 0);
			LCD.drawString(e.getMessage(), 0, 1);
		}
	}
	
	public void send(int data){
		//sends data into Android 
		try{	
			dos.writeInt(data);
			dos.flush();
		} 
		catch (Exception e) {
			LCD.clear();
			LCD.drawString("Sending error:", 0, 0);
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
