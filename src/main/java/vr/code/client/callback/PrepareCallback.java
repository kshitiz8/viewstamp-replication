package vr.code.client.callback;

import org.apache.thrift.TException;

import org.apache.thrift.async.AsyncMethodCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vr.code.client.api.VRCodeClientAPI;
import vr.code.service.Constants;
import vr.code.service.ReplicaState;
import vr.thrift.LogStatus;
import vr.thrift.PrepareParameter;
import vr.thrift.PrepareResponse;
import vr.thrift.VRCodeService;
import vr.thrift.VRCodeService.AsyncClient.rpcPrepare_call;
/**
 * 
 * @author Kshitiz Tripathi
 *
 */
public class PrepareCallback implements AsyncMethodCallback<VRCodeService.AsyncClient.rpcPrepare_call>{
	private static final Logger LOGGER = LoggerFactory.getLogger(PrepareCallback.class);
	public static ReplicaState replicaState = ReplicaState.getInstance();
	public final PrepareParameter prepareParameter;
	public PrepareCallback(PrepareParameter prepareParameter) {
		this.prepareParameter = prepareParameter;
	}

	public int getReplicaNumber(){
		return prepareParameter.getReplicaNumber();
	}
	
	public void retry(){
		if(prepareParameter.getRetryCount() <= Constants.PREPARE_MAX_RETRIES){
			VRCodeClientAPI.callPrepare(prepareParameter.getReplicaNumber(),Constants.PREPARE_TIMEOUT, 
					prepareParameter, new PrepareCallback(prepareParameter));
		}else{
			LOGGER.error(getReplicaNumber() +"-Replica : Prepare failed after max retries");
		}
	}

	@Override
	public void onComplete(rpcPrepare_call arg0) {
		try {
			PrepareResponse prepareResponse = (PrepareResponse)arg0.getResult();
			LOGGER.info(prepareParameter.toString());
			LOGGER.info(prepareResponse.toString());
			
			if(prepareResponse.isPrepareOk()){
				replicaState.setLogStatus(prepareParameter.getOpNumber(), 
						prepareParameter.getReplicaNumber(),
						LogStatus.prepared );
			}else{
				prepareParameter.setRetryCount(prepareParameter.getRetryCount() + 1);
				retry();
			}
			if(prepareResponse.isCommitOk() && prepareParameter.getCommitNumber() > -1){
				//TODO: BUG!!! only the commitNumber is set to committed, 
				// every uncommited entry below should also be set to committed. 
				replicaState.setLogStatus(prepareParameter.getCommitNumber(), 
						prepareParameter.getReplicaNumber(),
						LogStatus.commited );
			}
		} catch (TException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onError(Exception arg0) {
		LOGGER.error(prepareParameter.toString(),arg0);
		prepareParameter.setRetryCount(prepareParameter.getRetryCount() + 1);
		retry();

	}

}
