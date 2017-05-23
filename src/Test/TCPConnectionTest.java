package Test;

import rmi.provider.TCPConnection;
import rmi.provider.TCPConnection;


public class TCPConnectionTest {

	public static void main(String[] args) throws InterruptedException {
		
		
		TCPConnection connection = TCPConnection.getInstance();
		
		
		
		
		connection.getOutputQueueRegistry().put("Hallo vom Registry Port des Providers!");
		
		
		//System.out.println(connection.getIntputQueueRegistry().take());
		
		connection.getOutputQueueService().put("Hallo vom Service Port des Providers!");
		
		//System.out.println(connection.getIntputQueueService().take());
		

	}

}
