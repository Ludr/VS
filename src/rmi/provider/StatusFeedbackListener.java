package rmi.provider;

import org.cads.ev3.middleware.CaDSEV3RobotHAL;
import org.cads.ev3.middleware.hal.ICaDSEV3RobotFeedBackListener;
import org.cads.ev3.middleware.hal.ICaDSEV3RobotStatusListener;
import org.json.simple.JSONObject;

public class StatusFeedbackListener implements ICaDSEV3RobotStatusListener, ICaDSEV3RobotFeedBackListener {

	@Override
	public void giveFeedbackByJSonTo(JSONObject arg0) {
		// TODO Auto-generated method stub

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
					ProviderStub.getInstance().openGripper(0);
				} else {
					ProviderStub.getInstance().closeGripper(0);
				}
			} else if (state.equals("horizontal")) {
				String value = arg0.get("percent").toString();
				RoboControl.getInstance().setCurrentHorizontalPercent(Integer.valueOf(value));
				// System.out.println(currentPercentHorizontal);
				if (RoboControl.getInstance().getCurrentHorizontalPercent() == RoboControl.getInstance()
						.getTargetHorizontalPercent()) {
					CaDSEV3RobotHAL.getInstance().stop_h();
				}
			} else if (state.equals("vertical")) {
				String value = arg0.get("percent").toString();
				RoboControl.getInstance().setCurrentVerticalPercent(Integer.valueOf(value));
				if (RoboControl.getInstance().getCurrentVerticalPercent() >= RoboControl.getInstance()
						.getTargetVerticalPercent()) {
					CaDSEV3RobotHAL.getInstance().stop_v();
				}
			}
		} catch (Exception e) {

		}
	}

}
