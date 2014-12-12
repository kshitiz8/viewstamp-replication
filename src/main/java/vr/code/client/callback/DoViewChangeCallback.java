package vr.code.client.callback;

import org.apache.thrift.async.AsyncMethodCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vr.code.service.ReplicaState;
import vr.thrift.DoViewChangeParameter;
import vr.thrift.VRCodeService;
import vr.thrift.VRCodeService.AsyncClient.rpcDoViewChange_call;
/**
 * 
 * @author Kshitiz Tripathi
 *
 */
public class DoViewChangeCallback implements AsyncMethodCallback<VRCodeService.AsyncClient.rpcDoViewChange_call>{
	private static final Logger LOGGER = LoggerFactory.getLogger(DoViewChangeCallback.class);
	public static ReplicaState replicaState = ReplicaState.getInstance();
	public final DoViewChangeParameter doViewChangeParameter;
	
	public DoViewChangeCallback(DoViewChangeParameter doViewChangeParameter) {
		this.doViewChangeParameter = doViewChangeParameter;
	}
	@Override
	public void onComplete(rpcDoViewChange_call arg0) {

	}
	@Override
	public void onError(Exception arg0) {
	}
}
