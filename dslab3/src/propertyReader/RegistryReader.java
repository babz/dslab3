package propertyReader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public class RegistryReader {
	
	private HashMap<String, String> registries = new HashMap<String, String>();

	public RegistryReader() throws IOException {
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
