package vr.code.vo;

import java.util.HashMap;
import java.util.List;

import vr.code.vo.enums.ReplicaStatus;
import vr.thrift.Log;


public class ReplicaState {
	private ReplicaState() {
		
	}	
	public static ReplicaState getInstance(){
		return ReplicaStateHolder.instance;
	}

	private static class ReplicaStateHolder {
		static final ReplicaState instance = new ReplicaState();
	}
	
	
	
	List<String> qouroms ;
	int replicaNumber;
	int viewNumber = 0;
	ReplicaStatus status;
	int opNumber = 0;
	List<Log> logs;
	int commitNumber;
	int checkPoint;
	HashMap<String, ClientRequest > clientTable;
	
	
	public List<String> getQouroms() {
		return qouroms;
	}
	public int getReplicaNumber() {
		return replicaNumber;
	}
	public int getViewNumber() {
		return viewNumber;
	}
	public ReplicaStatus getStatus() {
		return status;
	}
	public int getOpNumber() {
		return opNumber;
	}
	public List<Log> getLogs() {
		return logs;
	}
	public int getCommitNumber() {
		return commitNumber;
	}
	public int getCheckPoint() {
		return checkPoint;
	}
	public HashMap<String, ClientRequest> getClientTable() {
		return clientTable;
	}

	public void setQouroms(List<String> qouroms) {
		this.qouroms = qouroms;
	}
	public void setReplicaNumber(int replicaNumber) {
		this.replicaNumber = replicaNumber;
	}
	public String getReplicaAddress(int index){
		return qouroms.get(index);
	}
	public String getCurrentReplicaAddress(){
		return getReplicaAddress(replicaNumber);
	}
	public void setClientTable(HashMap<String, ClientRequest> clientTable) {
		this.clientTable = clientTable;
	}
	public void setLogs(List<Log> logs) {
		this.logs = logs;
	}
	public void setViewNumber(int viewNumber) {
		this.viewNumber = viewNumber;
	}
	public void setOpNumber(int opNumber) {
		this.opNumber = opNumber;
	}
	public void setCommitNumber(int commitNumber) {
		this.commitNumber = commitNumber;
	}
	public void setCheckPoint(int checkPoint) {
		this.checkPoint = checkPoint;
	}
	//-------
	public synchronized void setStatus(ReplicaStatus status) {
		this.status = status;
	}
	public synchronized int getNextOpNumber(){
		opNumber = opNumber +1;
		return opNumber;
	}
	public int getPrimaryReplica(){
		return viewNumber % qouroms.size();
	}
	public boolean isPrimaryReplica(){
		return getPrimaryReplica() == replicaNumber;
	}
	
}