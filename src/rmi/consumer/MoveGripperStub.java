package rmi.consumer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.Socket;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.cads.ev3.rmi.generated.cadSRMIInterface.IIDLCaDSEV3RMIMoveGripper;

import rmi.message.FunctionParameter;

public class MoveGripperStub implements IIDLCaDSEV3RMIMoveGripper{
	
	private Socket socket;
	private DataOutputStream os = null;
    private DataInputStream is = null;
    
    public MoveGripperStub() {
		// TODO Auto-generated constructor stub
    	doConnect();
    }
	
	void doConnect(){
		
		try {
			socket = new Socket("localhost", 8888);
			os = new DataOutputStream(socket.getOutputStream());
			is = new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public int closeGripper(int arg0) throws Exception {
		FunctionParameter params = new FunctionParameter();
		params.functionName = 1;
		String xmlString = marshall(params);
		os.write(xmlString.getBytes());
		return 0;
	}

	@Override
	public int isGripperClosed() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int openGripper(int arg0) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}
	
	private String marshall(FunctionParameter func){
		StringWriter writer = new StringWriter();
		
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(FunctionParameter.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			jaxbMarshaller.marshal(func, writer);
			
			
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return writer.toString();
		
	}

}
