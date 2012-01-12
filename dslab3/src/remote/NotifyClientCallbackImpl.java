package remote;

public class NotifyClientCallbackImpl implements INotifyClientCallback {

	@Override
	public void sendNotification(int taskId) {
		StringBuffer msg = new StringBuffer();
		msg.append("Execution of task ");
		msg.append(taskId);
		msg.append(" finished");
		System.out.println(msg.toString());
	}
	
}
