package rmi.message;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FunctionParameter {
	
	@XmlElement
	public Integer functionName;
	
	@XmlElement
	public Integer percent;
	
	@XmlElement
	public Integer returnValue;
	
}