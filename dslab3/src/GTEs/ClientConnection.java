package GTEs;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import org.apache.log4j.Logger;

public class ClientConnection implements Runnable {
	
	private static final Logger LOG = Logger.getLogger(ClientConnection.class);

	private Socket sock;
	private String dir; //relief #2
	private EngineManager manager;
	private DataInputStream in = null;
	private DataOutputStream out = null;
	private ConnectionListener connectionListener;

	public ClientConnection(Socket socket, String taskDir, EngineManager engineManager, ConnectionListener connectionListener) {
		sock = socket;
		dir = taskDir;
		manager = engineManager;
		this.connectionListener = connectionListener;
		connectionListener.addClient(this);
	}

	@Override
	public void run() {
		int sleepTime = 0;
		try {
			LOG.debug("opening streams");
			
			in = new DataInputStream(sock.getInputStream());
			out = new DataOutputStream(sock.getOutputStream());
			
			LOG.debug("splitting command");
			
			// receive task command + taskData
			String[] cmd = in.readUTF().split(" ");
			if(cmd[0].equals("!executeTask")) {
				
				LOG.debug("execute task");
				for(int i = 0; i < cmd.length; i++) {
					System.out.println(cmd[i] + " , ");
				}
				
				//relief #2
				String effort = cmd[1];
				int load = 0;
				if(effort.equals("LOW")) {
					load = 33;
					sleepTime = 30000;
				} else if (effort.equals("MIDDLE")) {
					load = 66;
					sleepTime = 60000 * 3;
				} else if (effort.equals("HIGH")) {
					load = 100;
					sleepTime = 60000 * 5;
				}
				@SuppressWarnings("unused")
				
				String startScript = cmd[2] + " " + cmd[3] + " " + cmd[4];
				
				//TODO relief #1
				LOG.info("read file from client - not implemented, see relief #1");
				// read file from client
				LOG.info("finished reading file from client - not implemented, see relief #1");
				
//				out.writeUTF("Starting execution\n");
				LOG.info("!executeTask execution started");
				
				manager.addLoad(load);
				
				// TODO relief #2
				Process proc = Runtime.getRuntime().exec(startScript);
				
				BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
						
//				out.writeUTF("begin task xyz\n");
				Thread.sleep(sleepTime); // simulate execution
				
				
				out.writeUTF(reader.readLine());
				
				manager.removeLoad(load);
			} else if (cmd[0].equals("!currentLoad")) {
				out.writeInt(manager.getLoad());
			}
		} catch (IOException e) {
			
		}
		catch (InterruptedException e) {
		
		} finally {
			connectionListener.removeClient(this);
			
			//close all after finishing
			
			System.out.println("closing streams");

			try {
				out.close();
				in.close();
				sock.close();
			} catch (IOException e) { }
		}
	}

	public void terminate() {
		try {
			out.writeUTF("Engine shutting down");
			
			out.close();
			in.close();
			sock.close();
		} catch (IOException e) { }
	}

}
