package client;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import remote.ICompanyMode;
import remote.INotifyClientCallback;
import remote.ManagementException;
import remote.NotifyClientCallbackImpl;

public class CompanyScanner implements ICommandScanner {

	private ICompanyMode company;
	private File taskDir;
	private INotifyClientCallback callbackNotification = new NotifyClientCallbackImpl();

	public CompanyScanner(ICompanyMode loggedInCompany, File clientTaskDir) throws RemoteException {
		company = loggedInCompany;
		taskDir = clientTaskDir;
		UnicastRemoteObject.exportObject(callbackNotification, 0);
	}

	@Override
	public void readCommand(String[] cmd) throws RemoteException, ManagementException {
		if (cmd[0].equals("!list")) { //LOCALLY
			if(!checkNoOfArgs(cmd, 0)) {
				return;
			}
			System.out.println(listAllTasksInDir());

		} else if (cmd[0].equals("!credits")) {
			if(!checkNoOfArgs(cmd, 0)) {
				return;
			}
			System.out.println("You have " + company.getCredit() + " credits left.");

		} else if (cmd[0].equals("!buy")) {
			if(!checkNoOfArgs(cmd, 1)) {
				return;
			}
			int credit = Integer.parseInt(cmd[1]);
			company.buyCredits(credit);
			System.out.println("Successfully bought credits. You have " + company.getCredit() + " credits left.");

		} else if (cmd[0].equals("!prepare")) {
			if(!checkNoOfArgs(cmd, 2)) {
				return;
			}
			String taskName = cmd[1];
			String taskType = cmd[2];
			if(!taskExists(taskName)) {
				System.out.println("Task not found.");
				return;
			}
			int id = company.prepareTask(taskName, taskType);
			System.out.println("Task with id " + id + " prepared.");

		} else if (cmd[0].equals("!executeTask")) {
			if(!checkNoOfArgs(cmd, 2)) {
				return;
			}
			int id = Integer.parseInt(cmd[1]);
			String script = cmd[2];
			company.executeTask(id, script, callbackNotification);
			System.out.println("Execution for task " + id + " started.");

		} else if (cmd[0].equals("!info")) {
			if(!checkNoOfArgs(cmd, 1)) {
				return;
			}
			System.out.println(company.getInfo(Integer.parseInt(cmd[1])));

		} else if (cmd[0].equals("!getOutput")) {
			if(!checkNoOfArgs(cmd, 1)) {
				return;
			}
			System.out.println(company.getOutput(Integer.parseInt(cmd[1])));

		} else if (checkForAdminCommand(cmd[0])) {
			System.out.println("Command not allowed. You are not an admin.");
		} else {
			System.out.println("Invalid command");
		}
	}

	private boolean checkForAdminCommand(String cmd) {
		String adminCommands = "!getPricingCurve !setPriceStep";
		return adminCommands.contains(cmd); 
	}

	@Override
	public void logout() throws RemoteException {
		company.logout();
		UnicastRemoteObject.unexportObject(callbackNotification, false);
	}

	private boolean checkNoOfArgs(String[] cmd, int noOfSupposedArgs) {
		if((cmd.length - 1) != noOfSupposedArgs) {
			System.out.println("Supposed number of arguments: " + noOfSupposedArgs);
			return false;
		}
		return true;
	}

	private String listAllTasksInDir() {
		String[] tmp = taskDir.list();
		String allTasks = "";
		for(int i = 0; i < tmp.length; i++) {
			allTasks += tmp[i] + "\n";
		}
		return allTasks;
	}

	private boolean taskExists(String taskName) {
		if(!listAllTasksInDir().contains(taskName)) {
			return false;
		}
		return true;
	}
}

