package management;

import java.io.File;
import java.io.IOException;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import propertyReader.RegistryReader;
import remote.ILogin;


/**
 * handles authentication and billing etc for the clients (companies)
 * @author babz
 *
 */
public class ManagementMain {

	private static int schedulerTCPPort, preparationCosts;
	private static String schedulerHost;
	
	/**
	 * @param args
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) {

		// params = String bindingName, String schedulerHost, int schedulerTCPPort, int preparationCosts, String taskDir
		int noOfParams = 5;
		if(args.length != noOfParams) {
			System.out.println("Error: Too few arguments!");
			return;
		}

		String bindingName = args[0];
		schedulerHost = args[1];
		schedulerTCPPort = Integer.parseInt(args[2]);
		preparationCosts = Integer.parseInt(args[3]);
		File taskDir = new File(args[4]); //optional

		LoginImpl login = null;
		Registry registry = null;
		
		try {
			RegistryReader registryLocation = new RegistryReader();
			//Creates and exports a Registry instance on the local host that accepts requests on the specified port.
			registry = LocateRegistry.createRegistry(registryLocation.getRegistryPort());
			login = new LoginImpl();
			UnicastRemoteObject.exportObject(login, 0);
			//register name in registry
			registry.bind(bindingName, login);
		} catch (IOException e1) {
			System.out.println("Something wrong with registry.properties");
			e1.printStackTrace();
		} catch (AlreadyBoundException e) {
			System.err.println("mgmt registry already bound");
			e.printStackTrace();
		}

		MgmtInfoPoint commandReader;
		try {
			commandReader = new MgmtInfoPoint();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				//logout all clients
				login.logoutAll();
				registry.unbind(bindingName);
				UnicastRemoteObject.unexportObject(login, true);
			} catch (AccessException e) {
			} catch (RemoteException e) {
			} catch (NotBoundException e) {
			}
		}
	}

	public static synchronized int getSchedulerTCPPort() {
		return schedulerTCPPort;
	}

	public static synchronized int getPreparationCosts() {
		return preparationCosts;
	}

	public static synchronized String getSchedulerHost() {
		return schedulerHost;
	}
	
}
