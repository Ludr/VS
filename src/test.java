import org.cads.ev3.gui.swing.CaDSRobotGUISwing;
import org.cads.ev3.middleware.CaDSEV3RobotStudentImplementation;
import org.cads.ev3.middleware.CaDSEV3RobotType;
import org.cads.ev3.middleware.hal.ICaDSEV3RobotFeedBackListener;
import org.cads.ev3.middleware.hal.ICaDSEV3RobotStatusListener;
import org.cads.ev3.rmi.generated.cadSRMIInterface.IIDLCaDSEV3RMIMoveGripper;
import org.cads.ev3.rmi.generated.cadSRMIInterface.IIDLCaDSEV3RMIMoveHorizontal;

public class test {

	public static CaDSRobotGUISwing gui;
	
	public static void main(String[] args) throws Exception {
		IIDLCaDSEV3RMIMoveHorizontal hor = new testHorizontalMovement();
		IIDLCaDSEV3RMIMoveGripper gripper = new Gripper();
		gui = new CaDSRobotGUISwing(null, gripper, null, hor, null);
		gui.startGUIRefresh(1000);
		
		
		
		gripper.isGripperClosed();
		
		ICaDSEV3RobotFeedBackListener feed = new Feedbacklistener();
		ICaDSEV3RobotStatusListener status = new StatusListener();
		
		CaDSEV3RobotStudentImplementation impl = CaDSEV3RobotStudentImplementation.createInstance(CaDSEV3RobotType.SIMULATION, status, feed);
		impl.moveUp();
//		Thread.sleep(2000);
		impl.stop_h();
		impl.stop_v();
	}
}
