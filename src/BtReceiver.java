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
	boolean noPingYet = true;
	Chassis chassis;
	Turret turret;
	Thread sensorsSending;

	public BtReceiver() {
		brickName = Bluetooth.getFriendlyName();
	}

	public void connect() {
		// trying to establish a connection until it's done
		while (!establishConnection());

		while (connected) {
			if (noPingYet) {
				send(Constants.CONNECTED);
				Utils.sleep(50);
			}
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

	public synchronized void send(String data) {
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

	private void startSendingSensorsData() {
		sensorsSending = new Thread(new Runnable() {
			@Override
			public void run() {
				while (!Thread.interrupted()) {
					if (chassis != null) {
						chassis.getSensors();
					}
					if (turret != null) {
						turret.getSensors();
						
					}
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						return;
					}
				}
			}
		});
		sensorsSending.setName("BtReceiver");
		sensorsSending.start();
	}

	private void stopSendingSensorsData() {
		if (sensorsSending != null) {
			sensorsSending.interrupt();
		}
	}

	private void readData() {
		String strRec;
		try {
			strRec = dis.readUTF();
			Utils.print(strRec, 0);
		} catch (IOException e) {
			shutdown();
			return;
		}
		
		String received[] = Utils.split(strRec);
		String cmd = received[0];
		/*if (!strRec.startsWith(Constants.PING)) {
			Utils.print(strRec, 0);
		}*/

		if (cmd.equals(Constants.SET_CHASSIS)) {
			chassis = new Chassis(this);
		} else if (cmd.equals(Constants.SET_TURRET)) {
			turret = new Turret(this);
		} else if (cmd.equals(Constants.FIRE)) {
			turret.fire();
		} else if (cmd.equals(Constants.PING)) {
			if (noPingYet) {
				startSendingSensorsData();
				noPingYet = false;
			}
			send(strRec);
		} else if (cmd.equals(Constants.SHUTODWN)) {
			shutdown();
		} else if (cmd.equals(Constants.OVERRIDE_FAILSAFE)) {
			chassis.setFailsafeOverride(Boolean.parseBoolean(received[1]));
		} else if (cmd.equals(Constants.FORWARD)) {
			chassis.forward(Integer.parseInt(received[1]));
		} else if (cmd.equals(Constants.CHASSIS_LEFT)) {
			chassis.chassisLeft(Integer.parseInt(received[1]));
		} else if (cmd.equals(Constants.CHASSIS_RIGHT)) {
			chassis.chassisRight(Integer.parseInt(received[1]));
		} else if (cmd.equals(Constants.BACKWARD)) {
			chassis.backward(Integer.parseInt(received[1]));
		} else if (cmd.equals(Constants.UP)) {
			turret.up(Integer.parseInt(received[1]));
		} else if (cmd.equals(Constants.TURRET_LEFT)) {
			chassis.turretLeft(Integer.parseInt(received[1]));
		} else if (cmd.equals(Constants.TURRET_RIGHT)) {
			chassis.turretRight(Integer.parseInt(received[1]));
		} else if (cmd.equals(Constants.DOWN)) {
			turret.down(Integer.parseInt(received[1]));
		} else if (cmd.equals(Constants.SET_SPEED_LEFT)) {
			chassis.setSpeedLeft(Integer.parseInt(received[1]));
		} else if (cmd.equals(Constants.SET_SPEED_RIGHT)) {
			chassis.setSpeedRight(Integer.parseInt(received[1]));
		} else {
			Utils.print("read: " + cmd, 1);
		}
	}

	private void shutdown() {
		Utils.print("Shutdown");
		if (chassis != null)
			chassis.stopFailSafeMode();
		stopSendingSensorsData();
		Utils.sleep(150);
		System.exit(0);
	}
}
