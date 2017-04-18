package rmi.provider;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FunctionParameter {
	
	
	@XmlElement
	public String functionName;
	
	@XmlElement
	public Integer percent;
	
	@XmlElement
	public Integer returnValue;
	
}
