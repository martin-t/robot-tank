import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Sound;
import lejos.nxt.comm.*;

/* This class provides communication between NXT and Android. 
 * public methods: connect(), disconnect(), send() and read()
 * Note: after creating an instance of this class and calling connect() method,
 * a thread BTRecT will be created
 */

public class BtReceiver {
	String brickName;
	
	NXTConnection nxtConnection;
	DataInputStream dis;
	DataOutputStream dos;
	boolean connected = false;
	Chassis chassis;
	Turret turret;
	
	public BtReceiver(){
		brickName = Bluetooth.getFriendlyName();
	}
	
	public void connect() {
		// trying to establish a connection until it's done
		while (!establishConnection())
			;
		send(Constants.CONNECTED);
		while (connected) {
			readData();
		}
	}

	private boolean establishConnection() {
		// trying to establish a connection only once
		try {
			Utils.print("Waiting: " + brickName);
			nxtConnection = Bluetooth.waitForConnection();
			if (nxtConnection == null) {
				LCD.drawString("Connection error!", 0, 1);
				LCD.drawString("Press enter...", 0, 2);
				while (Button.waitForAnyPress() != Button.ID_ENTER) {
					;
				}
				LCD.clear();
				return false;
			}
			Utils.print("Opening streams");
			dis = nxtConnection.openDataInputStream();
			dos = nxtConnection.openDataOutputStream();
			connected = true;
			Utils.print("Connected!");
			return true;
		} catch (Exception e) {
			Utils.print(e.getMessage());
			while (Button.waitForAnyPress() != Button.ID_ENTER) {
				;
			}
			LCD.clear();
			return false;
		}
	}
	
	public void disconnect() {
		// close all streams
		try {
			dis.close();
			dos.close();
			nxtConnection.close();
			connected = false;
		} catch (Exception e) {
			LCD.clear();
			LCD.drawString("Disconnecting error:", 0, 0);
			LCD.drawString(e.getMessage(), 0, 1);
		}
	}
	
	public void send(String data) {
		// sends data into Android
		try {
			dos.writeUTF(data);
			dos.flush();
		} catch (Exception e) {
			LCD.clear();
			LCD.drawString("Sending error:", 0, 0);
			LCD.drawString(e.getMessage(), 0, 1);
		}
	}
	
	private void readData() {
		try {
			String strRec = dis.readUTF();
			String received[] = Utils.split(strRec);
			String cmd = received[0];
			Utils.print(cmd);
			if (cmd.equals(Constants.SET_CHASSIS)) {
				chassis = new Chassis();
			} else if (cmd.equals(Constants.SET_TURRET)) {
				turret = new Turret();
				turret.load();
			} else if (cmd.equals(Constants.FIRE)) {
				turret.fire();
			} else if(cmd.equals(Constants.PING)){
				send(strRec);
			} else {
				Utils.print("read: " + cmd, 1);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
