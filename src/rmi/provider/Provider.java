package rmi.provider;

public class Provider {

	public static void main(String[] args) {

		TCPConnection Comm =TCPConnection.getInstance();
		
		ProviderSkeleton providerSkeleton = ProviderSkeleton.getInstance();
		
		RoboControl roboControl = RoboControl.getInstance();
		
		
		

	}

}
