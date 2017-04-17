package rmi.consumer;

import org.cads.ev3.gui.swing.CaDSRobotGUISwing;
import org.cads.ev3.rmi.generated.cadSRMIInterface.IIDLCaDSEV3RMIMoveGripper;

public class Consumer {
	public static void main(String[] args) {
		// Start Gui and Stubs/Skeleton
		
		
		
		
		IIDLCaDSEV3RMIMoveGripper gripper = new MoveGripperStub();
		
		CaDSRobotGUISwing gui = new CaDSRobotGUISwing(null, gripper, null, null, null);
		gui.startGUIRefresh(1000);
		
		
	}
}
