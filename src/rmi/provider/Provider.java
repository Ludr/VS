package rmi.provider;

import org.cads.ev3.middleware.CaDSEV3RobotHAL;
import org.cads.ev3.middleware.CaDSEV3RobotType;

public class Provider {

	public static void main(String[] args) {

		ProviderSkeleton providerSkeleton = ProviderSkeleton.getInstance();

		RoboControl roboControl = RoboControl.getInstance();

		StatusFeedbackListener stfbls = new StatusFeedbackListener();
		
		CaDSEV3RobotHAL HalInstance = CaDSEV3RobotHAL
				.createInstance(CaDSEV3RobotType.REAL, stfbls, stfbls);

		TCPConnection Comm = TCPConnection.getInstance();
	}

}
