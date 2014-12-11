package vr.replica.server;


import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vr.replica.server.thrift.FailureResponse;
import vr.replica.server.thrift.RedirectResponse;
import vr.replica.server.thrift.ReplicaService;
import vr.replica.server.thrift.ResponseCode;
import vr.replica.server.thrift.ResponseStruct;
import vr.replica.server.thrift.ResponseUnion;
import vr.replica.server.thrift.SuccessResponse;
import vr.replica.server.vo.ClientRequest;
import vr.replica.server.vo.LogStatus;
import vr.replica.server.vo.ReplicaLog;
import vr.replica.server.vo.ReplicaState;
import vr.replica.server.vo.enums.ReplicaStatus;

public class ReplicaHandler implements ReplicaService.Iface{
	private static final Logger LOGGER = LoggerFactory.getLogger(ReplicaHandler.class);
	
	ReplicaState replicaState; 
	public ReplicaHandler() {
		super();
		replicaState =  ReplicaState.getInstance();
	}
	

	public ResponseStruct request(String message, String c, long r)
			throws TException {
		ResponseStruct response = new ResponseStruct();
		LOGGER.info("Request call");
		if(replicaState.getStatus() != ReplicaStatus.normal){
			response.setResponseCode(ResponseCode.failed);
			ResponseUnion responseUnion = new ResponseUnion();
			responseUnion.setFailureResponse(new FailureResponse().setRequestNumber(r).setResponseString("Failed as replica is not in normal state"));
			response.setResponseUnion(responseUnion);
			return response;
		}
		int currentViewNumber = replicaState.getViewNumber();
		response.setViewNumber(currentViewNumber);
		
		/*
		 * redirection logic
		 */
		if(!replicaState.isPrimaryReplica()){
			response.setResponseCode(ResponseCode.redirected);
			ResponseUnion responseUnion = new ResponseUnion();
			responseUnion.setRedirectResponse(new RedirectResponse().setPrimaryReplica(replicaState.getPrimaryReplica()));
			response.setResponseUnion(responseUnion);
			return response;
		}
		
		// 1. check max sequence number from this client 
		ClientRequest clientRequest = replicaState.getClientTable().get(c);
		
		if( clientRequest == null || r > clientRequest.getSequenceNumber()) {
			int opNumber 	=	replicaState.getNextOpNumber();
			ReplicaLog log 	= new ReplicaLog();
			log.setLogStatus( LogStatus.prepare );
			log.setMessage(	message );
			log.setOpNumber(opNumber);
			log.setViewNumber(currentViewNumber);
			
			if(clientRequest == null){
				clientRequest = new ClientRequest();
			}
			clientRequest.setClientId(c);
			clientRequest.setMessage(message);
			clientRequest.setOpNumber(opNumber);
			clientRequest.setViewNumber(currentViewNumber);
			clientRequest.setSequenceNumber(r);
			
			callPrepare(message, c, r);
			
			String responseString = "done";
			//TODO: responseString = application.execute("message");
			
			response.setResponseCode(ResponseCode.completed);
			ResponseUnion responseUnion = new ResponseUnion();
			responseUnion.setSuccessResponse(new SuccessResponse().setRequestNumber(r).setResponseString(responseString));
			response.setResponseUnion(responseUnion);
			return response;
		}else if( r < clientRequest.getSequenceNumber()){
			return  null;
			// TODO: return failed
		}else if( r == clientRequest.getSequenceNumber()){
			int opNumber = clientRequest.getOpNumber();
			int viewNumber =  clientRequest.getViewNumber();
			//TODO: check operation
			return null;
		}
		
		return null;
	}
	
	
	public void callPrepare(String message, String c, long r){
		Object[] qouroms = replicaState.getQouroms().toArray(); 
		for(int i = 0; i< qouroms.length;i++){
			if(i == replicaState.getReplicaNumber()){
				continue;
			}
			//TODO: call prepare for each of the replicas
			
		}
	}
	
	

}
