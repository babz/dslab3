package propertyReader;

import general.Config;

import java.io.FileInputStream;
import java.security.Key;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;
import org.bouncycastle.util.encoders.Hex;

public class SharedSecretKeyReader {

	private static final Logger LOG = Logger.getLogger(SharedSecretKeyReader.class);
	private Key key;

	public SharedSecretKeyReader(String owner, String companyName) {
		byte[] keyBytes = new byte[1024];
		//look up path to key in properties file
		String keyDir = new Config(owner).getString("keys.dir");
		String pathToSecretKey = keyDir + "/" + companyName + ".key";
		FileInputStream fis = new FileInputStream(pathToSecretKey);
		fis.read(keyBytes);
		fis.close();
		byte[] input = Hex.decode(keyBytes);
		// make sure to use the right ALGORITHM for what you want to do
		String algorithm = "HmacSHA256";
		key = new SecretKeySpec(input, algorithm); 
	}
	
	public Key getKey() {
		return key;
	}
}
