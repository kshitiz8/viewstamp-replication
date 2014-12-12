package vr.proxy.client;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.async.TAsyncClientManager;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TNonblockingSocket;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vr.thrift.RequestParameter;
import vr.thrift.VRCodeService;
import vr.thrift.VRCodeService.AsyncClient.rpcRequest_call;

public class VRProxy {
	private static final Logger LOGGER = LoggerFactory.getLogger(VRProxy.class);
	public static int REQUEST_TIMEOUT = 500;
	public static int REQUEST_WAIT = 100;
	public static int REQUEST_RETRY_COUNT = 3;

	private static ProxyConfiguration pConf = ProxyConfiguration.getInstance();			


	public void init(){
		ArrayList<String> qouroms = new ArrayList<String>();
		//read the qourom info and other info from config file


		FileInputStream input = null;
		Properties prop = new Properties();
		try {
			input = new FileInputStream("src/main/resources/server.properties");
			prop.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		int i = 0;
		for(String server : prop.getProperty("server.names").split(",")){
			String address =prop.getProperty(server); 
			System.out.println("[ "+ i++ +" ] -\t" +server + " \t"+address);
			qouroms.add(address);
		}



		pConf.setProxyId("1");
		pConf.setQouroms(qouroms);
		pConf.setPrimaryReplica(0);
		pConf.setViewNumber(0);
		pConf.setRequestNumber(System.currentTimeMillis());


	}

	public void execute(String operation) throws VRProxyException{
		if(pConf.isIdle()){
			pConf.setIdle(false);
			pConf.setRequestNumber(System.currentTimeMillis());
			RequestParameter requestParameter = new RequestParameter();
			requestParameter.setClientId(pConf.getProxyId());
			requestParameter.setOperation(operation);
			requestParameter.setRequestNumber(pConf.getRequestNumber());
			callRequest(pConf.getPrimaryReplica(), REQUEST_TIMEOUT, requestParameter,VRProxyCallbacks.prepareCallback(requestParameter));
		}else {
			throw new VRProxyException("Proxy in use");
		}
	}



	private static String getAddress(int replicaNumber){
		return pConf.getQouroms().get(replicaNumber).split(":")[0];
	}
	private static int getPort(int replicaNumber){
		return Integer.parseInt(pConf.getQouroms().get(replicaNumber).split(":")[1]);
	}

	private static VRCodeService.AsyncClient getAsyncClient(int targetReplicaNumber, int timeout) throws IOException{
		LOGGER.info(getAddress(targetReplicaNumber)+" " +getPort(targetReplicaNumber));
		VRCodeService.AsyncClient client  = new VRCodeService.AsyncClient(
				new TBinaryProtocol.Factory(), 
				new TAsyncClientManager(),
				new TNonblockingSocket(getAddress(targetReplicaNumber), getPort(targetReplicaNumber)));
		client.setTimeout(timeout);

		return client;
	}

	public static void callRequest( int targetReplicaNumber, 
			int timeout,
			RequestParameter requestParameter, 
			AsyncMethodCallback<rpcRequest_call> resultHandler){
		try {
			LOGGER.info("Client API: calling RPC request");
			VRCodeService.AsyncClient client = getAsyncClient(targetReplicaNumber, timeout);
			client.rpcRequest(requestParameter,resultHandler);
		}catch (TTransportException e) {
			LOGGER.error("error",e);
		} catch (TException e) {
			LOGGER.error("error",e);
		} catch (IOException e) {
			LOGGER.error("error",e);
		}
	}


	public static void main(String [] args) {
		VRProxy proxy = new VRProxy();
		proxy.init();
		try {
			proxy.execute("create a");
		} catch (VRProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			Thread.sleep(100000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}