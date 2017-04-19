package rmi.provider;

import org.cads.ev3.middleware.CaDSEV3RobotStudentImplementation;
import org.cads.ev3.middleware.CaDSEV3RobotType;
import org.cads.ev3.middleware.hal.ICaDSEV3RobotFeedBackListener;
import org.cads.ev3.middleware.hal.ICaDSEV3RobotStatusListener;

public class Provider {

	public static void main(String[] args) {

		ProviderSkeleton providerSkeleton = ProviderSkeleton.getInstance();

		RoboControl roboControl = RoboControl.getInstance();

		ICaDSEV3RobotFeedBackListener feed = new Feedbacklistener();

		CaDSEV3RobotStudentImplementation HalInstance = CaDSEV3RobotStudentImplementation
				.createInstance(CaDSEV3RobotType.SIMULATION, roboControl, feed);

		TCPConnection Comm = TCPConnection.getInstance();
	}

}
