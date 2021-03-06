package rmi.provider;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import rmi.message.*;

//TODO RegisterService instanziieren, wo er nachher gerbraucht wird (TCP Connection??)

public class RegisterService {

	private JAXBContext jaxbContext ;
	private Marshaller jaxbMarshaller;

	public RegisterService(){
		try {
			jaxbContext = JAXBContext.newInstance(RegisterMessage.class);
			jaxbMarshaller = jaxbContext.createMarshaller();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void registerAtBroker(String robotName){
		try {
			marshall(robotName,"gripper","horizontal","vertical",8889);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}





	/**
	 * marshalls a method call
	 * @param percent - percentage of movement, null to ignore this parameter
	 * @param returnValue - null to ignore returnvalue
	 * @return
	 * 		returns marshalled object as xml string
	 * @throws InterruptedException
	 */
	private String marshall(String robotName, String gripperService, String horizontalService, String verticalService, int portNr) throws InterruptedException{

		StringWriter writer = new StringWriter();
		
		RegisterMessage registerMessage = new RegisterMessage();
		registerMessage.name = robotName;
		registerMessage.service1 = gripperService;
		registerMessage.service2 = horizontalService;
		registerMessage.service3 = verticalService;
		registerMessage.portNr = portNr;



		try {

			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);

			jaxbMarshaller.marshal(registerMessage, writer);


		} catch (JAXBException e) {
			e.printStackTrace();
		}

		TCPConnection.getInstance().getOutputQueueRegistry().put(writer.toString());
		return writer.toString();

	}


}
