package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * super interface for companies and admins
 * @author babz
 *
 */
public interface IUser extends Remote {

	boolean isAdmin() throws RemoteException;
	
	void logout() throws RemoteException;

}
