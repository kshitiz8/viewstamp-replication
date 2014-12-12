package vr.code.service;


import java.util.List;

import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vr.thrift.ClientRequest;
import vr.thrift.CommitParameter;
import vr.thrift.CommitResponse;
import vr.thrift.DoViewChangeResponse;
import vr.thrift.GetStateResponse;
import vr.thrift.Log;
import vr.thrift.PrepareParameter;
import vr.thrift.PrepareResponse;
import vr.thrift.RecoveryResponse;
import vr.thrift.ReplicaStatus;
import vr.thrift.RequestAccept;
import vr.thrift.RequestFailure;
import vr.thrift.RequestParameter;
import vr.thrift.RequestRedirect;
import vr.thrift.RequestResponse;
import vr.thrift.RequestResponseCode;
import vr.thrift.RequestSuccess;
import vr.thrift.RequestUnion;
import vr.thrift.StartViewChangeResponse;
import vr.thrift.StartViewResponse;
import vr.thrift.VRCodeService;


public class VRCodeHandler implements  VRCodeService.Iface{
	private static final Logger LOGGER = LoggerFactory.getLogger(VRCodeHandler.class);

	public static ReplicaState replicaState = ReplicaState.getInstance();; 

	@Override
	public RequestResponse rpcRequest(RequestParameter requestParameter)
			throws TException {
		
		String operation = requestParameter.getOperation();
		String clientId = requestParameter.getClientId();
		long requestNumber = requestParameter.getRequestNumber();
		LOGGER.info("Request called by client "+clientId);
		RequestResponse response = new RequestResponse();
		/*
		 *  if the state of replica is not normal, return back
		 *  with failure response
		 */

		if(replicaState.getStatus() != ReplicaStatus.normal){
			response.setViewNumber(-1);
			response.setRequestResponseCode(RequestResponseCode.failed);
			RequestUnion requestUnion = new RequestUnion();
			requestUnion.setRequestFailure(
					new RequestFailure()
					.setRequestNumber(requestNumber)
					.setResponseString("Not in normal state"));
			response.setRequestUnion(requestUnion);
			return response;
		}

		response.setViewNumber(replicaState.getViewNumber());

		/*
		 * redirection logic if replica is not primary
		 */
		if(!replicaState.isPrimaryReplica()){
			response.setRequestResponseCode(RequestResponseCode.redirected);
			RequestUnion requestUnion = new RequestUnion();
			requestUnion.setRequestRedirect(new RequestRedirect().setPrimaryReplica(replicaState.getPrimaryReplica()));
			response.setRequestUnion(requestUnion);
			return response;
		}

		// 1. check max sequence number from this client 
		ClientRequest clientRequest = replicaState.getClientTable().get(clientId);
		
		if( clientRequest == null || requestNumber > clientRequest.getRequestParameter().getRequestNumber()) {
			
			if(clientRequest == null){
				clientRequest = new ClientRequest();
			}
			clientRequest.setRequestParameter(requestParameter);
			clientRequest.setViewNumber(replicaState.getViewNumber());
			clientRequest.setRequestResponseCode(RequestResponseCode.accepted);
			if(replicaState.getRequestQueue().size() <= Constants.MAX_QUEUE_SIZE){
				replicaState.getRequestQueue().push(clientRequest);
				replicaState.getClientTable().put(clientId, clientRequest);
				response.setRequestResponseCode(RequestResponseCode.accepted);
				RequestUnion requestUnion = new RequestUnion();
				requestUnion.setRequestAccept(new RequestAccept().setResponseString("Request is accepted"));
				response.setRequestUnion(requestUnion);
				return response;
			}else{
				response.setRequestResponseCode(RequestResponseCode.failed);
				RequestUnion requestUnion = new RequestUnion();
				requestUnion.setRequestFailure(new RequestFailure().setResponseString("Buffer overflow"));
				response.setRequestUnion(requestUnion);
				return  response;
			}
		}else if( requestNumber < clientRequest.getRequestParameter().getRequestNumber()){
			response.setRequestResponseCode(RequestResponseCode.failed);
			RequestUnion requestUnion = new RequestUnion();
			requestUnion.setRequestFailure(new RequestFailure().setResponseString("Old request. Ignored"));
			response.setRequestUnion(requestUnion);
			return  response;
		}else if( requestNumber == clientRequest.getRequestParameter().getRequestNumber()){
			int viewNumber =  clientRequest.getViewNumber();
			if(clientRequest.getRequestResponseCode() == RequestResponseCode.accepted && viewNumber == replicaState.getViewNumber()){
				response.setRequestResponseCode(RequestResponseCode.accepted);
				RequestUnion requestUnion = new RequestUnion();
				requestUnion.setRequestAccept(new RequestAccept().setResponseString("Request is in accepted state"));
				response.setRequestUnion(requestUnion);
				return response;
			}else if(clientRequest.getRequestResponseCode() == RequestResponseCode.completed){
				response.setRequestResponseCode(RequestResponseCode.completed);
				RequestUnion requestUnion = new RequestUnion();
				requestUnion.setRequestSuccess(new RequestSuccess().setResponseString(clientRequest.getOutput()));
				response.setRequestUnion(requestUnion);
				return  response;
			}else{
				response.setRequestResponseCode(RequestResponseCode.failed);
				RequestUnion requestUnion = new RequestUnion();
				requestUnion.setRequestFailure(new RequestFailure().setResponseString("Request has failed."));
				response.setRequestUnion(requestUnion);
				return  response;
			}
		}
		response.setRequestResponseCode(RequestResponseCode.failed);
		RequestUnion requestUnion = new RequestUnion();
		requestUnion.setRequestFailure(new RequestFailure().setResponseString("Failed. Unknown reason"));
		response.setRequestUnion(requestUnion);
		return  response;
	}

	//	public PrepareResponse rpcPrepare(int viewNumber, String message,
	//			int opNumber, int commitNumber) throws TException {
	@Override
	public PrepareResponse rpcPrepare(PrepareParameter prepareParameter)
			throws TException {
		
		PrepareResponse response = new PrepareResponse();
		
		int viewNumber = prepareParameter.getViewNumber();
		RequestParameter message = prepareParameter.getMessage();
		int opNumber = prepareParameter.getOpNumber();
		int commitNumber = prepareParameter.getCommitNumber();
		/*
		 * if view number from is less than view number  
		 */
		if(viewNumber < replicaState.getViewNumber()){
			response.setCommitOk(false);
			response.setPrepareOk(false);
			return response;
		}else if(viewNumber == replicaState.getViewNumber()){
//			if(opNumber == )
		}
		
		
		return new PrepareResponse();
	}

	@Override
	public CommitResponse rpcCommit(CommitParameter commitParameter)
			throws TException {
		int viewNumber = commitParameter.getViewNumber();
		int commitNumber = commitParameter.getCommitNumber();
		return null;
	}

	@Override
	public StartViewChangeResponse rpcStartViewChange(int newViewNumber,
			int replicaNumber) throws TException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DoViewChangeResponse rpcDoViewChange(int newViewNumber,
			List<Log> log, int oldViewNumber, int checkPoint, int opNumber,
			int commitNumber, int replicaNumber) throws TException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StartViewResponse rpcStartView(int viewNo, List<Log> log,
			int checkPoint, int opNumber, int commitNumber) throws TException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RecoveryResponse rpcRecovery(int replicaNumber, byte nonce)
			throws TException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GetStateResponse rpcGetState(int viewNumber, int opNumber,
			int replicaNumber) throws TException {
		// TODO Auto-generated method stub
		return null;
	}

}
