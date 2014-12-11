package vr.replica.server.vo;

public class ReplicaLog {
	int opNumber;
	int viewNumber;
	String message;
	LogStatus logStatus;
	
	public int getOpNumber() {
		return opNumber;
	}
	public void setOpNumber(int opNumber) {
		this.opNumber = opNumber;
	}
	public int getViewNumber() {
		return viewNumber;
	}
	public void setViewNumber(int viewNumber) {
		this.viewNumber = viewNumber;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public LogStatus getLogStatus() {
		return logStatus;
	}
	public void setLogStatus(LogStatus logStatus) {
		this.logStatus = logStatus;
	}
}
