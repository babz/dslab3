package propertyReader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import org.apache.log4j.Logger;

public class RegistryReader {
	
	private static final Logger LOG = Logger.getLogger(RegistryReader.class);
	private HashMap<String, String> registries = new HashMap<String, String>();

	public RegistryReader() throws IOException {
		LOG.info("registry properties are being read");
		java.io.InputStream inputStream = ClassLoader.getSystemResourceAsStream("registry.properties");
		if (inputStream != null) {
			Properties registryProps = new Properties();
			registryProps.load(inputStream);
			for (String registryKey : registryProps.stringPropertyNames()) { // get keys
				String registryValue = registryProps.getProperty(registryKey); // get values
				registries.put(registryKey, registryValue);
			}
		} else {
			//TODO company.properties could not be found
			LOG.error("read registry properties failed");
			System.err.println("read registry properties failed.");
		} 
	}
	
	public String getRegistryHost() {
		return registries.get("registry.host");
	}
	
	public int getRegistryPort() {
		return Integer.parseInt(registries.get("registry.port"));
	}
}
