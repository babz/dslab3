package client;

import java.rmi.RemoteException;

import remote.ManagementException;

public interface ICommandScanner {

	/**
	 * reads commands from commandline
	 * @param cmd command with args
	 * @throws RemoteException
	 * @throws ManagementException 
	 */
	public void readCommand(String[] cmd) throws RemoteException, ManagementException;
	
	/**
	 * logs out either company or admin
	 * @throws RemoteException
	 */
	public void logout() throws RemoteException;
	
}
