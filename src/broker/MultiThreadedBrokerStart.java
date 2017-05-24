package broker;

public class MultiThreadedBrokerStart {

	public static void main(String[] args) {
		
		
		MultiThreadBroker serverRegistry = new MultiThreadBroker(8889);
		MultiThreadBroker serverService= new MultiThreadBroker(8888);
		
		
		new Thread(serverRegistry).start();
		new Thread(serverService).start();

		
		
		
	}

}
