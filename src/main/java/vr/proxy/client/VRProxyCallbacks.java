package vr.proxy.client;

import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vr.thrift.RequestParameter;
import vr.thrift.RequestResponse;
import vr.thrift.RequestResponseCode;
import vr.thrift.VRCodeService;
import vr.thrift.VRCodeService.AsyncClient.rpcRequest_call;

public class VRProxyCallbacks {

	private static final Logger LOGGER = LoggerFactory.getLogger(VRProxyCallbacks.class);

	public static ProxyConfiguration pConf = ProxyConfiguration.getInstance();

	public static AsyncMethodCallback<VRCodeService.AsyncClient.rpcRequest_call> 
	prepareCallback(final RequestParameter requestParameter){
		
		return new AsyncMethodCallback<VRCodeService.AsyncClient.rpcRequest_call>() {
			public int getReplicaNumber(){
				return requestParameter.getReplicaNumber();
			}
			
			
			public void retry(int targetReplica){
				VRProxy.callRequest(targetReplica,
						VRProxy.REQUEST_TIMEOUT, 
						requestParameter, 
						VRProxyCallbacks.prepareCallback(requestParameter)
						);
			}
			
			@Override
			public void onError(Exception arg0) {
				LOGGER.info(requestParameter.toString());
				if(requestParameter.getRetryCount() >= VRProxy.REQUEST_RETRY_COUNT){
					LOGGER.error(getReplicaNumber()+"-Replica - RPCRequest - Max retry Count Reached",arg0);					
					pConf.setIdle(true);
				}else{
					LOGGER.error(getReplicaNumber()+"-Replica - RPCRequest - Failed, trying a different replica",arg0);
					int trial = requestParameter.getRetryCount() + 1;
					int targetReplica = (pConf.getPrimaryReplica()+trial)%pConf.getQouroms().size();
					requestParameter.setRetryCount(trial);
					requestParameter.setReplicaNumber(targetReplica);
				}
			}

			@Override
			public void onComplete(rpcRequest_call arg0) {
				try {
					
					RequestResponse requestResponse = (RequestResponse)arg0.getResult();
					LOGGER.info(requestResponse.toString());
					LOGGER.info(requestResponse.toString());
					
					if(requestResponse.getRequestResponseCode() == RequestResponseCode.accepted){
						if(requestParameter.getRetryCount() >= VRProxy.REQUEST_RETRY_COUNT){
							LOGGER.error(getReplicaNumber()+"-Replica - RPCRequest - Request failed after max retries in accepted state");
							pConf.setIdle(true);
							return;
						}
						LOGGER.info(requestResponse.getRequestUnion().getRequestAccept().getResponseString());
						try {
							Thread.sleep(VRProxy.REQUEST_WAIT);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						int retry = requestParameter.getRetryCount() + 1;
						requestParameter.setRetryCount(retry);
						VRProxy.callRequest(
								pConf.getPrimaryReplica(), 
								VRProxy.REQUEST_TIMEOUT,
								requestParameter, 
								VRProxyCallbacks.prepareCallback(requestParameter)
								);
					}else if(requestResponse.getRequestResponseCode() == RequestResponseCode.redirected){
						LOGGER.info("Redirected to replica"+requestResponse.getRequestUnion().getRequestRedirect().getPrimaryReplica());
						pConf.setPrimaryReplica(requestResponse.getRequestUnion().getRequestRedirect().getPrimaryReplica());
						pConf.setViewNumber(requestResponse.getViewNumber());
						VRProxy.callRequest(
								pConf.getPrimaryReplica(), 
								VRProxy.REQUEST_TIMEOUT,
								requestParameter, 
								VRProxyCallbacks.prepareCallback(requestParameter)
								);
					}else if(requestResponse.getRequestResponseCode() == RequestResponseCode.failed){
						LOGGER.info("Operation Failed "+requestResponse.getRequestUnion().getRequestFailure().getResponseString());
						LOGGER.error("Making proxy idle again");
						pConf.setIdle(true);
					}else if(requestResponse.getRequestResponseCode() == RequestResponseCode.completed){
						System.out.println("Operation: "+requestParameter.getOperation()+" --> Output = "+requestResponse.getRequestUnion().getRequestSuccess().getResponseString());
						System.out.print("quit to exit>");
						LOGGER.error("Making proxy idle again");
						pConf.setIdle(true);
					}
				} catch (TException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};
	}
}
