package rmi.consumer;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import rmi.message.*;

//TODO RegisterService instanziieren, wo er nachher gerbraucht wird (TCP Connection??)

public class RegisterService {

	private JAXBContext jaxbContext;
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

	public void registerAtBroker(String Name) {
		try {
			marshall(Name, "whatever", "whatever1", "whatever2", 8888);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * marshalls a method call
	 * 
	 * @param percent
	 *            - percentage of movement, null to ignore this parameter
	 * @param returnValue
	 *            - null to ignore returnvalue
	 * @return returns marshalled object as xml string
	 * @throws InterruptedException
	 */
	private String marshall(String Name, String gripperService,
			String horizontalService, String verticalService, int portNr)
			throws InterruptedException {

		StringWriter writer = new StringWriter();

		RegisterMessage registerMessage = new RegisterMessage();
		
		registerMessage.name = Name;
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
		System.out.println(writer.toString());
		TCPConnection.getInstance().getOutputQueueRegistry()
				.put(writer.toString());
		return writer.toString();

	}

}
