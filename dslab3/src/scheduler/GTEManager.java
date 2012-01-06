package scheduler;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.logging.Logger;

import GTEs.EngineIdentifier;

/**
 * manages the listener for the alive-packages from the engines and the
 * engine (energy) controller
 * 
 * @author babz
 * 
 */
public class GTEManager implements Runnable {
	private static Logger log = Logger.getLogger("class engine manager");

	private int min, max, timeout, checkPeriod;
	private DatagramSocket datagramSocket;
	private Hashtable<EngineIdentifier, GTEInfo> allEngines = new Hashtable<EngineIdentifier, GTEInfo>();

	private GTEAssigner assigner;

	private GTEController controller;

	private GTEListener listener;
	

	public GTEManager(int udpPort, int min, int max, int timeout,
			int checkPeriod) throws SocketException {
		datagramSocket = new DatagramSocket(udpPort);
		this.min = min;
		this.max = max;
		this.timeout = timeout;
		this.checkPeriod = checkPeriod;
	}

	@Override
	public void run() {
		log.info("manager started");

		// listener (precisely aliveMsgParser) fills 'allEngines'
		listener = new GTEListener(datagramSocket, allEngines);
		new Thread(listener).start();
		controller = new GTEController(datagramSocket, allEngines, min, max, timeout, checkPeriod);
		new Thread(controller).start();
		assigner = new GTEAssigner(allEngines);
	}
	
	public GTEAssigner getGTEAssigner() {
		return assigner;
	}
	
	public void terminate() {
		controller.terminate();
		listener.terminate();
		datagramSocket.close();
	}

	public String toString() {
		String engineList = "";
		for (Entry<EngineIdentifier, GTEInfo> engine : allEngines.entrySet()) {
			engineList += engine.getValue() + "\n";
		}
		return engineList;
	}
}
