package rmi.consumer;

import org.cads.ev3.gui.swing.CaDSRobotGUISwing;
import org.cads.ev3.rmi.generated.cadSRMIInterface.IIDLCaDSEV3RMIMoveGripper;

public class Consumer {
	public static void main(String[] args) {
		// Start Gui and Stubs/Skeleton
		
		
		
		
		RobotController rc = new RobotController();
		
		CaDSRobotGUISwing gui = new CaDSRobotGUISwing(null, rc, rc, rc, rc);
		gui.startGUIRefresh(1000);
		
		
	}
}
