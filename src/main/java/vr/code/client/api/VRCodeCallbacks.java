package vr.code.client.api;

import java.util.concurrent.TimeoutException;

import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vr.code.service.Constants;
import vr.code.service.ReplicaState;
import vr.code.service.VRCodeHandler;
import vr.thrift.LogStatus;
import vr.thrift.PrepareParameter;
import vr.thrift.PrepareResponse;
import vr.thrift.VRCodeService;
import vr.thrift.VRCodeService.AsyncClient.rpcPrepare_call;

public class VRCodeCallbacks {
	private static final Logger LOGGER = LoggerFactory.getLogger(VRCodeCallbacks.class);
	
	public static ReplicaState replicaState = ReplicaState.getInstance();
	
	public static AsyncMethodCallback<VRCodeService.AsyncClient.rpcPrepare_call> 
				prepareCallback(final PrepareParameter prepareParameter){
		return new AsyncMethodCallback<VRCodeService.AsyncClient.rpcPrepare_call>() {
			
			@Override
			public void onError(Exception arg0) {
				if(prepareParameter.getRetryCount() >= Constants.PREPARE_MAX_RETRIES){
						LOGGER.error("Prepare failed after max retries for replica "+ prepareParameter.getReplicaNumber(),arg0);
				}else{
					prepareParameter.setRetryCount(prepareParameter.getRetryCount() + 1);
					VRCodeClientAPI.callPrepare(prepareParameter.getReplicaNumber(),Constants.PREPARE_TIMEOUT, 
							prepareParameter, VRCodeCallbacks.prepareCallback(prepareParameter));
				}
			}
			
			@Override
			public void onComplete(rpcPrepare_call arg0) {
				try {
					PrepareResponse prepareResponse = (PrepareResponse)arg0.getResult();
					
					replicaState.getLogs().
						get(prepareParameter.getOpNumber()).
							getLogStatuses().
								set(prepareParameter.getReplicaNumber(),
										prepareResponse.isPrepareOk() ? LogStatus.prepared: LogStatus.failed);
					
					if(prepareResponse.isCommitOk() && prepareParameter.getCommitNumber() != -1){
						replicaState.getLogs().
						get(prepareParameter.getCommitNumber()).
							getLogStatuses().
								set(prepareParameter.getReplicaNumber(),LogStatus.commited);
					}
				} catch (TException e) {
					e.printStackTrace();
				}
			}
		};
	}

}
