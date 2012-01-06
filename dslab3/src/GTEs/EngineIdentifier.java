package GTEs;

import java.net.InetAddress;

public class EngineIdentifier {
	private InetAddress address;
	private int port;
	
	public EngineIdentifier(InetAddress addr, int tcpPort) {
		address = addr;
		this.port = tcpPort;
	}
	
	public InetAddress getAddress() {
		return address;
	}
	
	public int getPort() {
		return port;
	}

	@Override
	public String toString() {
		return "IP:" + address + ", TCP:" + port;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + port;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EngineIdentifier other = (EngineIdentifier) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (port != other.port)
			return false;
		return true;
	}
}
