package rmi.provider;

import org.cads.ev3.middleware.CaDSEV3RobotHAL;
import org.cads.ev3.middleware.hal.ICaDSEV3RobotFeedBackListener;
import org.cads.ev3.middleware.hal.ICaDSEV3RobotStatusListener;
import org.json.simple.JSONObject;

public class StatusFeedbackListener implements ICaDSEV3RobotStatusListener, ICaDSEV3RobotFeedBackListener {

	private boolean isGripperClosed = true;
	
	@Override
	public void giveFeedbackByJSonTo(JSONObject arg0) {
		System.out.println("Hallo");

	}

	@Override
	public void onStatusMessage(JSONObject arg0) {
		// TODO Auto-generated method stub
		RoboControl rc = RoboControl.getInstance();
		String state = arg0.get("state").toString();

		try {
			if (state.equals("gripper")) {
				String value = arg0.get("value").toString();
				if (value.equals("open") && isGripperClosed) {
					isGripperClosed = false;
					rc.providerStub.openGripper();
				} else if( value.equals("closed") && !isGripperClosed) {
					isGripperClosed = true;
					rc.providerStub.closeGripper();
				}
			} else if (state.equals("horizontal")) {
				int newValue = Integer.valueOf(arg0.get("percent").toString());
				
				if (rc.getCurrentHorizontalPercent() < newValue) {
					
					rc.setCurrentHorizontalPercent(newValue);
					if (rc.getTargetHorizontalPercent() <= rc.getCurrentHorizontalPercent()) {
						CaDSEV3RobotHAL.getInstance().stop_h();
					}
					rc.providerStub.moveHorizontalToPercent(newValue);
				} else if (rc.getCurrentHorizontalPercent() > newValue) {
					
					rc.setCurrentHorizontalPercent(newValue);
					
					if (rc.getTargetHorizontalPercent() >= rc.getCurrentHorizontalPercent()) {
						CaDSEV3RobotHAL.getInstance().stop_h();
					}
					rc.providerStub.moveHorizontalToPercent(newValue);
				}
			} else if (state.equals("vertical")) {
				int newValue = Integer.valueOf(arg0.get("percent").toString());
				if (rc.getCurrentVerticalPercent() < newValue) {
					rc.setCurrentVerticalPercent(newValue);
					if (rc.getTargetVerticalPercent() <= rc.getCurrentVerticalPercent()) {
						CaDSEV3RobotHAL.getInstance().stop_v();
					}
					rc.providerStub.moveVerticalToPercent(newValue);
				} else if (rc.getCurrentVerticalPercent() > newValue) {
					rc.setCurrentVerticalPercent(newValue);
					if (rc.getTargetVerticalPercent() >= rc.getCurrentVerticalPercent()) {
						CaDSEV3RobotHAL.getInstance().stop_v();
					}
					rc.providerStub.moveVerticalToPercent(newValue);
				}
			}
		} catch (Exception e) {

		}
	}
	

}


