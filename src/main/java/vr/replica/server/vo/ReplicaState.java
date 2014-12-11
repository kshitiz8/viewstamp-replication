package vr.replica.server.vo;

import java.util.HashMap;
import java.util.List;

import vr.replica.server.vo.enums.ReplicaStatus;


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
	List<ReplicaLog> logs;
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
	public List<ReplicaLog> getLogs() {
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
	public void setQouroms(List<String> qouroms) {
		this.qouroms = qouroms;
	}
	public synchronized void setStatus(ReplicaStatus status) {
		this.status = status;
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
	public void setLogs(List<ReplicaLog> logs) {
		this.logs = logs;
	}
	
}