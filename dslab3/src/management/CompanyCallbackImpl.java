package management;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import management.TaskInfo.StatusType;
import remote.ICompanyMode;
import remote.INotifyClientCallback;
import remote.ManagementException;

public class CompanyCallbackImpl implements ICompanyMode {

	private UserInfo company;
	private int prepCosts;
	private MgmtTaskManager taskManager;

	public CompanyCallbackImpl(UserInfo companyInfo) {
		company = companyInfo;
		taskManager = MgmtTaskManager.getInstance();
		prepCosts = ManagementMain.getPreparationCosts();
	}

	@Override
	public void logout() throws RemoteException {
		// callback instantiated only after successful login
		company.setOffline();
		UnicastRemoteObject.unexportObject(this, false);
	}

	@Override
	public int getCredit() throws RemoteException {
		return company.getCredits();
	}

	@Override
	public int buyCredits(int amount) throws RemoteException,
	ManagementException {
		if (amount < 0) {
			throw new ManagementException("Invalid amount of credits.");
		}
		return company.increaseCredit(amount);
	}

	@Override
	public int prepareTask(String taskName, String taskType)
			throws RemoteException, ManagementException {
		if (company.getCredits() < prepCosts) {
			throw new ManagementException(
					"Not enough credits to prepare a task.");
		}
		int id = taskManager.prepareTask(taskName, taskType, company.getName());
		if(id < 0) {
			throw new ManagementException("invalid usage");
		}
		company.decreaseCredit(prepCosts);
		return id;
	}

	@Override
	public void executeTask(int taskId, String startScript, INotifyClientCallback callback)
			throws RemoteException, ManagementException {
		checkTaskExistanceAndOwner(taskId);
		if(taskManager.getTask(taskId).getStatus() == StatusType.EXECUTING) {
			throw new ManagementException("Execution has already been started");
		}
		MgmtEngineManager engineManager = MgmtEngineManager.getInstance();
		engineManager.requestEngine(taskId);
		engineManager.executeTask(taskId, callback, startScript);
		//INFO: engine manager notifies the client
	}

	@Override
	public String getInfo(int taskId) throws RemoteException,
	ManagementException {
		checkTaskExistanceAndOwner(taskId);
		return taskManager.getTask(taskId).getInfo();
	}

	@Override
	public String getOutput(int taskId) throws RemoteException,
	ManagementException {
		checkTaskExistanceAndOwner(taskId);
		if (!taskManager.checkFinished(taskId)) {
			throw new ManagementException("Task " + taskId
					+ " has not been finished yet.");
		}
		int costs = taskManager.calculateCostsForTask(taskId, company.getName());
		if (costs > company.getCredits()) {
			throw new ManagementException(
					"You do not have enough credits to pay this execution. (Costs: "
							+ costs
							+ " credits) Buy new credits for retrieving the output.");
		}
		company.decreaseCredit(costs);
		return taskManager.getTask(taskId).getOutput();
	}

	private void checkTaskExistanceAndOwner(int taskId) throws RemoteException,
	ManagementException {
		if (!taskManager.taskExists(taskId)) {
			throw new ManagementException("Task " + taskId + " doesn't exist.");
		}
		if (!taskManager.checkTaskOwner(taskId, company.getName())) {
			throw new ManagementException("Task "
					+ " does not belong to your company.");
		}
	}

	@Override
	public boolean isAdmin() throws RemoteException {
		return false;
	}

}
