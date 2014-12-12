package application.client;

import java.util.Scanner;

import vr.proxy.client.VRProxy;
import vr.proxy.client.VRProxyException;

public class AppClient {
	public static void main(String[] args) {
		VRProxy proxy = new VRProxy();
		Scanner scanner=new Scanner(System.in);
	    while (true) {
	        System.out.print("quit to exit> ");
	        String operation = scanner.nextLine();
	        if(operation.equals("quit")){
	            break;
	        }
	        try {
				proxy.execute(operation);
			} catch (VRProxyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}

}
