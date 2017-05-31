package rmi.provider;

import java.net.UnknownHostException;

import org.cads.ev3.middleware.CaDSEV3RobotHAL;

public class Provider {

	public static void main(String[] args) throws UnknownHostException {
		// SessionControl.createInstance(args[0],args[1],args[2]);
		SessionControl.createInstance("localhost", "SIMULATION", "robot1");
		SessionControl session = SessionControl.getInstance();

		rmi.generated.ProviderSkeleton providerSkeleton = rmi.generated.ProviderSkeleton.getInstance();
		RoboControl roboControl = RoboControl.getInstance();
		StatusFeedbackListener stfbls = new StatusFeedbackListener();
		CaDSEV3RobotHAL HalInstance = CaDSEV3RobotHAL.createInstance(session.getSessionType(), stfbls, stfbls);
		TCPConnection Comm = TCPConnection.getInstance();

		RegisterService registry = new RegisterService();
		registry.registerAtBroker(session.robotName);
	}

}
