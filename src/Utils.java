import java.util.ArrayList;
import lejos.nxt.LCD;

public class Utils {
	public static void print(int i) {
		LCD.clear();
		LCD.drawInt(i, 0, 0);
	}

	public static void print(String s) {
		LCD.clear();
		LCD.drawString(s, 0, 0);
	}

	public static void print(String s, int y) {
		LCD.clear(y);
		LCD.drawString(s, 0, y);
	}

	public static void sleep(int t) {
		try {
			Thread.sleep(t);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	public static String[] split(String s) {
		String sep = " ";
		ArrayList<String> list = new ArrayList<>();
		int startIndex = 0;
		while (true) {
			int index = s.indexOf(sep, startIndex);
			if (index == -1)
				break;
			list.add(s.substring(startIndex, index));
			startIndex = index + 1;
		}
		list.add(s.substring(startIndex));
		return list.toArray(new String[list.size()]);
	}
}
