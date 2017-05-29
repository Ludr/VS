package rmi.message;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ConnectedRobots {

	@XmlElement
	String[] robotNames;
	
}
