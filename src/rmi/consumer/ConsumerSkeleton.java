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

	private ConsumerSkeleton() {

	}

	public static synchronized ConsumerSkeleton getInstance() {
		if (ConsumerSkeleton.instance == null) {
			ConsumerSkeleton.instance = new ConsumerSkeleton();
			new Thread(instance).start();
		}
		return ConsumerSkeleton.instance;
	}

	public void run() {
		while (true) {
			try {

				unmarshall(TCPConnection.getInstance().getIntputQueue().take());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void unmarshall(String XMLinput) throws Exception {
		StringReader reader = new StringReader(XMLinput);

		JAXBContext jaxbContext = JAXBContext.newInstance(FunctionParameter.class);

		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
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

