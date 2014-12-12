package vr.code.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.Scanner;
import java.util.TreeMap;

import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vr.code.client.api.VRCodeCallbacks;
import vr.code.client.api.VRCodeClientAPI;
import vr.thrift.ClientRequest;
import vr.thrift.Log;
import vr.thrift.LogStatus;
import vr.thrift.PrepareParameter;
import vr.thrift.ReplicaStatus;
import vr.thrift.RequestResponseCode;
import vr.thrift.VRCodeService;
import vr.thrift.VRCodeService.AsyncIface;

public class VRCodeServer {
	private static final Logger LOGGER = LoggerFactory.getLogger(VRCodeServer.class);
	public static ReplicaState replicaState;
	public static VRCodeHandler handler;

	public static VRCodeService.AsyncProcessor<AsyncIface> processor;

	public static void main(String [] args) {

		beforeStart();

		final int port = Integer.parseInt(replicaState.getCurrentReplicaAddress().split(":")[1]);

		//		try {
		//			TNonblockingServerTransport serverTransport = new TNonblockingServerSocket(new InetSocketAddress("localhost", 9090));
		//			processor = new VRCodeService.AsyncProcessor<VRCodeService.AsyncIface> (new VRCodeHandler());
		//
		//			TServer server = new TNonblockingServer(new TNonblockingServer.Args(serverTransport).processor(processor));
		//			System.out.println("Starting server on port "+port);
		//			server.serve();
		//		} catch (TTransportException e) {
		//			e.printStackTrace();
		//		}

		try {
			TNonblockingServerTransport serverTransport = new TNonblockingServerSocket(port);
			VRCodeService.Processor processor = new VRCodeService.Processor(new VRCodeHandler());

			TServer server = new TNonblockingServer(new TNonblockingServer.Args(serverTransport).
					processor(processor));
			System.out.println("Starting server on port "+port);
			(new Thread() {
				public void run() {
					if(replicaState.getReplicaNumber() == replicaState.getPrimaryReplica()){
						processRequestQueue();
					}
				}
			}).start();
			server.serve();
		} catch (TTransportException e) {
			e.printStackTrace();
		}
	}
	public static void processRequestQueue(){
		LOGGER.info("Inside processRequestQueue");
		ClientRequest currentRequest = null;
		while(true){
			try {
				Thread.sleep(Constants.POLL_TIMEOUT);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			LOGGER.info("Awake");

			if(currentRequest != null){
				LOGGER.info("");
				Log log = replicaState.getLogs().get(replicaState.getOpNumber());
				int vote = 0;
				for(LogStatus status : log.getLogStatuses()){
					if(status == LogStatus.prepared){
						vote = vote +1;
					}
				}

				if(vote > (int)replicaState.getQouroms().size()/2 + 1){
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
			}else{
				LOGGER.info("No existing request, lets find a new one");
				if(!(replicaState.getRequestQueue() != null && replicaState.getRequestQueue().size()>0)){
					LOGGER.info("No new  request either, lets sleep for a while");
					continue;
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
					if(i == replicaState.getReplicaNumber()){
						continue;
					}
					PrepareParameter prepareParameter = new PrepareParameter();
					prepareParameter.setCommitNumber(replicaState.getCommitNumber());
					prepareParameter.setMessage(nextRequest.getRequestParameter());
					prepareParameter.setOpNumber(opNumber);
					prepareParameter.setViewNumber(replicaState.getViewNumber());
					prepareParameter.setReplicaNumber(i);
					VRCodeClientAPI.callPrepare(i, 
							Constants.PREPARE_TIMEOUT, 
							prepareParameter, 
							VRCodeCallbacks.prepareCallback(prepareParameter));
				}
				currentRequest = nextRequest;
			}
		}
	}

	public static void beforeStart(){
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
		ArrayList<String> qouroms  = new ArrayList<String>();
		System.out.println("please select a server to start. Type 1,2,3...");
		for(String server : prop.getProperty("server.names").split(",")){
			String address =prop.getProperty(server); 
			System.out.println("[ "+ i++ +" ] -\t" +server + " \t"+address);
			qouroms.add(address);

		}
		replicaState = ReplicaState.getInstance();
		replicaState.setStatus(ReplicaStatus.recovering);
		replicaState.setQouroms(qouroms);
		Scanner s = new Scanner(System.in);
		int choice = s.nextInt();
		if(choice <0 || choice >= qouroms.size()){
			return;
		}
		replicaState.setReplicaNumber(choice);
		replicaState.setClientTable(new HashMap<String, ClientRequest>());
		replicaState.setRequestQueue(new ArrayDeque<ClientRequest>());
		replicaState.setLogs(new TreeMap<Integer,Log>());

		//TODO: run recovery
		replicaState.setStatus(ReplicaStatus.normal);
	}

}