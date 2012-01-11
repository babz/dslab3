package client;

import java.io.IOException;
import java.rmi.RemoteException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import remote.ManagementException;

public interface ICommandScanner {

	/**
	 * reads commands from commandline
	 * @param cmd command with args
	 * @throws RemoteException
	 * @throws ManagementException 
	 * @throws IOException 
	 * @throws NoSuchAlgorithmException 
	 * @throws NumberFormatException 
	 * @throws InvalidKeyException 
	 */
	public void readCommand(String[] cmd) throws RemoteException, ManagementException, InvalidKeyException, NumberFormatException, NoSuchAlgorithmException, IOException;
	
	/**
	 * logs out either company or admin
	 * @throws RemoteException
	 */
	public void logout() throws RemoteException;
	
}
