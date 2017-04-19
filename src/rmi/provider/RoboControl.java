package rmi.provider;

import org.cads.ev3.middleware.CaDSEV3RobotStudentImplementation;
import org.cads.ev3.middleware.hal.ICaDSEV3RobotStatusListener;
import org.cads.ev3.rmi.generated.cadSRMIInterface.IIDLCaDSEV3RMIMoveGripper;
import org.cads.ev3.rmi.generated.cadSRMIInterface.IIDLCaDSEV3RMIMoveHorizontal;
import org.cads.ev3.rmi.generated.cadSRMIInterface.IIDLCaDSEV3RMIMoveVertical;
import org.cads.ev3.rmi.generated.cadSRMIInterface.IIDLCaDSEV3RMIUltraSonic;
import org.json.simple.JSONObject;

public class RoboControl implements ICaDSEV3RobotStatusListener,
		IIDLCaDSEV3RMIMoveGripper, IIDLCaDSEV3RMIMoveHorizontal,
		IIDLCaDSEV3RMIMoveVertical, IIDLCaDSEV3RMIUltraSonic {

	private static RoboControl instance;
	ProviderStub providerStub = new ProviderStub();

	private volatile int currentPercentHorizontal = 0;
	private int targetPercentHorizontal = 0;

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
		return 0;
	}

	@Override
	public int moveVerticalToPercent(int arg0, int arg1) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getCurrentHorizontalPercent() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int moveHorizontalToPercent(int arg0, int arg1) throws Exception {
		targetPercentHorizontal = arg1;

		if (currentPercentHorizontal < targetPercentHorizontal) {
			Thread t = new Thread() {
				public void run() {
					CaDSEV3RobotStudentImplementation.getInstance().moveLeft();
				}
			};
			t.start();
		} else if (currentPercentHorizontal > targetPercentHorizontal) {
			Thread t = new Thread() {
				public void run() {
					CaDSEV3RobotStudentImplementation.getInstance().moveRight();
				}
			};
			t.start();
		}

		Thread t2 = new Thread() {
			public void run() {
				while (currentPercentHorizontal != targetPercentHorizontal) {
				}
				;
				CaDSEV3RobotStudentImplementation.getInstance().stop_h();
			}
		};
		t2.start();
		return 0;
	}

	@Override
	public int stop(int arg0) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int closeGripper(int arg0) throws Exception {
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
		CaDSEV3RobotStudentImplementation.getInstance().doOpen();
		return 0;
	}

	@Override
	public void onStatusMessage(JSONObject arg0) {
		// TODO Auto-generated method stub
		String state = arg0.get("state").toString();
		// System.out.println(arg0);

		try {
			if (state.equals("gripper")) {
				String value = arg0.get("value").toString();
				if (value.equals("open")) {
					providerStub.openGripper(0);
				} else {
					providerStub.closeGripper(0);
				}
			} else if (state.equals("horizontal")) {
				String value = arg0.get("percent").toString();
				currentPercentHorizontal = Integer.valueOf(value);
				providerStub
						.moveHorizontalToPercent(0, Integer.parseInt(value));
			} else if (state.equals("vertica")) {
				String value = arg0.get("percent").toString();
				providerStub.moveVerticalToPercent(0, Integer.parseInt(value));
			}
		} catch (Exception e) {

		}
	}
}
