package vr.code.client.callback;

import org.apache.thrift.async.AsyncMethodCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vr.code.service.ReplicaState;
import vr.thrift.StartViewParameter;
import vr.thrift.VRCodeService;
import vr.thrift.VRCodeService.AsyncClient.rpcStartView_call;
/**
 * 
 * @author Kshitiz Tripathi
 *
 */
public class StartViewCallback implements AsyncMethodCallback<VRCodeService.AsyncClient.rpcStartView_call>{
	private static final Logger LOGGER = LoggerFactory.getLogger(StartViewCallback.class);
	public static ReplicaState replicaState = ReplicaState.getInstance();
	public final StartViewParameter startViewParameter;
	
	public StartViewCallback(StartViewParameter startViewParameter) {
		this.startViewParameter = startViewParameter;
	}
	@Override
	public void onComplete(rpcStartView_call arg0) {

	}
	@Override
	public void onError(Exception arg0) {
	}
}
