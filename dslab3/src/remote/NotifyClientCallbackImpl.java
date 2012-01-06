package remote;

public class NotifyClientCallbackImpl implements INotifyClientCallback {

	StringBuffer msg = new StringBuffer();

	@Override
	public void sendNotification(int taskId) {
		msg.append("Execution of task ");
		msg.append(taskId);
		msg.append(" finished");
		System.out.println(msg.toString());
	}
	
}
