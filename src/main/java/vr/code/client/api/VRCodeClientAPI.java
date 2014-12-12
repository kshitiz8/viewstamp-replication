package vr.code.client.api;

import java.io.IOException;
import java.util.List;

import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.async.TAsyncClientManager;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TNonblockingSocket;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vr.code.service.ReplicaState;
import vr.code.service.VRCodeServer;
import vr.thrift.CommitParameter;
import vr.thrift.Log;
import vr.thrift.PrepareParameter;
import vr.thrift.RequestParameter;
import vr.thrift.VRCodeService;
import vr.thrift.VRCodeService.AsyncClient.rpcCommit_call;
import vr.thrift.VRCodeService.AsyncClient.rpcDoViewChange_call;
import vr.thrift.VRCodeService.AsyncClient.rpcGetState_call;
import vr.thrift.VRCodeService.AsyncClient.rpcPrepare_call;
import vr.thrift.VRCodeService.AsyncClient.rpcRecovery_call;
import vr.thrift.VRCodeService.AsyncClient.rpcRequest_call;
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
	
	public static void callStartViewChange(int targetReplicaNumber, int timeout, int newViewNumber,
			int replicaNumber, AsyncMethodCallback<rpcStartViewChange_call> resultHandler){
		try {
			LOGGER.info("Client API: calling RPC StartViewChange");
			VRCodeService.AsyncClient client = getAsyncClient(targetReplicaNumber, timeout);
			client.rpcStartViewChange(newViewNumber, replicaNumber, resultHandler);
		}catch (TTransportException e) {
			LOGGER.error("Error",e);
        } catch (TException e) {
        	LOGGER.error("Error",e);
        } catch (IOException e) {
            LOGGER.error("Error",e);
        }
	}
	
	public static  void callDoViewChange(int targetReplicaNumber, int timeout, int newViewNumber,
			List<Log> log, int oldViewNumber, int checkPoint, int opNumber,
			int commitNumber, int replicaNumber, AsyncMethodCallback<rpcDoViewChange_call> resultHandler){
		try {
			LOGGER.info("Client API: calling RPC DoViewChange");
			VRCodeService.AsyncClient client = getAsyncClient(targetReplicaNumber, timeout);
			client.setTimeout(timeout);
			client.rpcDoViewChange(newViewNumber, log, oldViewNumber, checkPoint, opNumber, commitNumber, replicaNumber, resultHandler);
		}catch (TTransportException e) {
			LOGGER.error("Error",e);
        } catch (TException e) {
        	LOGGER.error("Error",e);
        } catch (IOException e) {
            LOGGER.error("Error",e);
        }
	}
	
	public static void callStartView(int targetReplicaNumber, int timeout,int viewNo, List<Log> log,
			int checkPoint, int opNumber, int commitNumber, AsyncMethodCallback<rpcStartView_call> resultHandler){
		try {
			LOGGER.info("Client API: calling RPC StartView");
			VRCodeService.AsyncClient client = getAsyncClient(targetReplicaNumber, timeout);
			client.setTimeout(timeout);
			client.rpcStartView(viewNo, log, checkPoint, opNumber, commitNumber, resultHandler);
		}catch (TTransportException e) {
			LOGGER.error("Error",e);
        } catch (TException e) {
        	LOGGER.error("Error",e);
        } catch (IOException e) {
            LOGGER.error("Error",e);
        }
	}
	
	public static void callRecovery(int targetReplicaNumber, int timeout, int replicaNumber, byte nonce, 
			AsyncMethodCallback<rpcRecovery_call> resultHandler){
		try {
			LOGGER.info("Client API: calling RPC Recovery");
			VRCodeService.AsyncClient client = getAsyncClient(targetReplicaNumber, timeout);
			client.setTimeout(timeout);
			client.rpcRecovery(replicaNumber, nonce, resultHandler);
		}catch (TTransportException e) {
			LOGGER.error("Error",e);
        } catch (TException e) {
        	LOGGER.error("Error",e);
        } catch (IOException e) {
            LOGGER.error("Error",e);
        }
	}
	
	public static void callGetState(int targetReplicaNumber, int timeout, int viewNumber, int opNumber,
			int replicaNumber, AsyncMethodCallback<rpcGetState_call> resultHandler){
		try {
			LOGGER.info("Client API: calling RPC GetState");
			VRCodeService.AsyncClient client = getAsyncClient(targetReplicaNumber, timeout);
			client.setTimeout(timeout);
			client.rpcGetState(viewNumber, opNumber, replicaNumber, resultHandler);
		}catch (TTransportException e) {
			LOGGER.error("Error",e);
        } catch (TException e) {
        	LOGGER.error("Error",e);
        } catch (IOException e) {
            LOGGER.error("Error",e);
        }
	}
	public static void main(String[] args) {
		
		VRCodeServer.beforeStart();
		
		VRCodeClientAPI client = new VRCodeClientAPI();
		VRCodeCallbacks callbacks = new VRCodeCallbacks();
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
