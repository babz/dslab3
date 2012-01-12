package scheduler;

import general.Config;

import java.io.IOException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class SchedulerMain {

	// Define a static logger variable so that it references the
	// Logger instance named "MyApp".
	static Logger logger = Logger.getLogger(SchedulerMain.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// Set up a simple configuration that logs on the console.
		BasicConfigurator.configure();

		// TODO fehlerbehandlung für typen
		//params = int tcpPort, int udpPort, int min, int max, int timeout, int checkPeriod
		int noOfParams = 5;
		if(args.length != noOfParams) {
			System.out.println("Error: Too few arguments!");
			return;
		}

		int tcpPort = new Config("scheduler").getInt("tcp.port");
		int udpPort = Integer.parseInt(args[0]);
		int min = Integer.parseInt(args[1]); //min amt of used GTEs
		int max = Integer.parseInt(args[2]); //max amt of used GTEs
		int timeout = Integer.parseInt(args[3]);
		int checkPeriod = Integer.parseInt(args[4]);


		GTEManager engineManager = null;
		ClientConnectionManager clientManager = null;
		try {
			engineManager = new GTEManager(udpPort, min, max, timeout, checkPeriod);
			new Thread(engineManager).start();
			clientManager = new ClientConnectionManager(tcpPort, engineManager);
			new Thread(clientManager).start();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("connection from scheduler failed");
		}

		SchedulerInfoPoint commandReader;
		try {
			commandReader = new SchedulerInfoPoint(engineManager);
			commandReader.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// shutdown
			engineManager.terminate();
			clientManager.terminate();
		}
	}

}
