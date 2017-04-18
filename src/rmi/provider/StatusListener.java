package rmi.provider;
import org.cads.ev3.middleware.hal.ICaDSEV3RobotStatusListener;
import org.json.simple.JSONObject;

public class StatusListener implements ICaDSEV3RobotStatusListener {

	@Override
	public void onStatusMessage(JSONObject arg0) {
		// TODO Auto-generated method stub
//		System.out.println(arg0);
	}

}
