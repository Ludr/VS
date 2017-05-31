package rmi.provider;


import java.net.UnknownHostException;

import org.cads.ev3.middleware.CaDSEV3RobotType;

public class SessionControl {

	
	
	String robotName;
	
	String ipAdress;
	
	private static SessionControl instance = null;
	
	CaDSEV3RobotType sessionType;

	private SessionControl(String ipAdress,String sessionType,String robotName) throws UnknownHostException{
		this.ipAdress = ipAdress;
		
		if(sessionType.equals("REAL")){
			this.sessionType = CaDSEV3RobotType.REAL;
		}else{
			this.sessionType = CaDSEV3RobotType.SIMULATION;
		}
		this.robotName = robotName;
		
	}
	
	static void createInstance(String ipAdress,String sessionType,String robotName) throws UnknownHostException{
		
		if(instance == null){
			instance =new SessionControl(ipAdress,sessionType,robotName);
		}
		
		
	}
	
	public static SessionControl getInstance(){
		
		return instance;
	}
	
	public String getSelectedRobot() {
		return robotName;
	}

	public void setRobotName(String robotName) {
		this.robotName = robotName;
	}

	public String getIpAdress() {
		return ipAdress;
	}

	public void setIpAdress(String ipAdress) {
		this.ipAdress = ipAdress;
	}

	public CaDSEV3RobotType getSessionType() {
		return sessionType;
	}

	public void setSessionType(CaDSEV3RobotType sessionType) {
		this.sessionType = sessionType;
	}
	
	
	
	
	
}
