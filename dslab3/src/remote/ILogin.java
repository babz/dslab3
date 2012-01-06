package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;


/**
 * Interface that extends Remote so everyone can get methods via RMI
 * @author babz
 *
 */
public interface ILogin extends Remote {
	
	public IUser login(String companyName, String pw) throws RemoteException, ManagementException;

}
