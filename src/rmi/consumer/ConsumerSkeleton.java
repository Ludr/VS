package rmi.consumer;

import java.io.StringReader;

import javax.xml.bind.*;

import rmi.message.FunctionParameter;

/*
 * 
 * 
 */
public class ConsumerSkeleton extends Thread {

	private static ConsumerSkeleton instance;
	
	private JAXBContext jaxbContext;
	private Unmarshaller jaxbUnmarshaller;

	private ConsumerSkeleton() throws JAXBException {
		jaxbContext = JAXBContext.newInstance(FunctionParameter.class);

		jaxbUnmarshaller = jaxbContext.createUnmarshaller();

	}

	public static synchronized ConsumerSkeleton getInstance() throws JAXBException {
		if (ConsumerSkeleton.instance == null) {
			ConsumerSkeleton.instance = new ConsumerSkeleton();
			new Thread(instance).start();
		}
		return ConsumerSkeleton.instance;
	}

	public void run() {
		while (true) {
			try {

				unmarshall(TCPConnection.getInstance().getIntputQueueService().take());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void unmarshall(String XMLinput) throws Exception {
		StringReader reader = new StringReader(XMLinput);

		
		FunctionParameter functionParameter = (FunctionParameter) jaxbUnmarshaller.unmarshal(reader);

		switch (functionParameter.functionName) {
		case "openGripper":
			GuiUpdater.getInstance().openGripper(0);
			break;

		case "closeGripper":
			GuiUpdater.getInstance().closeGripper(0);
			break;

		}

	}

}

