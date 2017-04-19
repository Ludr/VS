package rmi.provider;

import java.io.StringWriter;
import java.util.concurrent.BlockingQueue;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.cads.ev3.rmi.generated.cadSRMIInterface.IIDLCaDSEV3RMIMoveGripper;
import org.cads.ev3.rmi.generated.cadSRMIInterface.IIDLCaDSEV3RMIMoveHorizontal;
import org.cads.ev3.rmi.generated.cadSRMIInterface.IIDLCaDSEV3RMIMoveVertical;
import org.cads.ev3.rmi.generated.cadSRMIInterface.IIDLCaDSEV3RMIUltraSonic;

import rmi.message.FunctionParameter;

public class ProviderStub implements IIDLCaDSEV3RMIMoveGripper, IIDLCaDSEV3RMIMoveHorizontal, IIDLCaDSEV3RMIMoveVertical, IIDLCaDSEV3RMIUltraSonic{
	
	private BlockingQueue<String> outputQueue;
	
	
	public ProviderStub() {
		outputQueue = TCPConnection.getInstance().getOutputQueue();
	}
	
	@Override
	public int closeGripper(int arg0) throws Exception {
		marshall(null, null);
		return 0;
	}

	@Override
	public int isGripperClosed() throws Exception {
		return 0;
	}

	@Override
	public int openGripper(int arg0) throws Exception {
		marshall(null, null);
		return 0;
	}
	
	@Override
	public int isUltraSonicOccupied() throws Exception {
		marshall(null, null);
		return 0;
	}

	@Override
	public int getCurrentVerticalPercent() throws Exception {
		marshall(null, null);
		return 0;
	}

	@Override
	public int moveVerticalToPercent(int arg0, int arg1) throws Exception {
		marshall(arg1, null);
		return 0;
	}

	@Override
	public int getCurrentHorizontalPercent() throws Exception {
		marshall(null, null);
		return 0;
	}

	@Override
	public int moveHorizontalToPercent(int arg0, int arg1) throws Exception {
		marshall(arg1, null);
		return 0;
	}

	@Override
	public int stop(int arg0) throws Exception {
		marshall(null, null);
		return 0;
	}
	
	/**
	 * marshalls a method call
	 * @param percent - percentage of movement, null to ignore this parameter
	 * @param returnValue - null to ignore returnvalue
	 * @return
	 * 		returns marshalled object as xml string
	 * @throws InterruptedException
	 */
	private String marshall(Integer percent, Integer returnValue) throws InterruptedException{
		StringWriter writer = new StringWriter();
		
		FunctionParameter params = new FunctionParameter();
		params.functionName = Thread.currentThread().getStackTrace()[2].getMethodName();
		params.percent = percent;
		params.returnValue = returnValue;
		
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(FunctionParameter.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);

			jaxbMarshaller.marshal(params, writer);
			
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
		outputQueue.put(writer.toString());
		return writer.toString();
		
	}

}
