package GTEs;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Logger;

/**
 * socket that sends alive messages constantly
 * @author babz
 *
 */
public class AliveSignalEmitter implements Runnable {
	private static Logger log = Logger.getLogger("class GTE");

	private DatagramSocket datagramSocket;
	private int udpPort, tcpPort;
	private int minConsumption, maxConsumption;
	private boolean suspended;
	private long alivePeriod;
	private String message;
	private String schedulerHost;
	private boolean alive;

	public AliveSignalEmitter(DatagramSocket socket, int udpPort, int tcpPort, String schedulerHost,
			int alivePeriod, int minConsumption, int maxConsumption)
					throws SocketException {
		suspended = false;
		alive = true;
		this.alivePeriod = alivePeriod;
		this.udpPort = udpPort;
		this.tcpPort = tcpPort;
		this.schedulerHost = schedulerHost;
		this.minConsumption = minConsumption;
		this.maxConsumption = maxConsumption;
		datagramSocket = socket;
	}

	// send alive-packages
	@Override
	public void run() {
		log.info("sending alive packages");
		message = tcpPort + " " + minConsumption + " " + maxConsumption;
		while (alive) {
			int packetLength = 100;
			byte[] msg = new byte[packetLength];
			msg = message.getBytes();
			try {
				if(!suspended) {
					datagramSocket.send(new DatagramPacket(msg, msg.length,
							InetAddress.getByName(schedulerHost), udpPort));
				}
				Thread.sleep(alivePeriod);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace(); // TODO remove
				return;
			}
		}
	}

	public void stopEmitter() {
		suspended = true;
	}

	public void restart() {
		//TODO check restart of thread
		suspended = false;
	}

	public void terminate() {
		alive = false;
	}

}
