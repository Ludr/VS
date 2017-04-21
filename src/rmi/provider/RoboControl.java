package rmi.provider;

import org.cads.ev3.middleware.CaDSEV3RobotHAL;
import org.cads.ev3.rmi.generated.cadSRMIInterface.IIDLCaDSEV3RMIMoveGripper;
import org.cads.ev3.rmi.generated.cadSRMIInterface.IIDLCaDSEV3RMIMoveHorizontal;
import org.cads.ev3.rmi.generated.cadSRMIInterface.IIDLCaDSEV3RMIMoveVertical;
import org.cads.ev3.rmi.generated.cadSRMIInterface.IIDLCaDSEV3RMIUltraSonic;

public class RoboControl implements IIDLCaDSEV3RMIMoveGripper, IIDLCaDSEV3RMIMoveHorizontal, IIDLCaDSEV3RMIMoveVertical,
		IIDLCaDSEV3RMIUltraSonic {

	private static RoboControl instance;
	ProviderStub providerStub = ProviderStub.getInstance();
	
	private int currentHorizontalPercent = 0;
	private int targetHorizontalPercent = 0;
	
	private int currentVerticalPercent = 0;
	private int targetVerticalPercent = 0;

	public int getTargetVerticalPercent() {
		return targetVerticalPercent;
	}

	public void setTargetVerticalPercent(int targetVerticalPercent) {
		this.targetVerticalPercent = targetVerticalPercent;
	}

	public void setCurrentVerticalPercent(int currentVerticalPercent) {
		this.currentVerticalPercent = currentVerticalPercent;
	}

	private RoboControl() {

	}

	public static synchronized RoboControl getInstance() {
		if (RoboControl.instance == null) {
			RoboControl.instance = new RoboControl();
		}
		return RoboControl.instance;
	}

	@Override
	public int isUltraSonicOccupied() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getCurrentVerticalPercent() throws Exception {
		// TODO Auto-generated method stub
		return currentVerticalPercent;
	}

	@Override
	public int moveVerticalToPercent(int arg0, int arg1) throws Exception {
		targetVerticalPercent = arg1;

		if (currentVerticalPercent < targetVerticalPercent) {
			Thread moveThread = new Thread() {
				public void run() {
					CaDSEV3RobotHAL.getInstance().moveUp();
				}
			};
			moveThread.start();
		
		} else if (currentVerticalPercent > targetVerticalPercent) {
			Thread moveThread = new Thread() {
				public void run() {
					CaDSEV3RobotHAL.getInstance().moveDown();
				}
			};
			moveThread.start();
		}
		return 0;
	}

	@Override
	public int getCurrentHorizontalPercent() throws Exception {
		// TODO Auto-generated method stub
		return currentHorizontalPercent;
	}

	public int getTargetHorizontalPercent() {
		return targetHorizontalPercent;
	}

	public void setTargetHorizontalPercent(int targetHorizontalPercent) {
		this.targetHorizontalPercent = targetHorizontalPercent;
	}

	public void setCurrentHorizontalPercent(int currentHorizontalPercent) {
		this.currentHorizontalPercent = currentHorizontalPercent;
	}

	@Override
	public int moveHorizontalToPercent(int arg0, int arg1) throws Exception {
		targetHorizontalPercent = arg1;

		if (currentHorizontalPercent < targetHorizontalPercent) {
			Thread moveThread = new Thread() {
				public void run() {
					CaDSEV3RobotHAL.getInstance().moveLeft();
				}
			};
			moveThread.start();

			// Thread stopThread = new Thread() {
			// public void run() {
			// while (currentPercentHorizontal < targetPercentHorizontal) {
			// }
			//
			// CaDSEV3RobotHAL.getInstance().stop_h();
			// }
			// };
			// stopThread.start();
		} else if (currentHorizontalPercent > targetHorizontalPercent) {
			Thread moveThread = new Thread() {
				public void run() {
					CaDSEV3RobotHAL.getInstance().moveRight();
				}
			};
			moveThread.start();

			// Thread stopThread = new Thread() {
			// public void run() {
			// while (currentPercentHorizontal > targetPercentHorizontal) {
			// }
			// ;
			// CaDSEV3RobotHAL.getInstance().stop_h();
			// }
			// };
			// stopThread.start();
		}
		return 0;
	}

	@Override
	public int stop(int arg0) throws Exception {
		// TODO Auto-generated method stub
		CaDSEV3RobotHAL.getInstance().stop_h();
		return 0;
	}

	@Override
	public int closeGripper(int arg0) throws Exception {
		CaDSEV3RobotHAL.getInstance().doClose();
		return 0;
	}

	@Override
	public int isGripperClosed() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int openGripper(int arg0) throws Exception {
		CaDSEV3RobotHAL.getInstance().doOpen();
		return 0;
	}

}
