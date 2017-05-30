package rmi.provider;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.cads.ev3.rmi.generated.cadSRMIInterface.IIDLCaDSEV3RMIMoveGripper;
import org.cads.ev3.rmi.generated.cadSRMIInterface.IIDLCaDSEV3RMIMoveHorizontal;
import org.cads.ev3.rmi.generated.cadSRMIInterface.IIDLCaDSEV3RMIMoveVertical;
import org.cads.ev3.rmi.generated.cadSRMIInterface.IIDLCaDSEV3RMIUltraSonic;

import rmi.message.FunctionParameter;

public class ProviderStub implements IIDLCaDSEV3RMIMoveGripper,
		IIDLCaDSEV3RMIMoveHorizontal, IIDLCaDSEV3RMIMoveVertical,
		IIDLCaDSEV3RMIUltraSonic {

	private JAXBContext jaxbContext;
	private Marshaller jaxbMarshallerGripper;
	private Marshaller jaxbMarshallerHorizontal;
	private Marshaller jaxbMarshallerVertical;

	private static ProviderStub instance;

	public static synchronized ProviderStub getInstance() {
		if (ProviderStub.instance == null) {
			ProviderStub.instance = new ProviderStub();
		}
		return ProviderStub.instance;
	}

	private ProviderStub() {
		try {
			jaxbContext = JAXBContext.newInstance(FunctionParameter.class);
			jaxbMarshallerGripper = jaxbContext.createMarshaller();
			jaxbMarshallerHorizontal = jaxbContext.createMarshaller();
			jaxbMarshallerVertical = jaxbContext.createMarshaller();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public int closeGripper(int arg0) throws Exception {
		marshall(null, 1);
		return 0;
	}

	@Override
	public int isGripperClosed() throws Exception {
		return 0;
	}

	@Override
	public int openGripper(int arg0) throws Exception {
		marshall(null, 1);
		return 0;
	}

	@Override
	public int isUltraSonicOccupied() throws Exception {
		marshall(null, 1);
		return 0;
	}

	@Override
	public int getCurrentVerticalPercent() throws Exception {
		marshall(null, 1);
		return 0;
	}

	@Override
	public int moveVerticalToPercent(int arg0, int arg1) throws Exception {
		marshall(arg1, 1);
		return 0;
	}

	@Override
	public int getCurrentHorizontalPercent() throws Exception {
		marshall(null, 1);
		return 0;
	}

	@Override
	public int moveHorizontalToPercent(int arg0, int arg1) throws Exception {
		marshall(arg1, 1);
		return 0;
	}

	@Override
	public int stop(int arg0) throws Exception {
		marshall(null, 1);
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
	private String marshall(Integer percent, Integer returnValue)
			throws InterruptedException {

		StringWriter writer = new StringWriter();

		FunctionParameter params = new FunctionParameter();
		params.robotName = SessionControl.getInstance().robotName;
		params.functionName = Thread.currentThread().getStackTrace()[2].getMethodName();
		params.percent = percent;
		params.returnValue = returnValue;

		try {
			switch (params.functionName) {

		case "moveHorizontalToPercent":
			jaxbMarshallerHorizontal.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
			jaxbMarshallerHorizontal.marshal(params, writer);
			break;

		case "moveVerticalToPercent":
			jaxbMarshallerVertical.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
			jaxbMarshallerVertical.marshal(params, writer);
			break;

		case "openGripper":
			jaxbMarshallerGripper.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
			jaxbMarshallerGripper.marshal(params, writer);
			break;

		case "closeGripper":
			jaxbMarshallerGripper.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
			jaxbMarshallerGripper.marshal(params, writer);
			break;
		}




		} catch (JAXBException e) {
			e.printStackTrace();
		}

		TCPConnection.getInstance().getOutputQueueService().put(writer.toString());
		return writer.toString();

	}

}
