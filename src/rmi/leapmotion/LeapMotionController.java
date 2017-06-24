package rmi.leapmotion;

import java.util.Timer;
import java.util.TimerTask;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import java.io.StringReader;
import java.lang.Math;

import com.leapmotion.leap.CircleGesture;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.Gesture.State;
import com.leapmotion.leap.GestureList;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.KeyTapGesture;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.ScreenTapGesture;
import com.leapmotion.leap.SwipeGesture;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import rmi.consumer.TCPConnection;
import rmi.consumer.connectedRobot;
import rmi.generated.ConsumerStub;
import rmi.message.RegisterMessage;

public class LeapMotionController extends Listener {
	private final int start_x = -100;
	private final int start_y = 150;
	private final int end_x = 100;
	private final int end_y = 400;
	private final int range = 100;
	
	static int maxRobots = 20;
	
	public StringProperty horizontalString = new SimpleStringProperty("0");
	public StringProperty verticalString = new SimpleStringProperty("0");

	public StringProperty pinchStrengthString = new SimpleStringProperty("0.0");
	public StringProperty grabStrengthString = new SimpleStringProperty("0.0");

	public static String currentRobotString = "robot1";
	
	private double x = 0;
	private double y = 0;
	
	private int current_horizontal = 0;
	private int current_vertical = 0;

	private int horizontalPercent = 0;
	private int verticalPercent = 0;

	boolean isPinched = false;
	boolean isGrabed = false;
	private boolean isGripperClosed = false;

	private int lastCircle = 0;
	private double totalSweptAngle = 0;
	
	private double pinchStrength = 0;
	private double grabStrength = 0;
	
	private int currentRobotIndex = 0;
	public static connectedRobot[] connectedRobots = new connectedRobot[maxRobots];
	Unmarshaller jaxbUnmarshaller;
	JAXBContext jaxbContext;

	public void onInit(Controller controller) {
		System.out.println("Initialized");
	}

	public void onConnect(Controller controller) {
		System.out.println("Connected");
		controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
		TimerTask action = new TimerTask() {
			public void run() {
				if (Math.abs(current_horizontal - horizontalPercent) > 5){
					ConsumerStub.getInstance().moveHorizontalToPercent(range - horizontalPercent);
					current_horizontal = horizontalPercent;
				}
				if (Math.abs(current_vertical - verticalPercent) > 5){
					ConsumerStub.getInstance().moveVerticalToPercent(verticalPercent);
					current_vertical = verticalPercent;
				}
				if (grabStrength > 0.95){
					ConsumerStub.getInstance().closeGripper();
				} else if (grabStrength < 0.05){
					ConsumerStub.getInstance().openGripper();
				}
			}

		};
		Timer timedCaller = new Timer();
		timedCaller.schedule(action, 500, 500);
	}

	public void onDisconnect(Controller controller) {
		// Note: not dispatched when running in a debugger.
		System.out.println("Disconnected");
	}

	public void onExit(Controller controller) {
		System.out.println("Exited");
	}

	public void onFrame(Controller controller) {
		// Get the most recent frame and report some basic information
		Frame frame = controller.frame();

		// Get hands
		for (Hand hand : frame.hands()) {
			// String handType = hand.isLeft() ? "Left hand" : "Right hand";
			// System.out.println(" " + handType + ", id: " + hand.id() + ",
			// palm position: " + hand.palmPosition());
			// System.out.println("Grab: " + hand.grabStrength() + "\t Pinch: "
			// + hand.pinchStrength());
			x = hand.palmPosition().getX();
			y = hand.palmPosition().getY();

			horizontalPercent = (int) map((int) x, start_x, end_x, range, 0);
			horizontalPercent = ensureRange(horizontalPercent, 0, range);
			verticalPercent = (int) map((int) y, start_y, end_y, 0, range);
			verticalPercent = ensureRange(verticalPercent, 0, range);

			// Call Methods
			pinchStrength = hand.pinchStrength();
			grabStrength = hand.grabStrength();

//			if (pinchStrength > 0.80 && grabStrength < 0.2 && !isPinched) {
//				isPinched = true;
//				// call to horitontal Percent
//				System.out.println("Method Call: move to horizontal " + horizontalPercent);
//				ConsumerStub.getInstance().moveHorizontalToPercent(range-horizontalPercent);
//				// call to vertical percent
//				System.out.println("Method Call: move to vertical " + verticalPercent);
//				ConsumerStub.getInstance().moveVerticalToPercent(verticalPercent);
//			} else if (grabStrength > 0.95 && !isGrabed) {
//				isGrabed = true;
//				if (isGripperClosed) {
//					System.out.println("Method Call: open gripper");
//					ConsumerStub.getInstance().openGripper();
//					isGripperClosed = false;
//				} else {
//					System.out.println("Method Call: close gripper");
//					ConsumerStub.getInstance().closeGripper();
//					isGripperClosed = true;
//				}
//			} else if (pinchStrength < 0.1 && grabStrength < 0.1 && isPinched) {
//				isPinched = false;
//			} else if (grabStrength < 0.1 && isGrabed) {
//				isGrabed = false;
//			}

			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					horizontalString.setValue(String.valueOf(horizontalPercent));
					verticalString.setValue(String.valueOf(verticalPercent));
					pinchStrengthString.setValue(String.valueOf(pinchStrength));
					grabStrengthString.setValue(String.valueOf(grabStrength));
				}
			});

		}
		// circle to change robot
		GestureList gestures = frame.gestures();
		for (int i = 0; i < gestures.count(); i++) {
			Gesture gesture = gestures.get(i);
			switch (gesture.type()) {
			case TYPE_CIRCLE:
				CircleGesture circle = new CircleGesture(gesture);
				if (lastCircle != circle.id()) {
					lastCircle = circle.id();
					totalSweptAngle = 0;
				}
				// Calculate clock direction using the angle between circle
				// normal and pointable
				boolean clockwise;
				if (circle.pointable().direction().angleTo(circle.normal()) <= Math.PI / 2) {
					// Clockwise if angle is less than 90 degrees
					clockwise = true;
				} else {
					clockwise = false;
				}

				// Calculate angle swept since last frame
				double sweptAngle = 0;
				if (circle.state() != State.STATE_START) {
					CircleGesture previousUpdate = new CircleGesture(controller.frame(1).gesture(circle.id()));
					totalSweptAngle += (circle.progress() - previousUpdate.progress()) * 2 * Math.PI;
				}

//				System.out.println(" Circle id: " + circle.id() + ", clockwise: " + clockwise + ", amount : " + totalSweptAngle);
				if (totalSweptAngle > 4) {
					if (clockwise) {
						// get next robot
						currentRobotString = "robot2";
						System.out.println("Method call: change to next robot -> " + currentRobotString);
						totalSweptAngle = -2;
					} else {
						// get previous robot
						currentRobotString = "robot1";
						System.out.println("Method call: change to previous robot -> " + currentRobotString);
						totalSweptAngle = -2;
					}
				}

				break;
			default:
				System.out.println("Unknown gesture type.");
				break;
			}
		}

		// clear TCP Queue
		String registyMessage = TCPConnection.getInstance().getIntputQueueService().poll();
		if(registyMessage != null) {
			unmarshall(registyMessage);
		}
	}

	public void unmarshall(String XMLinput) {

		StringReader reader = new StringReader(XMLinput);

		RegisterMessage newRobot = null;
		try {
			newRobot = (RegisterMessage) jaxbUnmarshaller.unmarshal(reader);
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		System.out.println(newRobot.name);

		// Consumer.gui.setChoosenService(newRobot.name);

		for (int i = 0; i < connectedRobots.length; i++) {

			if (connectedRobots[i] == null) {
				connectedRobots[i] = new connectedRobot(newRobot.name, 0, 0, false);
				System.out.println("Neuer Roboter! " + connectedRobots[i].getRobotName());
				break;

			} else if (connectedRobots[i].getRobotName() == newRobot.name) {
				System.out.println("Duplicate Robot received");
				break;
			}

		}
	}
	
//	private String nextRobot(){
//		currentRobotIndex = currentRobotIndex < maxRobots-1 ? currentRobotIndex++ : 0; 
//		setSelectedRobot(connectedRobots[currentRobotIndex].getRobotName());
//		return getSelectedRobot();
//	}
//	
//	private String previousRobot(){
//		currentRobotIndex = currentRobotIndex > 0 ? currentRobotIndex-- : maxRobots-1; 
//		setSelectedRobot(connectedRobots[currentRobotIndex].getRobotName());
//		return getSelectedRobot();
//	}
	
	private long map(long x, long in_min, long in_max, long out_min, long out_max) {
		return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
	}

	private int ensureRange(int value, int min, int max) {
		return Math.min(Math.max(value, min), max);
	}
}