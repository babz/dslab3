package scheduler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Map.Entry;

import GTEs.EngineIdentifier;

/**
 * handles client requests for engines
 * @author babz
 *
 */
public class GTEAssigner {

	private Hashtable<EngineIdentifier, GTEInfo> engines;
	private Hashtable<EngineIdentifier, GTEInfo> consideredEngines;

	public GTEAssigner(Hashtable<EngineIdentifier, GTEInfo> allEngines) {
		engines = allEngines;
	}

	/**
	 * chooses an engine for the required task
	 * @param taskEffort
	 * @return null if not enough capacity left
	 * @throws IOException 
	 */
	public EngineIdentifier getEngine(String taskEffort) throws IOException {
		int effort = getTaskEffortInInt(taskEffort);
		EngineIdentifier assignedEngine = null;
		consideredEngines = new Hashtable<EngineIdentifier, GTEInfo>();
		
		for (Entry<EngineIdentifier, GTEInfo> tmpEngine: engines.entrySet()) {
			// update load information
			Socket engineSock = new Socket(tmpEngine.getValue().getIpAddress(), tmpEngine.getValue().getTcpPort());
			DataInputStream in = new DataInputStream(engineSock.getInputStream());
			DataOutputStream out = new DataOutputStream(engineSock.getOutputStream());
			out.writeUTF("!currentLoad");
			tmpEngine.getValue().setLoad(in.readInt());
			
			out.close();
			in.close();
			engineSock.close();
			
			//check if enough capacity
			if((100 - tmpEngine.getValue().getLoad()) >= effort) {
				consideredEngines.put(tmpEngine.getKey(), tmpEngine.getValue());
			}
		}
		
		//check welche engine energietechnisch am besten geeignet - lineare interpolation
		double minExpectedCons = Double.MAX_VALUE;
		for (Entry<EngineIdentifier, GTEInfo> tmpEngine: consideredEngines.entrySet()) {
			/* expectedConsumption (using LINEAR interpolation)
			 expectedConsumption = 
			 	minCons + ((maxCons - minCons) * (currentLoad/100 + expectedAdditionalLoad/100))
			 */
			int minCons = tmpEngine.getValue().getMinConsumption();
			int maxCons = tmpEngine.getValue().getMaxConsumption();
			double load = (double) tmpEngine.getValue().getLoad();
			double expectedConsumption = minCons + ((maxCons - minCons) * (load/100 + ((double) effort)/100));
			
			if(expectedConsumption < minExpectedCons) {
				minExpectedCons = expectedConsumption;
				assignedEngine = tmpEngine.getKey();
			}
		}
		
		//update load information on assigned engine (estimated only...)
		if(assignedEngine != null) {
			engines.get(assignedEngine).addLoad(effort);
		}
		return assignedEngine;
	}

	private int getTaskEffortInInt(String taskEffort) {
		if(taskEffort.equals("LOW")) {
			return 33;
		} else if (taskEffort.equals("MIDDLE")) {
			return 66;
		} else if (taskEffort.equals("HIGH")) {
			return 100;
		} else {
			return (Integer) null;
		}
	}

}
