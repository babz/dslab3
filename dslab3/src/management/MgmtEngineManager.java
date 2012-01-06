package management;

import java.io.IOException;

import remote.INotifyClientCallback;
import remote.ManagementException;

public class MgmtEngineManager {

	private static MgmtEngineManager instance;
	private MgmtTaskManager taskManager;

	private MgmtEngineManager() {
		taskManager = MgmtTaskManager.getInstance();
	}

	public static MgmtEngineManager getInstance() {
		if(instance == null) {
			instance = new MgmtEngineManager();
		}
		return instance;
	}

	/**
	 * requests and assigns a GTE to a task.
	 */
	public void requestEngine(int taskId) throws ManagementException {
		SchedulerConnectionManager scheduler;
		String assignedEngine;
		try {
			scheduler = SchedulerConnectionManager.getInstance();
			assignedEngine = scheduler.requestEngine(taskManager.getEffort(taskId));
		} catch (IOException e) {
			throw new ManagementException("unknown scheduler host - connection failed"); 
		}
		if (assignedEngine == null) {
			throw new ManagementException("Connection to scheduler failed.");
		}
		if (assignedEngine.startsWith("!engineRequestFailed")) {
			throw new ManagementException(
					"No engine available for execution. Please try again later.");
		}
		String[] engineDetails = assignedEngine.split(":");
		String address = engineDetails[0];
		int port = Integer.parseInt(engineDetails[1]);
		taskManager.assignEngine(taskId, address, port); 
	}

	public void executeTask(int taskId, INotifyClientCallback callback, String startScript) {
		ExecuteTaskRunnable taskExe = new ExecuteTaskRunnable(taskId, callback, startScript);
		new Thread(taskExe).start();
	}

}
