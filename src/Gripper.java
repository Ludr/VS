import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.cads.ev3.middleware.CaDSEV3RobotStudentImplementation;
import org.cads.ev3.rmi.generated.cadSRMIInterface.IIDLCaDSEV3RMIMoveGripper;

public class Gripper implements IIDLCaDSEV3RMIMoveGripper {

	
	
	@Override
	public int closeGripper(int arg0) throws Exception {
		// TODO Auto-generated method stub
		CaDSEV3RobotStudentImplementation.getInstance().doClose();
		return 0;
	}

	@Override
	public int isGripperClosed() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int openGripper(int arg0) throws Exception {
		// TODO Auto-generated method stub
		CaDSEV3RobotStudentImplementation.getInstance().doOpen();
		return 0;
	}

}
