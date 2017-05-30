package rmi.consumer;

public class connectedRobot {

	
	private String robotName;
	
	private int horizontalPercent;
	
	private int verticalPercent;
	
	private boolean grabbed;

	
	
	public connectedRobot(String name,int horPer,int verPer,boolean gra){
		
		setRobotName(name);
		
		setHorizontalPercent(horPer);
		
		setVerticalPercent(verPer);
		
		setGrabbed(gra);
		
		
	}



	public String getRobotName() {
		return robotName;
	}



	public void setRobotName(String robotName) {
		this.robotName = robotName;
	}



	public int getHorizontalPercent() {
		return horizontalPercent;
	}



	public void setHorizontalPercent(int horPer) {
		this.horizontalPercent = horPer;
	}



	public int getVerticalPercent() {
		return verticalPercent;
	}



	public void setVerticalPercent(int verPer) {
		this.verticalPercent = verPer;
	}



	public boolean isGrabbed() {
		return grabbed;
	}



	public void setGrabbed(boolean grabbed) {
		this.grabbed = grabbed;
	}
	
	

	
	
	
}
