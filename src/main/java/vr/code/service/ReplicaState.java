package vr.code.service;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import vr.thrift.ClientRequest;
import vr.thrift.Log;
import vr.thrift.ReplicaStatus;


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
	TreeMap<Integer,Log> logs;
	int commitNumber = -1;
	int checkPoint;
	HashMap<String, ClientRequest > clientTable;
	ArrayDeque<ClientRequest> requestQueue;
	
	
	
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
	public TreeMap<Integer, Log> getLogs() {
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
	public ArrayDeque<ClientRequest> getRequestQueue() {
		return requestQueue;
	}
	
	
	public void setRequestQueue(ArrayDeque<ClientRequest> requestQueue) {
		this.requestQueue = requestQueue;
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
	public void setLogs(TreeMap<Integer, Log> logs) {
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
	
	public int majoity(){
		return (int)getQouroms().size()/2 + 1;
	}
	
}