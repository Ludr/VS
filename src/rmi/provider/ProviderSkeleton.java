package rmi.provider;

import javax.xml.bind.*;

import rmi.provider.TCPConnection.Sender;

import java.io.BufferedReader;
import java.io.File;
import java.io.StringReader;

/*
 * 
 * 
 */
public class ProviderSkeleton extends Thread {

	private static ProviderSkeleton instance;
	private TCPConnection tcpConnection;

	private ProviderSkeleton() {

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
			RoboControl.getInstance().openGripper(0);
			break;

		case "closeGripper":
			RoboControl.getInstance().closeGripper(0);
			break;

		}

	}

}
