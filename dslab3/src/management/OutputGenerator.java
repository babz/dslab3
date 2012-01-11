package management;

import java.io.Serializable;

public class OutputGenerator implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String output;
	private byte[] compHash;

	public OutputGenerator(String taskOutput, byte[] computedHash) {
		output = taskOutput;
		compHash = computedHash;
	}
	
	public String getTaskOutput() {
		return output;
	}
	
	public byte[] getHash() {
		return compHash;
	}
	
}
