package rmi.generated;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import rmi.provider.*;


import rmi.message.FunctionParameter;

public class ProviderStub {

	private JAXBContext jaxbContext;
	private Marshaller jaxbMarshaller;

  // generate Singleton
	private static ProviderStub instance;

public static synchronized ProviderStub getInstance() {
  if (instance == null) {
    instance = new ProviderStub();
  }
  return instance;
}

private ProviderStub() {
  try {
    jaxbContext = JAXBContext.newInstance(FunctionParameter.class);
    jaxbMarshaller = jaxbContext.createMarshaller();
  } catch (JAXBException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
  }
}


	// generate functions
	public int closeGripper() {
    marshall(null);
    return 0;
}
public int openGripper() {
    marshall(null);
    return 0;
}
public int moveVerticalToPercent(int percent) {
    marshall(percent);
    return 0;
}
public int moveHorizontalToPercent(int percent) {
    marshall(percent);
    return 0;
}


	/**
	 * marshalls a method call
	 *
	 * @param percent
	 *            - percentage of movement, null to ignore this parameter
	 * @return returns marshalled object as xml string
	 */
	 private String marshall(Integer percent) {
 		StringWriter writer = new StringWriter();

 		FunctionParameter params = new FunctionParameter();
 		params.robotName = SessionControl.getInstance().getSelectedRobot();
 		params.functionName = Thread.currentThread().getStackTrace()[2].getMethodName();
 		params.percent = percent;
 		params.returnValue = 1;

 		try {
 			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
 			jaxbMarshaller.marshal(params, writer);

 			TCPConnection.getInstance().getOutputQueueService().put(writer.toString());
 		} catch (JAXBException e) {
 			e.printStackTrace();
 		} catch (InterruptedException e) {
 			e.printStackTrace();
 		}

 		return writer.toString();
 	}
}
