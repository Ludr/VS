package rmi.provider;
import org.cads.ev3.middleware.hal.ICaDSEV3RobotFeedBackListener;
import org.json.simple.JSONObject;

public class Feedbacklistener implements ICaDSEV3RobotFeedBackListener {

	@Override
	public void giveFeedbackByJSonTo(JSONObject arg0) {
		System.out.println(arg0);
	}

}
