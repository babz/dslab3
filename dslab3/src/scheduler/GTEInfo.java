package scheduler;

import java.net.InetAddress;

/**
 * contains information about one engine including the online-status-flag
 * @author babz
 *
 */
public class GTEInfo {

	private long currTime;
	private InetAddress ip;
	private int tcpPort, udp, minConsumption, maxConsumption, load;
	private StatusFlag status;
	
	public GTEInfo(InetAddress ip, int udpPort, int tcpPort, int minConsumption, int maxConsumption) {
		updateTime();
		this.ip = ip;
		this.udp = udpPort;
		this.tcpPort = tcpPort;
		this.minConsumption = minConsumption;
		this.maxConsumption = maxConsumption;
		status = StatusFlag.ONLINE;
	}
	
	private enum StatusFlag {SUSPENDED, ONLINE, OFFLINE}
	
	public long getTime() {
		return currTime;
	}
	
	public void updateEngine(int udpPort) {
		updateTime();
		udp = udpPort;
	}
	
	public void updateTime() {
		currTime = System.currentTimeMillis();
	}
	
	public boolean isActive() {
		return StatusFlag.ONLINE == status; 
	}
	
	public boolean isSuspended() {
		return StatusFlag.SUSPENDED == status;
	}
	
	public void suspendGTE() {
		status = StatusFlag.SUSPENDED;
	}
	
	public boolean isOffline() {
		return StatusFlag.OFFLINE == status;
	}
	
	public void setOffline() {
		status = StatusFlag.OFFLINE;
	}
	
	public void setActive() {
		status = StatusFlag.ONLINE;
	}
	
	public int getLoad() {
		return load;
	}
	
	public void setLoad(int newLoad) {
		load = newLoad;
	}
	
	public void addLoad(int effort) {
		load += effort;	
	}

	public int getTcpPort() {
		return tcpPort;
	}
	
	public int getMinConsumption() {
		return minConsumption;
	}
	
	public int getMaxConsumption() {
		return maxConsumption;
	}
	
	public InetAddress getIpAddress() {
		return ip;
	}
	
	public int getUdpPort() {
		return udp;
	}

	@Override
	public String toString() {
		return "IP:" + ip.getHostAddress() + ", TCP:" + tcpPort + ", UDP: " + udp + ", " 
				+ status.toString().toLowerCase() + ", Energy Signature: min " 
				+ minConsumption + "W, max " + maxConsumption + "W, " 
				+ ", Load: " + load + "%";
	}

}
