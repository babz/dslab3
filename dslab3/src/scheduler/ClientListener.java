package scheduler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Logger;

import GTEs.EngineIdentifier;

/**
 * Server Socket; listens to client
 * own thread for each client
 * @author babz
 *
 */
public class ClientListener implements Runnable {
	private static Logger log = Logger.getLogger("client listener");
	private PrintWriter out;
	private BufferedReader in;
	private Socket clientSocket;
	private GTEAssigner gteAssigner;

	public ClientListener(Socket socket, GTEAssigner gteAssigner) throws IOException {
		clientSocket = socket;
		this.gteAssigner = gteAssigner;
		out = new PrintWriter(clientSocket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	}

	@Override
	public void run() {
		log.info("read stream from client");

		String input = null;
		try {
			while ((input = in.readLine()) != null) {
				EngineIdentifier engine = gteAssigner.getEngine(input);
				if (engine == null) {
					out.println("!engineRequestFailed");
				} else {
					out.println(engine.getAddress().getHostAddress() + ":" + engine.getPort());
				}
			} 
		} catch (IOException e) {
			// shutdown
		} finally {
			destroy();
		}
	}

	private void destroy() {
		out.close();
		try {
			in.close();
			clientSocket.close();
		} catch (IOException e) { }
	}
}