package vr.replica.server;

import vr.replica.server.vo.ReplicaState;

public class ReplicaStateManager {
	private ReplicaState replicaState  = null;
	
	public ReplicaState getReplicaState(){
		if(replicaState == null){
			replicaState = new ReplicaState();
		}
		return replicaState;
	}
	
	
	
	
	

}
