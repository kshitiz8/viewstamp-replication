package vr.replica.server.vo;

import java.util.HashMap;

public class ReplicaLogs {
	public HashMap<Long,ReplicaLog> list = new HashMap<Long,ReplicaLog> ();
	
	
private ReplicaLogs() {
		
	}	
	public static ReplicaLogs getInstance(){
		return ReplicaLogsHolder.instance;
	}

	private static class ReplicaLogsHolder {
		static final ReplicaLogs instance = new ReplicaLogs();
	}
	
	
}
