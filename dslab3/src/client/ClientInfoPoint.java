package client;


import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Scanner;

import remote.IAdminMode;
import remote.ICompanyMode;
import remote.ILogin;
import remote.IUser;
import remote.ManagementException;

/**
 * reads commands from command line of client
 * differentiate between admins and companies in management
 * @author babz
 *
 */
public class ClientInfoPoint {

	private Scanner sc = new Scanner(System.in);
	private ILogin login;
	private ICommandScanner cmdScanner = null;
	private File taskDir;

	public ClientInfoPoint(ILogin rmLogin, File clientTaskDir) throws IOException {
		login = rmLogin;
		taskDir = clientTaskDir;
	}

	public void read() throws RemoteException, ManagementException {
		while(sc.hasNextLine()) {
			String[] cmd = sc.nextLine().split(" ");
			if (cmd[0].equals("!login")) {
				if(cmd.length != 3) {
					System.out.println("You can't login without name and password dude!");
					continue;
				}
				String companyName = cmd[1];
				String pw = cmd[2];
				try {
					IUser user = login.login(companyName, pw);
					if(user.isAdmin()) {
						cmdScanner = new AdminScanner((IAdminMode) user);
						System.out.println("Successfully logged in. Using admin mode.");

					} else {
						cmdScanner = new CompanyScanner((ICompanyMode) user, taskDir);
						System.out.println("Successfully logged in. Using company mode.");
					}

				} catch (ManagementException ex) {
					System.out.println("Wrong name or password.");
				}
			} else if (cmd[0].equals("!logout")) {
				if(cmdScanner == null) {
					System.out.println("You have to log in first.");
					continue;
				}
				cmdScanner.logout();
				cmdScanner = null;
				System.out.println("Successfully logged out.");
			} else if (cmd[0].equals("!exit")) {
				cmdScanner.logout();
				return;
			}else {
				if(cmdScanner == null) {
					System.out.println("You have to log in first.");
					continue;
				}
				try {
					cmdScanner.readCommand(cmd);
				} catch (ManagementException e) {
					System.out.println("Error: " + e.getMessage());
				}
			}				
		}
	}
}
