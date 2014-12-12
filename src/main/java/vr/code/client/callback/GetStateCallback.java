package vr.code.client.callback;

import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vr.code.client.api.VRCodeClientAPI;
import vr.code.service.Constants;
import vr.code.service.ReplicaState;
import vr.thrift.GetStateParameter;
import vr.thrift.GetStateResponse;
import vr.thrift.ReplicaStatus;
import vr.thrift.VRCodeService;
import vr.thrift.VRCodeService.AsyncClient.rpcGetState_call;

public class GetStateCallback implements AsyncMethodCallback<VRCodeService.AsyncClient.rpcGetState_call>{
	private static final Logger LOGGER = LoggerFactory.getLogger(GetStateCallback.class);
	public static ReplicaState replicaState = ReplicaState.getInstance();
	public final GetStateParameter getStateParameter;
	public GetStateCallback(GetStateParameter getStateParameter) {
		this.getStateParameter = getStateParameter;
	}

	public int getReplicaNumber(){
		return getStateParameter.getReplicaNumber();
	}
	
	public void retry(){
		if(getStateParameter.getRetryCount() <= Constants.GET_STATE_MAX_RETRIES){
			VRCodeClientAPI.callGetState(getStateParameter.getReplicaNumber(), 
					Constants.GET_STATE_TIMEOUT, getStateParameter, 
					new GetStateCallback(getStateParameter));
		}else{
			LOGGER.error(getReplicaNumber() +"-Replica : Get State failed after max retries");
		}
	}

	@Override
	public void onComplete(rpcGetState_call arg0) {
		try {
			GetStateResponse getStateResponse = (GetStateResponse)arg0.getResult();
			LOGGER.info(getStateParameter.toString());
			LOGGER.info(getStateResponse.toString());
			
			replicaState.getLogs().putAll(getStateResponse.getLogs());
			replicaState.setOpNumber(getStateResponse.getOpNumber());
			replicaState.setCommitNumber(getStateResponse.getCommitNumber());
			replicaState.setViewNumber(getStateResponse.getViewNumber());
			
			replicaState.setStatus(ReplicaStatus.normal);
		} catch (TException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onError(Exception arg0) {
		LOGGER.error(getStateParameter.toString(),arg0);
		getStateParameter.setRetryCount(getStateParameter.getRetryCount() + 1);
		
		int newTargetReplica = (getStateParameter.getReplicaNumber() +1)%replicaState.getQouroms().size();
		
		newTargetReplica = newTargetReplica == replicaState.getReplicaNumber() ? 
				(newTargetReplica +1)%replicaState.getQouroms().size() : newTargetReplica;
		getStateParameter.setReplicaNumber(newTargetReplica);
		retry();
	}

}
