package GTEs;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * manages datagram sockets: aliveSignalEmitter and SchedulerListener
 * @author babz
 *
 */
public class EngineManager implements Runnable {

	private int tcp;
	private int udp;
	private String host;
	private int alivePeriod;
	private int minConsumption;
	private int maxConsumption;
	private DatagramSocket datagramSocket;
	private SchedulerListener scheduleListener;
	private AliveSignalEmitter emitter;
	private String taskDir;
	private int load;
	private ConnectionListener connectionListener;

	public EngineManager(int udpPort, int tcpPort, String schedulerHost,
			int alivePeriod, int minConsumption, int maxConsumption, String taskDir) throws SocketException {
		udp = udpPort;
		datagramSocket = new DatagramSocket();
		tcp = tcpPort;
		host = schedulerHost;
		this.alivePeriod = alivePeriod;
		this.minConsumption = minConsumption;
		this.maxConsumption = maxConsumption;
		this.taskDir = taskDir;
		this.load = 0;
	}

	@Override
	public void run() {
		
		try {
			emitter = new AliveSignalEmitter(datagramSocket, udp, tcp,
					host, alivePeriod, minConsumption, maxConsumption);
			new Thread(emitter).start();
			scheduleListener = new SchedulerListener(datagramSocket, emitter);
			new Thread(scheduleListener).start();
			connectionListener = new ConnectionListener(tcp, taskDir, this);
			new Thread(connectionListener).start();
		} catch (SocketException e) {
			
		} catch (IOException e) {
		}
	}
	
	public int getLoad() {
		return load;
	}
	
	public synchronized void addLoad(int loadToAdd) {
		load += loadToAdd;
	}
	
	public synchronized void removeLoad(int loadToRemove) {
		load -= loadToRemove;
	}
	
	public void terminate() {
		connectionListener.terminate();
		scheduleListener.terminate();
		emitter.terminate();
		datagramSocket.close();
	}

}
