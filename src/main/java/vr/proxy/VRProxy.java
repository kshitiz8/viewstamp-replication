package vr.proxy;
import java.util.ArrayList;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import vr.code.vo.ProxyConfiguration;

public class VRProxy {

	public static int REQUEST_TIMEOUT = 2000;
	public static int REREQUEST_TIMEOUT = 1000; 
	public static int REQUEST_RETRY_COUNT = 3;

	private static ProxyConfiguration pConf = new ProxyConfiguration();


	public void init(){
		ArrayList<String> qouroms = new ArrayList<String>();
		//read the qourom info and other info from config file
		pConf.setProxyId(1);
		qouroms.add("localhost:9090");
		qouroms.add("localhost:9091");
		qouroms.add("localhost:9092");

		pConf.setQouroms(qouroms);
		pConf.setPrimaryReplica(0);
		pConf.setViewNumber(0);

		//pConf.setSerialNumber(0);

		//



	}
	public static void main(String [] args) {
		VRProxy proxy = new VRProxy();
		proxy.init();
//		try {
//			proxy.execute("1");
//		} catch (VRProxyException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

//	public String execute(String message) throws VRProxyException{
//		if(pConf.isIdle()){
//			pConf.setIdle(false);
//			pConf.setSerialNumber(System.currentTimeMillis());
//			for(int i = 0; i < REREQUEST_TIMEOUT; i++){
//				ResponseStruct response = request(message);
//				switch (response.getResponseCode()) {
//				case accepted: //TODO: put wait condition
//					break;
//				case redirected:
//					pConf.setPrimaryReplica(
//							response.getResponseUnion().
//							getRedirectResponse().getPrimaryReplica()
//							);
//					break;
//				case completed:
//					pConf.setIdle(true);
//					System.out.println(response.responseUnion.getSuccessResponse().getResponseString());
//					return response.responseUnion.getSuccessResponse().getResponseString();
//				case failed:	
//					pConf.setIdle(true);
//					throw new VRProxyException(
//							response.responseUnion.getFailureResponse().getResponseString());
//				default:
//					break;
//				}
//			}
//			throw new VRProxyException("Server Error");
//		}else {
//			throw new VRProxyException("Proxy in use");
//		}
//	}
//	
//	public ResponseStruct request(String m){
//		String connectionString[] = pConf.getQouroms().get(pConf.getPrimaryReplica()).split(":");
//		try {
//			TTransport transport;
//			transport = new TSocket(connectionString[0],
//					Integer.parseInt(connectionString[1]),
//					REQUEST_TIMEOUT);
//			transport.open();
//			TProtocol protocol = new  TBinaryProtocol(transport);
//			ReplicaService.Client client = new ReplicaService.Client(protocol);
//			ResponseStruct response = client.request(m, 
//					Integer.toString(pConf.getProxyId()), 
//					pConf.getSerialNumber()
//					);
//			transport.close();
//			return response;
//		} catch (TException x) {
//			x.printStackTrace();
//		}
//		return null;
//	}
}