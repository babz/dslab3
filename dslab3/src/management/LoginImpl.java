package management;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import remote.ILogin;
import remote.IUser;
import remote.ManagementException;


/**
 * Implements the Remote-Interface and implements login for one user
 * @author babz
 *
 */
public class LoginImpl implements ILogin {

	private static final Logger LOG = Logger.getLogger(LoginImpl.class);
	
	private UserManager cManager = null;
	private Set<IUser> allUsers = new HashSet<IUser>();

	public LoginImpl() throws IOException {
		cManager = UserManager.getInstance();
	}

	@Override
	public IUser login(String userName, String pw) throws ManagementException, RemoteException {
		LOG.info("login user");
		if(cManager.login(userName, pw)) {
			IUser user = null;
			if(cManager.getUserInfo(userName).isAdmin()) {
				//Admin Mode
				user = new AdminCallbackImpl(cManager.getUserInfo(userName));
				LOG.info("user is admin");
			} else {
				//Company Mode
				user = new CompanyCallbackImpl(cManager.getUserInfo(userName));
				LOG.info("user is company");
			}
			UnicastRemoteObject.exportObject(user, 0);
			allUsers.add(user);
			return user;
		} else {
			LOG.error("login failed");
			throw new ManagementException("login failed");
		}
	}

	public void logoutAll() throws RemoteException {
		LOG.info("all users are logged out");
		for(IUser u : allUsers) {
			u.logout();
		}
	}

}
