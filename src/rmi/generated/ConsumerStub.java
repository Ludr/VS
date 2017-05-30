package rmi.generated;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import rmi.message.FunctionParameter;

public class ConsumerStub {

	private JAXBContext jaxbContext;
	private Marshaller jaxbMarshaller;

	// generate Singleton
	private static ConsumerStub instance;

	public static synchronized ConsumerStub getInstance() {
		if (instance == null) {
			instance = new ConsumerStub();
		}
		return instance;
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

	// generate functions
	public int closeGripper() {
		marshall(null, null);
		return 0;
	}

	public int openGripper() {
		marshall(null, null);
		return 0;
	}

	public int moveVerticalToPercent(int percent) {
		marshall(percent, null);
		return 0;
	}

	public int moveHorizontalToPercent(int percent) {
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
	 */
	private String marshall(Integer percent, Integer returnValue) {
		StringWriter writer = new StringWriter();

		FunctionParameter params = new FunctionParameter();
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

		// TCPConnection.getInstance().getOutputQueue().put(writer.toString());
		return writer.toString();
	}
}
