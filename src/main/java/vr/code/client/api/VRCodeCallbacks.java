package vr.code.client.api;

import java.util.concurrent.TimeoutException;

import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;

import vr.thrift.PrepareParameter;
import vr.thrift.PrepareResponse;
import vr.thrift.VRCodeService;
import vr.thrift.VRCodeService.AsyncClient.rpcPrepare_call;

public class VRCodeCallbacks {
	public AsyncMethodCallback<VRCodeService.AsyncClient.rpcPrepare_call> 
				prepareCallback(final PrepareParameter prepareParameter){
		return new AsyncMethodCallback<VRCodeService.AsyncClient.rpcPrepare_call>() {
			
			@Override
			public void onError(Exception arg0) {
				if(arg0 instanceof TimeoutException){ // Timeout occurred for the call
					System.out.print(prepareParameter);
				}
				
			}
			
			@Override
			public void onComplete(rpcPrepare_call arg0) {
				try {
					PrepareResponse prepareResponse = (PrepareResponse)arg0.getResult();
					if(prepareResponse.isPrepareOk()){
						
					}
				} catch (TException e) {
					e.printStackTrace();
				}
				
				
			}
		};
	}

}
