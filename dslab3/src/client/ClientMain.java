package client;

import java.io.File;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import propertyReader.RegistryReader;
import remote.ILogin;
import remote.ManagementException;


public class ClientMain {

	public static final Logger LOG = Logger.getLogger(ClientMain.class);
	
	/**
	 * @param args
	 * @throws NoSuchAlgorithmException 
	 * @throws NumberFormatException 
	 * @throws InvalidKeyException 
	 */
	public static void main(String[] args) throws InvalidKeyException, NumberFormatException, NoSuchAlgorithmException {

		// Set up a simple configuration that logs on the console.
		BasicConfigurator.configure();

		//		params = String schedulerHost, int schedulerTCPPort, String taskDir
		int noOfParams = 2;
		if(args.length != noOfParams) {
			System.out.println("Error: Too few arguments!");
			return;
		}

		String mgmtComponent = args[0];
		File taskDir = new File(args[1]);

		ILogin login = null;
		try {
			RegistryReader registryDetails = new RegistryReader(); 
			Registry registry = LocateRegistry.getRegistry(registryDetails.getRegistryHost(), registryDetails.getRegistryPort());
			login = (ILogin) registry.lookup(mgmtComponent);
			LOG.info("registry looked up");
			//TODO comp.executeTask() -> aufruf der engineAnforderung u der prepare
		} catch (NotBoundException e) {
			LOG.error("client was not bound");
			System.err.println("ClientMain Exception");
			e.printStackTrace();
		} catch (IOException ioExc) {
			LOG.error("io exc in clientMain");
		}

		ClientInfoPoint commandReader;
		try {
			commandReader = new ClientInfoPoint(login, taskDir);
			try {
				commandReader.read();
			} catch (ManagementException e) {
				System.err.println("Fix your management!");
				e.printStackTrace();
			} catch (RemoteException re) {
				System.err.println("remote exception from scanner classes");
				re.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
