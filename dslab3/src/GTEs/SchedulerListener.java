package GTEs;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * listens to scheduler for incoming suspend/activate packages
 * @author babz
 *
 */
public class SchedulerListener implements Runnable {

	private DatagramSocket socket;
	private DatagramPacket packet;
	private AliveSignalEmitter emitter;
	private boolean alive;

	public SchedulerListener(DatagramSocket datagramSocket, AliveSignalEmitter emitter) {
		socket = datagramSocket;
		this.emitter = emitter;
		int packetLength = 100;
		byte[] buf = new byte[packetLength];
		packet = new DatagramPacket(buf, buf.length);
		alive = true;
	}

	@Override
	public void run() {
		while (alive) {
			try {
				//receive packages and forward them
				socket.receive(packet);
				unpack(packet);
			} catch (IOException e) {
				// shutdown
			}

		}
	}

	private void unpack(DatagramPacket packet) {
		String msg = new String(packet.getData(), 0, packet.getLength());

		//msg decode
		if(msg.equals("suspend")) {
			emitter.stopEmitter();
		} else if (msg.equals("activate")) {
			emitter.restart();
		} else {
			//TODO exception werfen: unknown command
		}

	}

	public void terminate() {
		alive = false;
	}
}
