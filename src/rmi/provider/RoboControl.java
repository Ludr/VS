package rmi.provider;

import org.cads.ev3.middleware.CaDSEV3RobotHAL;

public class RoboControl {

	private static RoboControl instance;
	rmi.generated.ProviderStub providerStub = rmi.generated.ProviderStub.getInstance();

	private int currentHorizontalPercent = 0;
	private int targetHorizontalPercent = 0;

	private int currentVerticalPercent = 0;
	private int targetVerticalPercent = 0;

	private int isGripperClosed = 1;

	public int getTargetVerticalPercent() {
		return targetVerticalPercent;
	}

	public void setTargetVerticalPercent(int targetVerticalPercent) {
		this.targetVerticalPercent = targetVerticalPercent;
	}

	public void setCurrentVerticalPercent(int currentVerticalPercent) {
		this.currentVerticalPercent = currentVerticalPercent;
	}

	public int getCurrentVerticalPercent() {
		return currentVerticalPercent;
	}

	public int getCurrentHorizontalPercent() {
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

	private RoboControl() {
	}

	public static synchronized RoboControl getInstance() {
		if (RoboControl.instance == null) {
			RoboControl.instance = new RoboControl();
		}
		return RoboControl.instance;
	}

	public int stop() {
		System.out.println("Stopping!");
		CaDSEV3RobotHAL.getInstance().stop_h();
		return 0;
	}

	public int closeGripper() {
		isGripperClosed = 1;
		CaDSEV3RobotHAL.getInstance().doClose();
		return 0;
	}

	public int isGripperClosed() {
		return isGripperClosed;
	}

	public int openGripper() {
		isGripperClosed = 0;
		CaDSEV3RobotHAL.getInstance().doOpen();
		return 0;
	}

	public int moveVerticalToPercent(int percent) {
		targetVerticalPercent = percent;

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

	public int moveHorizontalToPercent(int percent) {
		targetHorizontalPercent = percent;

		if (currentHorizontalPercent < targetHorizontalPercent) {
			Thread moveThread = new Thread() {
				public void run() {
					CaDSEV3RobotHAL.getInstance().moveLeft();
				}
			};
			moveThread.start();

		} else if (currentHorizontalPercent > targetHorizontalPercent) {
			Thread moveThread = new Thread() {
				public void run() {
					CaDSEV3RobotHAL.getInstance().moveRight();
				}
			};
			moveThread.start();

		}
		return 0;
	}

}
