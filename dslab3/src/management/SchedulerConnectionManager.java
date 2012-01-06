package management;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Logger;

/**
 * manages sockets: writes to server, listens to answer
 * @author babz
 *
 */
public class SchedulerConnectionManager {

	private static Logger log = Logger.getLogger("open scheduler connection");

	private static SchedulerConnectionManager instance;
	private String host;
	private int tcpPort;

	private SchedulerConnectionManager() throws UnknownHostException, IOException {
		host = ManagementMain.getSchedulerHost();
		tcpPort = ManagementMain.getSchedulerTCPPort();
	}

	public static synchronized SchedulerConnectionManager getInstance() throws UnknownHostException, IOException {
		if(instance == null) {
			instance = new SchedulerConnectionManager();
		}
		return instance;
	}

	public String requestEngine(String taskEffort) throws NumberFormatException, IOException {
		Socket socket = new Socket(host, tcpPort); 
		PrintWriter serverWriter = new PrintWriter(socket.getOutputStream(), true);
		BufferedReader schedulerIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		
		log.info("forward request to scheduler");
		serverWriter.println(taskEffort);
		
		log.info("catch answer from scheduler");
		String answer = schedulerIn.readLine();

		//close everything
		try {
			serverWriter.close();
			schedulerIn.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			socket.close();
		}
		return answer;
	}
}
