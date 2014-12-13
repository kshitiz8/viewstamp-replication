package vr.code.client.api;

import java.io.IOException;

import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.async.TAsyncClientManager;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TNonblockingSocket;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vr.code.client.callback.PrepareCallback;
import vr.code.service.ReplicaState;
import vr.code.service.VRCodeServer;
import vr.thrift.CommitParameter;
import vr.thrift.DoViewChangeParameter;
import vr.thrift.GetStateParameter;
import vr.thrift.PrepareParameter;
import vr.thrift.RecoveryParameter;
import vr.thrift.StartViewChangeParameter;
import vr.thrift.StartViewParameter;
import vr.thrift.VRCodeService;
import vr.thrift.VRCodeService.AsyncClient.ping_call;
import vr.thrift.VRCodeService.AsyncClient.rpcCommit_call;
import vr.thrift.VRCodeService.AsyncClient.rpcDoViewChange_call;
import vr.thrift.VRCodeService.AsyncClient.rpcGetState_call;
import vr.thrift.VRCodeService.AsyncClient.rpcPrepare_call;
import vr.thrift.VRCodeService.AsyncClient.rpcRecovery_call;
import vr.thrift.VRCodeService.AsyncClient.rpcStartViewChange_call;
import vr.thrift.VRCodeService.AsyncClient.rpcStartView_call;


public class VRCodeClientAPI {

	private static final Logger LOGGER = LoggerFactory.getLogger(VRCodeClientAPI.class);
	private static ReplicaState replicaState =ReplicaState.getInstance(); 

	private static String getAddress(int replicaNumber){
		return replicaState.getQouroms().get(replicaNumber).split(":")[0];
	}
	private static int getPort(int replicaNumber){
		return Integer.parseInt(replicaState.getQouroms().get(replicaNumber).split(":")[1]);
	}

	private static VRCodeService.AsyncClient getAsyncClient(int targetReplicaNumber, int timeout) throws IOException{
		VRCodeService.AsyncClient client  = new VRCodeService.AsyncClient(
				new TBinaryProtocol.Factory(), 
				new TAsyncClientManager(),
				new TNonblockingSocket(getAddress(targetReplicaNumber), getPort(targetReplicaNumber)));
		client.setTimeout(timeout);

		return client;
	}

	public static void callPrepare(int targetReplicaNumber, 
			int timeout,
			PrepareParameter prepareParameter, 
			AsyncMethodCallback<rpcPrepare_call> resultHandler){
		try {
			LOGGER.info("Client API: calling RPC prepare");
			VRCodeService.AsyncClient client = getAsyncClient(targetReplicaNumber, timeout);
			client.rpcPrepare(prepareParameter,resultHandler);
		}catch (TTransportException e) {
			LOGGER.error("Error",e);
		} catch (TException e) {
			LOGGER.error("Error",e);
		} catch (IOException e) {
			LOGGER.error("Error",e);
		}
	}

	public static void callCommit(int targetReplicaNumber, int timeout,CommitParameter commitParameter, 
			AsyncMethodCallback<rpcCommit_call> resultHandler){
		try {
			LOGGER.info("Client API: calling RPC Commit");
			VRCodeService.AsyncClient client = getAsyncClient(targetReplicaNumber, timeout);
			client.rpcCommit(commitParameter, resultHandler);
		}catch (TTransportException e) {
			LOGGER.error("Error",e);
		} catch (TException e) {
			LOGGER.error("Error",e);
		} catch (IOException e) {
			LOGGER.error("Error",e);
		}
	}

	public static void callStartViewChange(int targetReplicaNumber, int timeout, 
			StartViewChangeParameter startViewChangeParameter,
			AsyncMethodCallback<rpcStartViewChange_call> resultHandler){
		try {
			LOGGER.info("Client API: calling RPC StartViewChange");
			VRCodeService.AsyncClient client = getAsyncClient(targetReplicaNumber, timeout);
			client.rpcStartViewChange(startViewChangeParameter, resultHandler);
		}catch (TTransportException e) {
			LOGGER.error("Error",e);
		} catch (TException e) {
			LOGGER.error("Error",e);
		} catch (IOException e) {
			LOGGER.error("Error",e);
		}
	}

	public static  void callDoViewChange(int targetReplicaNumber, int timeout,
			DoViewChangeParameter doViewChangeParameter,
			AsyncMethodCallback<rpcDoViewChange_call> resultHandler){
		try {
			LOGGER.info("Client API: calling RPC DoViewChange");
			VRCodeService.AsyncClient client = getAsyncClient(targetReplicaNumber, timeout);
			client.setTimeout(timeout);
			client.rpcDoViewChange(doViewChangeParameter, resultHandler);
		}catch (TTransportException e) {
			LOGGER.error("Error",e);
		} catch (TException e) {
			LOGGER.error("Error",e);
		} catch (IOException e) {
			LOGGER.error("Error",e);
		}
	}

	public static void callStartView(int targetReplicaNumber, int timeout,
			StartViewParameter startViewParameter, 
			AsyncMethodCallback<rpcStartView_call> resultHandler){
		try {
			LOGGER.info("Client API: calling RPC StartView");
			VRCodeService.AsyncClient client = getAsyncClient(targetReplicaNumber, timeout);
			client.setTimeout(timeout);
			client.rpcStartView(startViewParameter, resultHandler);
		}catch (TTransportException e) {
			LOGGER.error("Error",e);
		} catch (TException e) {
			LOGGER.error("Error",e);
		} catch (IOException e) {
			LOGGER.error("Error",e);
		}
	}

	public static void callRecovery(int targetReplicaNumber, int timeout, 
			RecoveryParameter recoveryParameter, 
			AsyncMethodCallback<rpcRecovery_call> resultHandler){
		try {
			LOGGER.info("Client API: calling RPC Recovery");
			VRCodeService.AsyncClient client = getAsyncClient(targetReplicaNumber, timeout);
			client.setTimeout(timeout);
			client.rpcRecovery(recoveryParameter, resultHandler);
		}catch (TTransportException e) {
			LOGGER.error("Error",e);
		} catch (TException e) {
			LOGGER.error("Error",e);
		} catch (IOException e) {
			LOGGER.error("Error",e);
		}
	}

	public static void callGetState(int targetReplicaNumber, int timeout, 
			GetStateParameter getStateParameter, 
			AsyncMethodCallback<rpcGetState_call> resultHandler){
		try {
			LOGGER.info("Client API: calling RPC GetState");
			VRCodeService.AsyncClient client = getAsyncClient(targetReplicaNumber, timeout);
			client.setTimeout(timeout);
			client.rpcGetState(getStateParameter, resultHandler);
		}catch (TTransportException e) {
			LOGGER.error("Error",e);
		} catch (TException e) {
			LOGGER.error("Error",e);
		} catch (IOException e) {
			LOGGER.error("Error",e);
		}
	}
	public static void callPing(int targetReplicaNumber, int timeout, 

			AsyncMethodCallback<ping_call> resultHandler){
		try {
			//LOGGER.info("Client API: calling Ping");
			VRCodeService.AsyncClient client = getAsyncClient(targetReplicaNumber, timeout);
			client.setTimeout(timeout);
			client.ping(resultHandler);
		}catch (TTransportException e) {
			LOGGER.error("Error",e);
		} catch (TException e) {
			LOGGER.error("Error",e);
		} catch (IOException e) {
			LOGGER.error("Error",e);
		}
	}
	public static void main(String[] args) {

		VRCodeServer.beforeStart("0");

		VRCodeClientAPI client = new VRCodeClientAPI();
		PrepareCallback callback = new PrepareCallback(null);
		//PrepareParameter prepareParameter = new PrepareParameter(0,new RequestParameter("delete a", "1",1), 1,1,1,0);
		//client.callPrepare(0,300,prepareParameter, callbacks.prepareCallback(prepareParameter));
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
