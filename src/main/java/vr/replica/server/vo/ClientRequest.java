package vr.replica.server.vo;

public class ClientRequest {
	int opNumber;
	int viewNumber;
	String clientId;
	long sequenceNumber;
	String message;
	
	public int getOpNumber() {
		return opNumber;
	}
	public void setOpNumber(int opNumber) {
		this.opNumber = opNumber;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public long getSequenceNumber() {
		return sequenceNumber;
	}
	public void setSequenceNumber(long sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getViewNumber() {
		return viewNumber;
	}
	public void setViewNumber(int viewNumber) {
		this.viewNumber = viewNumber;
	}
}
