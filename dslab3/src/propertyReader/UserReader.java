package propertyReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import management.UserInfo;

public class UserReader {
	//key: username
	private Map<String, UserInfo> user = new HashMap<String, UserInfo>();

	public UserReader() throws IOException {
		InputStream inputStream = ClassLoader.getSystemResourceAsStream("user.properties");
		if (inputStream != null) {
			Properties userProps = new Properties();
			userProps.load(inputStream);
			user = new HashMap<String, UserInfo>();

			for (String userKey : userProps.stringPropertyNames()) { // get all user names
				if(!userKey.contains(".")) {
					String pw = userProps.getProperty(userKey);
					user.put(userKey, new UserInfo(userKey, pw));
				}
			}

			for (String userKey : userProps.stringPropertyNames()) { // get all user names
				String[] key = userKey.split("\\.");
				if(userKey.contains("admin")) {
					String isAdmin = userProps.getProperty(userKey);
					user.get(key[0]).setAdmin(isAdmin);
				} else if (userKey.contains("credits")) {
					String credits = userProps.getProperty(userKey);
					user.get(key[0]).setCredits(Integer.parseInt(credits));
				}
			}
		} else {
			System.err.println("read user.properties failed.");
		}  
	}

	public Map<String, UserInfo> getAllUsers()  {
		return user;
	}


}
