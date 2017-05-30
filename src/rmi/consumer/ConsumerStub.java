package rmi.consumer;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import rmi.message.FunctionParameter;

public class ConsumerStub /* implemnts IDL interfaces */{

	private JAXBContext jaxbContext;
	private Marshaller jaxbMarshaller;

	private static ConsumerStub instance;

	public static synchronized ConsumerStub getInstance() {
		if (ConsumerStub.instance == null) {
			ConsumerStub.instance = new ConsumerStub();
		}
		return ConsumerStub.instance;
	}

	private ConsumerStub() {
		try {
			jaxbContext = JAXBContext.newInstance(FunctionParameter.class);
			jaxbMarshaller = jaxbContext.createMarshaller();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public int closeGripper() throws Exception{
		marshall(null, null);
		return 0;
	}

	public int openGripper() throws Exception {
		marshall(null, null);
		return 0;
	}

	public int moveVerticalToPercent(int percent) throws Exception {
		marshall(percent, null);
		return 0;
	}

	public int moveHorizontalToPercent(int percent) throws Exception {
		marshall(percent, null);
		return 0;
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
	 * @throws JAXBException
	 */
	private String marshall(Integer percent, Integer returnValue) throws InterruptedException, Exception {
		StringWriter writer = new StringWriter();

		FunctionParameter params = new FunctionParameter();
		//params.robotName = ?  TODO: Holen des aktuellen Roboternamens aus der GUI!

		params.robotName = GuiUpdater.getInstance().getSelectedRobot();
		params.functionName = Thread.currentThread().getStackTrace()[2].getMethodName();
		params.percent = percent;
		params.returnValue = returnValue;

		try {

			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);

			jaxbMarshaller.marshal(params, writer);

		} catch (JAXBException e) {
			e.printStackTrace();
		}

		TCPConnection.getInstance().getOutputQueueService().put(writer.toString());
		return writer.toString();
	}
}
