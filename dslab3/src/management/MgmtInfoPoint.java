package management;

import java.io.IOException;
import java.util.Scanner;

public class MgmtInfoPoint {

	private Scanner sc = new Scanner(System.in);
	private UserManager userManager;

	public MgmtInfoPoint() throws IOException {
		userManager = UserManager.getInstance();
		read();
	}

	private void read() {
		while(sc.hasNext()) {
			String command = sc.next();
			if (command.equals("!users")) {
				System.out.println(userManager.getUsersAndTasks());
			} else if (command.equals("!exit")) {
				return;
			} else {
				System.out.println("Invalid command!");
			}
		}
	}

}
