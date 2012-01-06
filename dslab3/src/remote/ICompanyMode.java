package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;


/**
 * remote interface handles all commands valid for companies
 * @author babz
 *
 */
public interface ICompanyMode extends Remote, IUser {

	/**
	 * get the credits of a company
	 * @return amount of credits the company owns
	 * @throws RemoteException
	 */
	int getCredit() throws RemoteException;
	
	/**
	 * buys the given amount of credits
	 * @param amount number of credits that a company wants to buy
	 * @return message
	 * @throws RemoteException thrown if amount is negative
	 * @throws ManagementException 
	 */
	int buyCredits(int amount) throws RemoteException, ManagementException;
	
	/**
	 * company credits are reduced for the preparation of the task
	 * @param taskName name of task
	 * @param taskType either "low", "middle" or "high"
	 * @return unique id for the task is returned; preparation successful
	 * @throws RemoteException thrown if company doesn't have enough credits and task is being ignored
	 * @throws ManagementException 
	 */
	int prepareTask(String taskName, String taskType) throws RemoteException, ManagementException;
	
	/**
	 * executes task by forwarding to an engine
	 * @param taskId unique id of task
	 * @param startScript "java -jar task<id>.jar"
	 * @throws RemoteException thrown if either there is no free engine for execution or the task belongs to another company
	 * @throws ManagementException 
	 */
	void executeTask(int taskId, String startScript, INotifyClientCallback cb) throws RemoteException, ManagementException;
	
	/**
	 * prints out information for the specified task
	 * @param taskId
	 * @return message
	 * @throws RemoteException thrown if task id unknown or task doesn't belong to the logged in company
	 * @throws ManagementException 
	 */
	String getInfo(int taskId) throws RemoteException, ManagementException;
	
	/**
	 * output of a finished task from the management component
	 * @param taskId id of task for which the output is required
	 * @return output printed if task belongs to company and task has been finished and paid;
	 * 					otherwise make the user pay first
	 * @throws RemoteException if not enough credit or task doesn't belong to company
	 * @throws ManagementException 
	 */
	String getOutput(int taskId) throws RemoteException, ManagementException;
}
