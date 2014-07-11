import java.io.IOException;

import lejos.nxt.Sound;

public class Main {

	public static void main(String[] args) throws IOException {
		Sound.playTone(2000, 300);
		BtReceiver btReceiver = new BtReceiver();
		btReceiver.connect();
	}

}
