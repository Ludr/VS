package rmi.provider;

import javax.xml.bind.*;

import rmi.provider.TCPConnection.Sender;

import java.io.File;


/*
 * 
 * 
 */
public class ProviderSkeleton extends Thread {

	private static ProviderSkeleton instance;
	private TCPConnection tcpConnection;
	
	private ProviderSkeleton(){
	RoboControl roboControl = RoboControl.getInstance();
	tcpConnection = TCPConnection.getInstance();
	new Thread(new ProviderSkeleton()).start();
	}
	
	public static synchronized ProviderSkeleton getInstance() {
		if (ProviderSkeleton.instance == null) {
			ProviderSkeleton.instance = new ProviderSkeleton();
		}
		return ProviderSkeleton.instance;
	}

	public void run(){
		try {
			unmarshall(tcpConnection.getIntputQueue().take());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public static void unmarshall(String XMLinput) throws Exception{
		
		File file = new File(XMLinput);
		JAXBContext jaxbContext = JAXBContext.newInstance(FunctionParameter.class);

		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		FunctionParameter functionParameter = (FunctionParameter) jaxbUnmarshaller.unmarshal(file);
		
		
		
		switch(functionParameter.functionName){
		case "openGripper"   : RoboControl.getInstance().openGripper(0); break;
		
		case  "closeGripper" : RoboControl.getInstance().closeGripper(0);break;
		
		}
		
		
	}
	
}
