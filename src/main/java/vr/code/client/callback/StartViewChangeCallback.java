package vr.code.client.callback;

import org.apache.thrift.async.AsyncMethodCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vr.code.service.ReplicaState;
import vr.thrift.StartViewChangeParameter;
import vr.thrift.VRCodeService;
import vr.thrift.VRCodeService.AsyncClient.rpcPrepare_call;
import vr.thrift.VRCodeService.AsyncClient.rpcStartViewChange_call;
/**
 * 
 * @author Kshitiz Tripathi
 *
 */
public class StartViewChangeCallback implements AsyncMethodCallback<VRCodeService.AsyncClient.rpcStartViewChange_call>{
	private static final Logger LOGGER = LoggerFactory.getLogger(StartViewChangeCallback.class);
	public static ReplicaState replicaState = ReplicaState.getInstance();
	public final StartViewChangeParameter startViewChangeParameter;
	
	public StartViewChangeCallback(StartViewChangeParameter startViewChangeParameter) {
		this.startViewChangeParameter = startViewChangeParameter;
	}
	@Override
	public void onComplete(rpcStartViewChange_call arg0) {

	}
	@Override
	public void onError(Exception arg0) {
	}
}
