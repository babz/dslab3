package scheduler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Hashtable;

import org.apache.log4j.Logger;

import GTEs.EngineIdentifier;

/**
 * listens to alivePackages and forwards them to the parser
 * @author babz
 *
 */
public class GTEListener implements Runnable {
//	private static Logger log = Logger.getLogger("class GTE listener");
	private static final Logger LOG = Logger.getLogger(GTEListener.class);

	private DatagramPacket packet;
	private DatagramSocket datagramSocket;
	private Hashtable<EngineIdentifier, GTEInfo> engines;

	private boolean alive = true;

	public GTEListener(DatagramSocket datagramSocket, Hashtable<EngineIdentifier, GTEInfo> engines) {
		int packetLength = 100;
		byte[] buf = new byte[packetLength];
		packet = new DatagramPacket(buf, buf.length);
		this.datagramSocket = datagramSocket;
		this.engines = engines;
	}

	@Override
	public void run() {
		LOG.info("run");
		while (alive ) {
			try {
				//receive packages and forward them
				datagramSocket.receive(packet);
				new Thread(new GTEAliveMsgParser(packet, engines)).start();
			} catch (IOException e) {
				// shutdown
			}

		}
	}
	
	public void terminate() {
		alive = false;
	}
}
