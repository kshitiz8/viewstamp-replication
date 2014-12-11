package vr.code.service;


import java.util.List;

import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vr.code.vo.ClientRequest;
import vr.code.vo.ReplicaState;
import vr.code.vo.enums.ReplicaStatus;
import vr.thrift.CommitParameter;
import vr.thrift.CommitResponse;
import vr.thrift.DoViewChangeResponse;
import vr.thrift.GetStateResponse;
import vr.thrift.Log;
import vr.thrift.LogStatus;
import vr.thrift.PrepareParameter;
import vr.thrift.PrepareResponse;
import vr.thrift.RecoveryResponse;
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

	ReplicaState replicaState; 
	public VRCodeHandler() {
		super();
		replicaState =  ReplicaState.getInstance();
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

	@Override
	public RequestResponse rpcRequest(RequestParameter requestParameter)
			throws TException {
		String operation = requestParameter.getOperation();
		String clientId = requestParameter.getClientId();
		long requestNumber = requestParameter.getRequestNumber();
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

		if( clientRequest == null || requestNumber > clientRequest.getSequenceNumber()) {
			int opNumber 	=	replicaState.getNextOpNumber();
			Log log 	= new Log();
			log.setLogStatus( LogStatus.prepare);
			log.setOperation(operation);
			log.setOpNumber(opNumber);
			log.setViewNumber(replicaState.getViewNumber());

			if(clientRequest == null){
				clientRequest = new ClientRequest();
			}
			clientRequest.setClientId(clientId);
			clientRequest.setMessage(operation);
			clientRequest.setOpNumber(opNumber);
			clientRequest.setViewNumber(replicaState.getViewNumber());
			clientRequest.setSequenceNumber(requestNumber);



			callPrepare(operation, clientId, requestNumber);

			String responseString = "done";
			//TODO: responseString = application.execute("message");

			response.setRequestResponseCode(RequestResponseCode.completed);
			RequestUnion requestUnion = new RequestUnion();
			requestUnion.setRequestSuccess(new RequestSuccess().setRequestNumber(requestNumber).setResponseString(responseString));
			response.setRequestUnion(requestUnion);
			return response;
		}else if( requestNumber < clientRequest.getSequenceNumber()){
			return  response;
			// TODO: return failed
		}else if( requestNumber == clientRequest.getSequenceNumber()){
			int opNumber = clientRequest.getOpNumber();
			int viewNumber =  clientRequest.getViewNumber();
			//TODO: check operation

			return response;
		}
		return null;
	}

	//	public PrepareResponse rpcPrepare(int viewNumber, String message,
	//			int opNumber, int commitNumber) throws TException {
	@Override
	public PrepareResponse rpcPrepare(PrepareParameter prepareParameter)
			throws TException {
		int viewNumber = prepareParameter.getViewNumber();
		RequestParameter message = prepareParameter.getMessage();
		int opNumber = prepareParameter.getOpNumber();
		int commitNumber = prepareParameter.getCommitNumber();
		
		
		
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
