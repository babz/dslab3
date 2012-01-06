package management;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import propertyReader.UserReader;


/**
 * reads company properties-file and provides methods for companies
 * @author babz
 *
 */
public class UserManager {
	private static UserManager instance;
	private Map<String, UserInfo> allUsers; //map with names and passwords

	private UserManager() throws IOException {
		allUsers = new UserReader().getAllUsers();
	}

	public static synchronized UserManager getInstance() throws IOException {
		if(instance == null) {
			instance = new UserManager();
		}
		return instance;
	}

	public boolean login(String name, String pw) {
		if(!allUsers.containsKey(name)) {
			return false;
		} 
		return allUsers.get(name).loginIfPasswordCorrect(pw);
	}

	public UserInfo getUserInfo(String name) {
		return allUsers.get(name);
	}
	
	/**
	 * @param username 
	 * @return true if logout successful
	 */
	public boolean logout(String username) {
		UserInfo user = allUsers.get(username);
		if(user.isOnline()) {
			user.setOffline();
			return true;
		}
		return false;
	}
	
	//TODO check online status and amount of requested tasks per category

	public String getUsersAndTasks() {
		StringBuffer users = new StringBuffer();
		for(Entry<String, UserInfo> user: allUsers.entrySet()) {
			users.append(user.getValue());
			if(!user.getValue().isAdmin()) {
				users.append(MgmtTaskManager.getInstance().getTaskEffortCountByCompany(user.getKey()));
			}
			users.append("\n");
		}
		return users.toString();
	}


}
