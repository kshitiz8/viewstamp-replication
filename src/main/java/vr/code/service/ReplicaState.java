package vr.code.service;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import vr.thrift.ClientRequest;
import vr.thrift.Log;
import vr.thrift.LogStatus;
import vr.thrift.ReplicaStatus;
import vr.thrift.ViewChangePhase;


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
	int prevViewNumber = 0;
	ReplicaStatus status;
	int opNumber = 0;
	TreeMap<Integer,Log> logs;
	int commitNumber = -1;
	int checkPoint;
	HashMap<String, ClientRequest > clientTable;
	ArrayDeque<ClientRequest> requestQueue;
	long primaryLastTimestamp;
	ViewChangePhase viewChangePhase = ViewChangePhase.startView;
	List<Boolean> startViewChangeRequests = new ArrayList<Boolean>();
	List<Boolean> doViewChangeRequests = new ArrayList<Boolean>();
	TreeMap<Integer,Log> doViewChangeMaxLog ;
	
	
	public ViewChangePhase getViewChangePhase() {
		return viewChangePhase;
	}
	public void setViewChangePhase(ViewChangePhase viewChangePhase) {
		this.viewChangePhase = viewChangePhase;
	}
	public TreeMap<Integer, Log> getDoViewChangeMaxLog() {
		return doViewChangeMaxLog;
	}
	public void setDoViewChangeMaxLog(TreeMap<Integer, Log> doViewChangeMaxLog) {
		this.doViewChangeMaxLog = doViewChangeMaxLog;
	}
	public int getPrevViewNumber() {
		return prevViewNumber;
	}
	public void setPrevViewNumber(int prevViewNumber) {
		this.prevViewNumber = prevViewNumber;
	}
	
	public List<Boolean> getStartViewChangeRequests() {
		return startViewChangeRequests;
	}
	public void setStartViewChangeRequests(List<Boolean> startViewChangeRequests) {
		this.startViewChangeRequests = startViewChangeRequests;
	}
	public List<Boolean> getDoViewChangeRequests() {
		return doViewChangeRequests;
	}
	public void setDoViewChangeRequests(List<Boolean> doViewChangeRequests) {
		this.doViewChangeRequests = doViewChangeRequests;
	}
	public long getPrimaryLastTimestamp() {
		return primaryLastTimestamp;
	}
	public void setPrimaryLastTimestamp(long primaryLastTimestamp) {
		this.primaryLastTimestamp = primaryLastTimestamp;
	}
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
	public synchronized int getNextViewNumber(){
		if(status == ReplicaStatus.normal){
			prevViewNumber = viewNumber;
			status = ReplicaStatus.view_change;
		}
		viewNumber = viewNumber +1;
		return viewNumber;
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
	public void setLogStatus(int opNumber, int replicaNumber, LogStatus logStatus){
		this.logs.get(opNumber).getLogStatuses().set(replicaNumber, logStatus);
	}
	public void updatePrimaryLastTimestamp(){
		setPrimaryLastTimestamp(System.currentTimeMillis());
	}
	@Override
	public String toString() {
		return "ReplicaState [qouroms=" + qouroms + ", replicaNumber="
				+ replicaNumber + ", viewNumber=" + viewNumber
				+ ", prevViewNumber=" + prevViewNumber + ", status=" + status
				+ ", opNumber=" + opNumber + ", logs=" + logs
				+ ", commitNumber=" + commitNumber + ", checkPoint="
				+ checkPoint + ", clientTable=" + clientTable
				+ ", requestQueue=" + requestQueue + ", primaryLastTimestamp="
				+ primaryLastTimestamp + ", viewChangePhase=" + viewChangePhase
				+ ", startViewChangeRequests=" + startViewChangeRequests
				+ ", doViewChangeRequests=" + doViewChangeRequests
				+ ", doViewChangeMaxLog=" + doViewChangeMaxLog + "]";
	}
	
}