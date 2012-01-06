package management;


/**
 * handles ONE task
 * @author babz
 *
 */
public class TaskInfo {

	public enum EffortType { LOW, MIDDLE, HIGH }
	public enum StatusType { PREPARED, EXECUTING, FINISHED }
	
	private int id;
	private long beginTime, endTime;
	private String name;
	private EffortType effort;
	private StatusType status;
	private int enginePort;
	private String engineAddress;
	private String companyName;
	private String output;

	public TaskInfo(int id, String taskName, String taskEffort, String companyName, StatusType statusType) {
		this.id = id; 
		name = taskName;
		this.setEffort(taskEffort);
		this.companyName = companyName;
		status = statusType;
		output = null;
	}

	public String getCompanyName() {
		return companyName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getId() {
		return id;
	}

	public EffortType getEffortType() {
		return effort;
	}
	
	public StatusType getStatus() {
		return status;
	}
	
	public void setStatus(StatusType statusType) {
		status = statusType;
		if(statusType == StatusType.EXECUTING) {
			beginTime = System.currentTimeMillis();
		} else if (statusType == StatusType.FINISHED) {
			endTime = System.currentTimeMillis();
		}
	}

	public void setEffort(String effortType) {
		if(effortType.equals("LOW")) {
			effort = EffortType.LOW;
		} else if (effortType.equals("MIDDLE")) {
			effort = EffortType.MIDDLE;
		} else if (effortType.equals("HIGH")) {
			effort = EffortType.HIGH;
		}
	}

	public void assignEngine(String address, int port) {
		engineAddress = address;
		enginePort = port;
	}

	public String getAssignedEngineAddress() {
		return engineAddress;
	}

	public int getAssignedEnginePort() {
		return enginePort;
	}
	
	public int getCosts() {
		int costsPerMinBegun = 10;
		long exeDuration = endTime - beginTime;
		return (int) Math.ceil(exeDuration/((double)60000)) * costsPerMinBegun;
	}
	
	public void setOutput(String outputFromEngine) {
		output = outputFromEngine;
	}
	
	public String getOutput() {
		return output;
	}
	
	public String getInfo() {
		String out = "Task " + id + " (" + name + ")";
		out += "\nType: " + effort.toString();
		if(engineAddress != null) {
			out += "\nAssigned Engine: " + engineAddress + ":" + enginePort;
		} else {
			out += "\nAssigned Engine: none";
		}
		out += "\nStatus: " + status.toString();
		if(status != StatusType.FINISHED) {
			out += "\nCosts: unknown";
		} else {
			out += "\nCosts: " + getCosts();
		}
		return out;
	}
}
