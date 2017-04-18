package rmi.consumer;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.cads.ev3.rmi.generated.cadSRMIInterface.IIDLCaDSEV3RMIMoveGripper;
import org.cads.ev3.rmi.generated.cadSRMIInterface.IIDLCaDSEV3RMIMoveHorizontal;
import org.cads.ev3.rmi.generated.cadSRMIInterface.IIDLCaDSEV3RMIMoveVertical;
import org.cads.ev3.rmi.generated.cadSRMIInterface.IIDLCaDSEV3RMIUltraSonic;

import rmi.message.FunctionParameter;

public class RobotController implements IIDLCaDSEV3RMIMoveGripper, IIDLCaDSEV3RMIMoveHorizontal, IIDLCaDSEV3RMIMoveVertical, IIDLCaDSEV3RMIUltraSonic{
	
	public RobotController() {
		TCPConnection.getInstance();
	}
	
	@Override
	public int closeGripper(int arg0) throws Exception {
		FunctionParameter params = new FunctionParameter();
		params.functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
		String xmlString = marshall(params);
		TCPConnection.getInstance().getOutputQueue().put(xmlString);
		return 0;
	}

	@Override
	public int isGripperClosed() throws Exception {
		FunctionParameter params = new FunctionParameter();
		params.functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
		String xmlString = marshall(params);
		TCPConnection.getInstance().getOutputQueue().put(xmlString);
		return 0;
	}

	@Override
	public int openGripper(int arg0) throws Exception {
		FunctionParameter params = new FunctionParameter();
		params.functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
		String xmlString = marshall(params);
		TCPConnection.getInstance().getOutputQueue().put(xmlString);
		return 0;
	}
	
	private String marshall(FunctionParameter func){
		StringWriter writer = new StringWriter();
		
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(FunctionParameter.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);

			jaxbMarshaller.marshal(func, writer);
			
			
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return writer.toString() + "\n";	
	}

	@Override
	public int isUltraSonicOccupied() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getCurrentVerticalPercent() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int moveVerticalToPercent(int arg0, int arg1) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getCurrentHorizontalPercent() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int moveHorizontalToPercent(int arg0, int arg1) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int stop(int arg0) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

}
