package GTEs;

import java.util.Scanner;

/**
 * reads from terminal and gives informations
 * @author babz
 *
 */
public class EngineInfoPoint {
	private Scanner sc = new Scanner(System.in);
	private EngineManager engineManager;
	
	public EngineInfoPoint(EngineManager manager) {
		engineManager = manager;
	}

	public void read() {
		//engine commands: load, exit
		while(sc.hasNext()) {
			String command = sc.next();
			if(command.equals("!load")) {
				System.out.println("Current load: " + engineManager.getLoad() + "%");
			} else if (command.equals("!exit")) {
				return;
			} else {
				System.out.println("Invalid command!");
			}
		}
	}

}
