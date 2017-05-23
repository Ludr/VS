package rmi.message;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RegisterMessage {



	@XmlElement
	public String name;
	
	@XmlElement
	public String service1;
	
	@XmlElement
	public String service2;
	
	@XmlElement
	public String service3;
	
	@XmlElement
	public int portNr;
	
	

//	public String getRobotName() {
//		return name;
//	}
//
//	public void setRobotName(String robotName) {
//		this.name = robotName;
//	}
//
//	public String getService1() {
//		return service1;
//	}
//
//	public void setService1(String service1) {
//		this.service1 = service1;
//	}
//
//	public String getService2() {
//		return service2;
//	}
//
//	public void setService2(String service2) {
//		this.service2 = service2;
//	}
//
//	public String getService3() {
//		return service3;
//	}
//
//	public void setService3(String service3) {
//		this.service3 = service3;
//	}
//
//	public int getPortNr() {
//		return portNr;
//	}
//
//	public void setPortNr(int portNr) {
//		this.portNr = portNr;
//	}


}
