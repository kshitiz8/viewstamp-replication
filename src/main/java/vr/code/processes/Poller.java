package vr.code.processes;

import java.util.ArrayList;

import org.apache.thrift.async.AsyncMethodCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vr.code.client.api.VRCodeClientAPI;
import vr.code.client.callback.DoViewChangeCallback;
import vr.code.client.callback.PrepareCallback;
import vr.code.client.callback.StartViewCallback;
import vr.code.client.callback.StartViewChangeCallback;
import vr.code.service.Constants;
import vr.code.service.ReplicaState;
import vr.code.service.VRCodeServer;
import vr.thrift.ClientRequest;
import vr.thrift.DoViewChangeParameter;
import vr.thrift.Log;
import vr.thrift.LogStatus;
import vr.thrift.PrepareParameter;
import vr.thrift.ReplicaStatus;
import vr.thrift.RequestResponseCode;
import vr.thrift.StartViewChangeParameter;
import vr.thrift.StartViewParameter;
import vr.thrift.ViewChangePhase;
import vr.thrift.VRCodeService.AsyncClient.ping_call;

public class Poller extends Thread{

	private static final Logger LOGGER = LoggerFactory.getLogger(VRCodeServer.class);
	public static ReplicaState replicaState = ReplicaState.getInstance();

	ClientRequest currentRequest = null;
	public void run() {
		int period = 0;
		while(true){
			
			//System.out.println(replicaState.toString());
			try {
				Thread.sleep(Constants.POLL_TIMEOUT);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if(replicaState.getStatus() == ReplicaStatus.normal 
					&&replicaState.getReplicaNumber() == replicaState.getPrimaryReplica()){
				if(currentRequest == null){
					monitorRequestQueue();
				}else{
					monitorLogStatus();
				}
				if(period++%100 == 0){
					period = 1;
					pingReplicas();
				}
			}else{
				monitorPrimary();
				if(replicaState.getViewChangePhase() == ViewChangePhase.startViewChange){
					monitorStartViewChangeRequests();}
				if(replicaState.getViewChangePhase() == ViewChangePhase.doViewChange){
					monitorDoViewChangeRequests();}
			}
		}
	}
	public void monitorLogStatus(){
		Log log = replicaState.getLogs().get(replicaState.getOpNumber());
		int vote = 1;
		for(LogStatus status : log.getLogStatuses()){
			if(status == LogStatus.prepared){
				vote = vote +1;
			}
		}
		/*
		 * TODO: BUG! if majority of replica's fail during prepare,
		 * the server hangs. Should ignore the request
		 */
		if(vote > replicaState.majoity()){
			LOGGER.info("Majority Condition reached :)");
			replicaState.getLogs().
			get(replicaState.getOpNumber()).
			getLogStatuses().
			set(replicaState.getReplicaNumber(), LogStatus.commited);

			currentRequest.setOutput("Output after running operation "+ currentRequest.getRequestParameter().getOperation());
			currentRequest.setRequestResponseCode(RequestResponseCode.completed);
			replicaState.getClientTable().put(currentRequest.getRequestParameter().getClientId(), new ClientRequest(currentRequest));
			currentRequest = null;
			replicaState.setCommitNumber(replicaState.getOpNumber());
		}
	}

	public void monitorRequestQueue(){
		if(!(replicaState.getRequestQueue() != null && replicaState.getRequestQueue().size()>0)){
			return;
		}
		ClientRequest nextRequest = replicaState.getRequestQueue().pop();
		int opNumber 	=	replicaState.getNextOpNumber();
		Log log 	= new Log();
		ArrayList<LogStatus> logStatuses = new ArrayList<LogStatus>();
		for(int i=0; i< replicaState.getQouroms().size(); i++){
			if(i == replicaState.getReplicaNumber())
				logStatuses.add(LogStatus.prepared);
			else{
				logStatuses.add(LogStatus.prepare);
			}
		}
		log.setLogStatuses( logStatuses);
		log.setOperation(nextRequest.getRequestParameter().getOperation());
		log.setOpNumber(opNumber);
		log.setViewNumber(replicaState.getViewNumber());
		replicaState.getLogs().put(opNumber, log);
		for(int i=0; i< replicaState.getQouroms().size(); i++){
			if(i == replicaState.getReplicaNumber()){continue;}
			PrepareParameter prepareParameter = new PrepareParameter();
			prepareParameter.setCommitNumber(replicaState.getCommitNumber());
			prepareParameter.setMessage(nextRequest.getRequestParameter());
			prepareParameter.setOpNumber(opNumber);
			prepareParameter.setViewNumber(replicaState.getViewNumber());
			prepareParameter.setReplicaNumber(i);
			VRCodeClientAPI.callPrepare(i, 
					Constants.PREPARE_TIMEOUT, 
					prepareParameter, 
					new PrepareCallback(prepareParameter));
		}
		currentRequest = nextRequest;

	}

	public void monitorPrimary(){
		long timeout = Constants.VIEW_CHANGE_TIMEOUT;
		if(replicaState.getStatus() != ReplicaStatus.normal){
			timeout = Constants.VIEW_CHANGE_TIMEOUT1;
		}

		if(System.currentTimeMillis() - replicaState.getPrimaryLastTimestamp() > timeout){
			replicaState.updatePrimaryLastTimestamp();
			replicaState.setViewChangePhase(ViewChangePhase.startViewChange);
			int newViewNumber = replicaState.getNextViewNumber();

			for(int i=0; i< replicaState.getQouroms().size(); i++){
				if(i == replicaState.getReplicaNumber()){
					replicaState.getStartViewChangeRequests().set(i, true);
					continue;
				}
				replicaState.getStartViewChangeRequests().set(i, false);
				StartViewChangeParameter startViewChangeParameter = new StartViewChangeParameter();
				startViewChangeParameter.setNewViewNumber(newViewNumber);
				startViewChangeParameter.setReplicaNumber(replicaState.getReplicaNumber());

				VRCodeClientAPI.callStartViewChange(i, 
						Constants.PREPARE_TIMEOUT, 
						startViewChangeParameter,
						new StartViewChangeCallback(startViewChangeParameter));
			}
		}
	}

	public void monitorStartViewChangeRequests(){
		int sum = 0;
		for(int i=0; i< replicaState.getQouroms().size(); i++){
			if(replicaState.getStartViewChangeRequests().get(i)){
				sum =sum+1;
			}
		}
		if(sum >= replicaState.majoity()){
			
			LOGGER.info("Got majority for StartViewChange");
			replicaState.updatePrimaryLastTimestamp();
			replicaState.setViewChangePhase(ViewChangePhase.doViewChange);
			if(replicaState.getPrimaryReplica() == replicaState.getReplicaNumber()){
				replicaState.getDoViewChangeRequests().set(replicaState.getReplicaNumber(),true);
			}else{
				DoViewChangeParameter doViewChangeParameter = new DoViewChangeParameter(replicaState.getViewNumber(), 
						replicaState.getLogs(), 
						replicaState.getPrevViewNumber(), 
						replicaState.getCheckPoint(), 
						replicaState.getOpNumber(), 
						replicaState.getCommitNumber(), 
						replicaState.getReplicaNumber(), 
						0);

				VRCodeClientAPI.callDoViewChange(replicaState.getPrimaryReplica(), Constants.DO_VIEW_CHANGE_TIMEOUT, 
						doViewChangeParameter, 
						new DoViewChangeCallback(doViewChangeParameter));
			}

		}
	}
	public void monitorDoViewChangeRequests(){
		int sum = 0;
		for(int i=0; i< replicaState.getQouroms().size(); i++){
			if(replicaState.getDoViewChangeRequests().get(i)){
				sum =sum+1;
			}
		}
		if(sum > replicaState.majoity()){
			replicaState.updatePrimaryLastTimestamp();
			replicaState.setStatus(ReplicaStatus.normal);
			replicaState.setViewChangePhase(ViewChangePhase.startView);;
			for(int i=0; i< replicaState.getQouroms().size(); i++){
				if(i == replicaState.getReplicaNumber()){continue;}
				StartViewParameter startViewParameter 
								= new StartViewParameter(replicaState.getViewNumber(),
														replicaState.getLogs(),
														replicaState.getCheckPoint(),
														replicaState.getOpNumber(),
														replicaState.getCommitNumber(),
														i,
														0);
				VRCodeClientAPI.callStartView(i, Constants.START_VIEW_TIMEOUT, startViewParameter, new StartViewCallback(startViewParameter));
			}
		}
	}
	
	
	
	public void pingReplicas(){
		for(int i=0; i< replicaState.getQouroms().size(); i++){
			if(i == replicaState.getReplicaNumber()){continue;}
			VRCodeClientAPI.callPing(i, 10, new AsyncMethodCallback<ping_call>() {
				
				@Override
				public void onError(Exception arg0) {
				}
				
				@Override
				public void onComplete(ping_call arg0) {
				}

			});
		}
	}

}


