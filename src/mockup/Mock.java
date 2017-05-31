package mockup;

public class Mock {

	private static Mock instance;

	public static volatile int requestedHorizontalPercent = 0;
	public static volatile int requestedVerticalPercent = 0;
	public static volatile boolean requestedGrabberState = false;

	public static volatile boolean reachedGoalHorizontal = false;
	public static volatile boolean reachedGoalVertical = false;
	public static volatile boolean grabberState;

	public static synchronized Mock getInstance() {
		if (instance == null) {
			instance = new Mock();
			// new Thread(instance).start();
		}
		return instance;
	}

	private Mock() {

	}

	public int moveVerticalToPercent(int percent) {
		
		if (percent == requestedVerticalPercent) {
			reachedGoalVertical = true;
		}
		return 0;

	}

	public int moveHorizontalToPercent(int percent) {
		
		if (percent == requestedHorizontalPercent) {
			reachedGoalHorizontal = true;
		}
		return 0;

	}

	public int closeGripper() {

		grabberState = true;

		return 0;

	}

	public int openGripper() {

		grabberState = false;

		return 0;
	}

	public void test() {

		

		ConsumerStub stub = ConsumerStub.getInstance();

		System.out.println("Mock-up test run: ");

		System.out.println("");

		System.out.println("Move Robot on Horizontal Axis to 100 Percent");

		Mock.requestedHorizontalPercent = 100;

		stub.moveHorizontalToPercent(100);

		while (reachedGoalHorizontal == false) {

		}

		reachedGoalHorizontal = false;

		System.out.println("Reached 100 Percent!");

		System.out.println("Move Robot on Horizontal Axis to 0 Percent");

		requestedHorizontalPercent = 0;

		stub.moveHorizontalToPercent(0);

		while (reachedGoalHorizontal == false) {

		}

		reachedGoalHorizontal = false;

		System.out.println("Reached 0 Percent!");

		System.out.println("Move Robot on Vertical Axis to 100 Percent");

		requestedVerticalPercent = 100;

		stub.moveVerticalToPercent(100);

		while (reachedGoalVertical == false) {

		}

		reachedGoalVertical = false;

		System.out.println("Reached 100 Percent!");

		System.out.println("Move Robot on Vertical Axis to 0 Percent");

		requestedVerticalPercent = 0;

		stub.moveVerticalToPercent(0);

		while (reachedGoalVertical == false) {

		}

		reachedGoalVertical = false;

		System.out.println("Reached 0 Percent!");

		

		System.out.println("Open Robot Gripper");

		requestedGrabberState = false;

		stub.openGripper();

		while (grabberState == true) {

		}

		grabberState = false;

		System.out.println("Gripper opened!");
		
		System.out.println("Close Robot Gripper");

		requestedGrabberState = true;

		stub.closeGripper();

		while (grabberState == false) {

		}

		grabberState = true;

		System.out.println("Gripper closed!");
	}

}
