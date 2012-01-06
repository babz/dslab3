package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface INotifyClientCallback extends Remote {
	
	public void sendNotification(int taskId) throws RemoteException;
}
