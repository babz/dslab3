package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IAdminMode extends Remote, IUser {

	
	/**
	 * prints out the steps of the pricing curve
	 * @throws RemoteException
	 */
	public String getPricingCurve() throws RemoteException;
	
	/**
	 * inserts a new or updates an existing price step
	 * @param taskCount
	 * @param percent
	 * @throws RemoteException thrown if taskCount or percentage invalid
	 * @throws ManagementException 
	 */
	public void setPriceStep(int taskCount, double percent) throws RemoteException, ManagementException;
	
}
