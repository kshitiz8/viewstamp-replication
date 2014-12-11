package vr.replica.server;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.Scanner;

import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

import vr.replica.server.thrift.ReplicaService;
import vr.replica.server.vo.ClientRequest;
import vr.replica.server.vo.ReplicaLog;
import vr.replica.server.vo.ReplicaState;
import vr.replica.server.vo.enums.ReplicaStatus;

public class ReplicaServer {

	public static ReplicaState replicaState;
	public static ReplicaHandler handler;

	public static ReplicaService.Processor processor;

	public static void main(String [] args) {
		
		serverStart();
		
		final int port = Integer.parseInt(replicaState.getCurrentReplicaAddress().split(":")[1]);
		try {
			handler = new ReplicaHandler();
			processor = new ReplicaService.Processor(handler);

			Runnable simple = new Runnable() {
				public void run() {
					
					simple(processor,port);
				}
			};      

			new Thread(simple).start();
		} catch (Exception x) {
			x.printStackTrace();
		}
	}

	public static void simple(ReplicaService.Processor processor, int port) {
		try {
			TServerTransport serverTransport = new TServerSocket(port);
			TServer server = new TSimpleServer(new Args(serverTransport).processor(processor));

			System.out.println("Starting the simple server at "+port);
			server.serve();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void serverStart(){
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
		replicaState.setLogs(new ArrayList<ReplicaLog>());

		//TODO: run recovery
		replicaState.setStatus(ReplicaStatus.normal);
	}

}