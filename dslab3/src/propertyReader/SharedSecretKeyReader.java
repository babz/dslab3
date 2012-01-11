package propertyReader;

import general.Config;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;
import org.bouncycastle.util.encoders.Hex;

public class SharedSecretKeyReader {

	private static final Logger LOG = Logger.getLogger(SharedSecretKeyReader.class);

	private SecretKeySpec getKey(String owner, String company, String algorithm) throws IOException {
		LOG.info("read secret key");
		byte[] keyBytes = new byte[1024];
		//look up path to key in properties file
		String keyDir = new Config(owner).getString("keys.dir");
		String pathToSecretKey = keyDir + "/" + company + ".key";
		FileInputStream fis = new FileInputStream(pathToSecretKey);
		fis.read(keyBytes);
		fis.close();
		byte[] input = Hex.decode(keyBytes);
		// make sure to use the right ALGORITHM for what you want to do
		return new SecretKeySpec(input, algorithm);
	}

	//used by client and manager
	public byte[] createHash(String owner, String companyName) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
		String algorithm = "HmacSHA256";
		Key secretKey = this.getKey(owner, companyName, algorithm);
		// make sure to use the right ALGORITHM for what you want to do
		Mac hMac = Mac.getInstance(algorithm);
		hMac.init(secretKey);
		// MESSAGE is the message to sign in bytes
		hMac.update(secretKey.getEncoded());
		byte[] hash = hMac.doFinal();
		return hash;
	}

	public boolean verifyHash(byte[] computedHash, String receivedHash) {

		// computedHash is the HMAC of the received plaintext
		// byte[] computedHash = hMac.doFinal();

		// receivedHash is the HMAC that was sent by the communication partner
		byte[] received = receivedHash.getBytes();

		boolean validHash = MessageDigest.isEqual(computedHash, received);
		return validHash;
	}
}
