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

			@Override
			public void onError(Exception arg0) {
				if(requestParameter.getRetryCount() >= VRProxy.REQUEST_RETRY_COUNT){
					LOGGER.error("Request failed after max retries",arg0);
					LOGGER.error("Making proxy idle again");
					pConf.setIdle(true);
				}else{
					LOGGER.info("Request failed, lets try with another replica",arg0);
					int retry = requestParameter.getRetryCount() + 1;
					requestParameter.setRetryCount(retry);
					VRProxy.callRequest(
							(pConf.getPrimaryReplica()+retry)%pConf.getQouroms().size(), 
							VRProxy.REQUEST_TIMEOUT, 
							requestParameter, 
							VRProxyCallbacks.prepareCallback(requestParameter)
							);
				}
			}

			@Override
			public void onComplete(rpcRequest_call arg0) {
				try {
					RequestResponse requestResponse = (RequestResponse)arg0.getResult();
					if(requestResponse.getRequestResponseCode() == RequestResponseCode.accepted){
						if(requestParameter.getRetryCount() >= VRProxy.REQUEST_RETRY_COUNT){
							LOGGER.error("Request failed after max retries in accepted state");
							LOGGER.error("Making proxy idle again");
							pConf.setIdle(true);
							return;
						}
						LOGGER.info(requestResponse.getRequestUnion().getRequestAccept().getResponseString());
						LOGGER.info("Wait before retrying...");
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
					}else if(requestResponse.getRequestResponseCode() == RequestResponseCode.failed){
						LOGGER.info("OPeration Success. Output = "+requestResponse.getRequestUnion().getRequestSuccess().getResponseString());
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
