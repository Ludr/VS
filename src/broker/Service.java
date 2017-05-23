package broker;

import java.net.Socket;

public class Service {

	
	private String robotName;
	
	private String service;
	
	private int portNr;
	
	private String ipAdress;
	
	private Socket socket;
	
	public Service(String name, String service,int port,String ipAdress,Socket socket){
		
		this.robotName = name;
		this.service = service;
		this.portNr = port;
		this.ipAdress = ipAdress;
		this.socket = socket;
	}

	public String getRobotName() {
		return robotName;
	}

	public void setRobotName(String robotName) {
		this.robotName = robotName;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public int getPortNr() {
		return portNr;
	}

	public void setPortNr(int portNr) {
		this.portNr = portNr;
	}

	public String getIpAdress() {
		return ipAdress;
	}

	public void setIpAdress(String ipAdress) {
		this.ipAdress = ipAdress;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	
}
