package vr.code.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.TreeMap;

import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vr.code.client.api.VRCodeClientAPI;
import vr.code.client.callback.PrepareCallback;
import vr.code.processes.RequestPoller;
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

		beforeStart(args[0]);

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
			
			new Poller().start();
			server.serve();
		} catch (TTransportException e) {
			e.printStackTrace();
		}
	}
	
	public static void beforeStart(String args){
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
///		System.out.println("please select a server to start. Type 0,1,2,3...");
		for(String server : prop.getProperty("server.names").split(",")){
			String address =prop.getProperty(server); 
			System.out.println(i++ + ") "+server + " \t"+address);
			qouroms.add(address);
			

		}
		replicaState = ReplicaState.getInstance();
		replicaState.setStatus(ReplicaStatus.recovering);
		replicaState.setQouroms(qouroms);
		
		replicaState.setReplicaNumber(Integer.parseInt(args));
		replicaState.setClientTable(new HashMap<String, ClientRequest>());
		replicaState.setRequestQueue(new ArrayDeque<ClientRequest>());
		replicaState.setLogs(new TreeMap<Integer,Log>());

		//TODO: run recovery
		replicaState.setStatus(ReplicaStatus.normal);
	}

}