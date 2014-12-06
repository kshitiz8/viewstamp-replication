package vr.replica.server.vo;

import java.util.HashMap;
import java.util.List;

import vr.replica.server.vo.Operation;
import vr.replica.server.vo.ReplicaLog;
import vr.replica.server.vo.ReplicaStatus;


public class ReplicaState {
	List<String> qouroms ;
	int replicaNumber;
	int viewNumber;
	ReplicaStatus status;
	int opNumber;
	List<ReplicaLog> logs;
	int commitNumber;
	HashMap<String, Operation > clientTable;
}