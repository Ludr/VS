package rmi.provider;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import rmi.message.*;


/*
 * 
 * 
 */
public class ProviderSkeleton extends Thread {

	private static ProviderSkeleton instance;
	private JAXBContext jaxbContext; 

	private static Unmarshaller jaxbUnmarshaller;

	private ProviderSkeleton() {
		try {
			jaxbContext = JAXBContext.newInstance(FunctionParameter.class);
			jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

	public static synchronized ProviderSkeleton getInstance() {
		if (ProviderSkeleton.instance == null) {
			ProviderSkeleton.instance = new ProviderSkeleton();
			new Thread(instance).start();
		}
		return ProviderSkeleton.instance;
	}

	public void run() {
		while (true) {
			try {

				unmarshall(TCPConnection.getInstance().getIntputQueueService().take());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void unmarshall(String XMLinput) throws Exception {
		
		
		StringReader reader = new StringReader(XMLinput);

		
		FunctionParameter functionParameter = (FunctionParameter) jaxbUnmarshaller.unmarshal(reader);

		switch (functionParameter.functionName) {
		case "openGripper":
			RoboControl.getInstance().openGripper();
			break;
		case "closeGripper":
			RoboControl.getInstance().closeGripper();
			break;
		case "moveHorizontalToPercent":
			RoboControl.getInstance().moveHorizontalToPercent(functionParameter.percent);
			break;
		case "moveVerticalToPercent":
			RoboControl.getInstance().moveVerticalToPercent(functionParameter.percent);
			break;
		}

	}

}
