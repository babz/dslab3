package management;


public class UserInfo {
	
	private enum StatusFlag {ONLINE, OFFLINE}
	
	private boolean isAdmin_;
	private String name_, pw_;
	private int credits_;
	private StatusFlag status;
	
	//TODO wenn lustig dann noch nach admin u company aufsplitten
	
	public UserInfo(String name, String pw) {
		name_ = name;
		pw_ = pw;
		status = StatusFlag.OFFLINE;
	}

	public void setAdmin(String admin) {
		if(admin.equals("false")) {
			isAdmin_ = false;
		} else {
			isAdmin_ = true;
		}
	}
	
	public void setCredits(int credits) {
		credits_ = credits;
	}

	public boolean isAdmin() {
		return isAdmin_;
	}

	public String getName() {
		return name_;
	}

	public String getPw() {
		return pw_;
	}

	public int getCredits() {
		return credits_;
	}
	
	public int increaseCredit(int c) {
		credits_ += c;
		return credits_;
	}
	
	public int decreaseCredit(int c) {
		credits_ -= c;
		return credits_;
	}

	public boolean isOnline() {
		return StatusFlag.ONLINE == status;
	}

	public void setOnline() {
		status = StatusFlag.ONLINE;
	}
	
	public void setOffline() {
		status = StatusFlag.OFFLINE;
	}

	public boolean loginIfPasswordCorrect(String password) {
		if(password.equals(pw_)) {
			setOnline();
			return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		StringBuffer userInfo = new StringBuffer();
		userInfo.append(name_ + " (" + status.toString().toLowerCase() + ")");
		return userInfo.toString();
	}
}
