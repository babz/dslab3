package management;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import management.TaskInfo.EffortType;
import management.TaskInfo.StatusType;

public class MgmtTaskManager {

	private static int taskIdAssign = 0;
	private static MgmtTaskManager instance;

	// new tasks are only put if prepare is requested, so all tasks in the map
	// are at least prepared!
	private Map<Integer, TaskInfo> allTasks = new HashMap<Integer, TaskInfo>();

	private MgmtTaskManager() {
	}

	public static synchronized MgmtTaskManager getInstance() {
		if (instance == null) {
			instance = new MgmtTaskManager();
		}
		return instance;
	}

	/**
	 * prepares task for execution
	 * 
	 * @param taskName
	 *            name of task
	 * @param type
	 *            level of effort
	 * @param companyName
	 *            name of company that requests the task
	 * @return true if task was successfully prepared, false if invalid taskName
	 */
	public int prepareTask(String taskName, String type, String companyName) {

		if (!validType(type)) {
			return -1;
		}

		int newTaskId = ++taskIdAssign;
		allTasks.put(newTaskId, new TaskInfo(newTaskId, taskName, type,
				companyName, StatusType.PREPARED));
		return newTaskId;
	}

	public Set<Integer> getTaskIdsByCompany(String name) {
		Set<Integer> tasks = new TreeSet<Integer>();
		for (Entry<Integer, TaskInfo> task : allTasks.entrySet()) {
			if (name.equals(task.getValue().getCompanyName())) {
				tasks.add(task.getKey());
			}
		}
		return tasks;
	}

	public Set<Integer> getFinishedTasksByCompany(String companyName) {
		Set<Integer> tasks = getTaskIdsByCompany(companyName);
		Set<Integer> finishedTasks = new TreeSet<Integer>();
		
		for (int task : tasks) {
			TaskInfo t = allTasks.get(tasks);
			if(t != null) {
				if (t.getStatus() == StatusType.FINISHED) {
					finishedTasks.add(task);
				}
			}
		}
		return finishedTasks;
	}

	public String getTaskEffortCountByCompany(String name) {
		StringBuffer effortCounter = new StringBuffer();
		int lowTasks = 0;
		int middleTasks = 0;
		int highTasks = 0;
		Set<Integer> tasks = getFinishedTasksByCompany(name);
		for (int taskId : tasks) {
			EffortType taskEffort = allTasks.get(taskId).getEffortType();
			if (taskEffort == EffortType.LOW) {
				lowTasks++;
			} else if (taskEffort == EffortType.MIDDLE) {
				middleTasks++;
			} else {
				highTasks++;
			}
		}
		effortCounter.append(": LOW " + lowTasks + ", MIDDLE " + middleTasks
				+ ", HIGH " + highTasks);
		return effortCounter.toString();
	}

	private boolean validType(String type) {
		return type.equals("HIGH") || type.equals("LOW")
				|| type.equals("MIDDLE");
	}

	/**
	 * 
	 * @param id
	 * @return true if tasks exists
	 */
	public boolean taskExists(int id) {
		return allTasks.containsKey(id);
	}

	public String getEffort(int id) {
		return allTasks.get(id).getEffortType().toString();
	}

	public void assignEngine(int taskId, String address, int port) {
		allTasks.get(taskId).assignEngine(address, port);
	}

	public TaskInfo getTask(int taskId) {
		return allTasks.get(taskId);
	}

	/**
	 * checks if a company is the owner of a task
	 * 
	 * @param taskId
	 *            id of requested task
	 * @param companyName
	 *            company that requests the task
	 * @return true if company is owner of task
	 */
	public boolean checkTaskOwner(int taskId, String companyName) {
		if (companyName.equals(allTasks.get(taskId).getCompanyName())) {
			return true;
		}
		return false;
	}

	/**
	 * checks if a task has already been executed
	 * 
	 * @param taskId
	 *            id of requested task
	 * @return true if statusflag of task equals finished
	 */
	public boolean checkFinished(int taskId) {
		if (allTasks.get(taskId).getStatus() == StatusType.FINISHED) {
			return true;
		}
		return false;
	}

	/**
	 * checks the costs for the task considering the effort and the discount;
	 * discount is always auf die nächste int gerundet!
	 * @param taskId
	 *            task for which the costs are calculated
	 * @param company 
	 * @return cost of execution of task
	 */
	public int calculateCostsForTask(int taskId, String company) {
		int costs = allTasks.get(taskId).getCosts();
		int sumOfExecutedTasks = getTaskIdsByCompany(company).size();
		double discount = PricingCurve.getInstance().getDiscount(sumOfExecutedTasks);
		if(discount == 0) {
			return costs;
		}
		return (int) Math.ceil(costs - (costs / 100 * discount));
	}
}
