package GTEs;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ConnectionListener implements Runnable {

	private ServerSocket socket;
	private String taskDir;
	private EngineManager engineManager;
	private List<ClientConnection> clients = Collections.synchronizedList(new LinkedList<ClientConnection>());

	public ConnectionListener(int tcp, String taskDir, EngineManager engineManager) throws IOException {
		socket = new ServerSocket(tcp);
		this.taskDir = taskDir;
		this.engineManager = engineManager;
	}

	@Override
	public void run() {
		try {
			while(true) {
				ClientConnection connection = new ClientConnection(socket.accept(), taskDir, engineManager, this);
				new Thread(connection).start();
			}
		} catch (IOException e) { }
	}
	
	public void addClient(ClientConnection handler) {
		clients.add(handler);
	}
	
	public void removeClient(ClientConnection handler) {
		clients.remove(handler);
	}

	public void terminate() {
		synchronized (clients) {
			for(ClientConnection c : clients) {
				c.terminate();
			}
		}
		try {
			socket.close();
		} catch (IOException e) { }
	}

}
