package broker;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.Socket;
import java.util.HashSet;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import rmi.message.FunctionParameter;
import rmi.message.RegisterMessage;

public class WorkerRunnable implements Runnable {

	private static Service[] offeredServices = new Service[200];

	String portType;

	protected Socket clientSocket = null;
	protected String serverText = null;

	private JAXBContext jaxbContextFunction;
	private JAXBContext jaxbContextRegister;

	private static Unmarshaller jaxbUnmarshallerFunction;
	private static Marshaller jaxbMarshallerFunction;
	private static Unmarshaller jaxbUnmarshallerRegister;
	private static Marshaller jaxbMarshallerRegister;

	public WorkerRunnable(Socket clientSocket, String type) throws JAXBException {
		this.clientSocket = clientSocket;
		portType = type;

		jaxbContextFunction = JAXBContext.newInstance(FunctionParameter.class);

		jaxbContextRegister = JAXBContext.newInstance(RegisterMessage.class);

		jaxbUnmarshallerFunction = jaxbContextFunction.createUnmarshaller();
		jaxbMarshallerFunction = jaxbContextFunction.createMarshaller();

		jaxbUnmarshallerRegister = jaxbContextRegister.createUnmarshaller();
		jaxbMarshallerRegister = jaxbContextRegister.createMarshaller();

		// offeredServices[0] = new Service("Hannes", null, 0, null, null);
		// offeredServices[1] = new Service("Lucy", null, 0, null, null);
		// offeredServices[2] = new Service("Fuckhead", null, 0, null, null);
	}

	public void run() {

		BufferedReader in;
		try {
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			String inputline = "";
			while ((inputline = in.readLine()) != null) {

				if (portType.equals("service")) {
					processInputService(clientSocket, inputline);
				} else if (portType.equals("registry")) {
					processInputRegistry(clientSocket, inputline);
				}

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private synchronized void processInputRegistry(Socket sc, String inputline) {
		// System.out.println("processInputRegisry");

		RegisterMessage message = null;
		try {
			message = unmarshallRegisterMessage(inputline);
		} catch (Exception e) {
			// TODO: handle exception
		}

		// System.out.println(message.name);

		if (message.name.equals("gui")) {
			for (int i = 0; i < offeredServices.length; i++) {
				if (offeredServices[i] == null) {
					offeredServices[i] = new Service(message.name, null, message.portNr, null, sc);
					break;
				}
			}
			
			handleServiceDiscoveryAll(sc);

		} else {

			String newRobotName = message.name;
			for (int i = 0; i < offeredServices.length; i++) {

				if (offeredServices[i] == null) {

					offeredServices[i] = new Service(message.name, message.service1, message.portNr, null, sc);
					i++;
					offeredServices[i] = new Service(message.name, message.service2, message.portNr, null, sc);
					i++;
					offeredServices[i] = new Service(message.name, message.service3, message.portNr, null, sc);
					break;

				}

				// if (offeredServices[i] != null) {
				// if (offeredServices[i].getRobotName() == message.name) {
				// System.out
				// .println("Duplicate registration, registration not
				// completed!");
				// }
				// } else if (offeredServices[i] == null) {
				// offeredServices[i] = new Service(message.name,
				// message.service1, message.portNr, null, sc);
				// if (offeredServices[++i] == null) {
				// offeredServices[i] = new Service(message.name,
				// message.service2, message.portNr, null,
				// sc);
				// } else {
				// System.out
				// .println("Problem while saving services, expected null but
				// wasn't");
				// }
				// if (offeredServices[++i] == null) {
				// offeredServices[i] = new Service(message.name,
				// message.service3, message.portNr, null,
				// sc);
				// } else {
				// System.out
				// .println("Problem while saving services, expected null but
				// wasn't");
				// }
				// for (int j = 0; j < offeredServices.length; j++) {
				// System.out.println(offeredServices[i].getService());
				// }
				// break;
				//
				// }

			}
			for (int j = 0; j < offeredServices.length; j++) {
				if (offeredServices[j] == null || offeredServices[j].getRobotName() == null) continue;
				if (offeredServices[j].getRobotName().equals("gui")) {
					handleServiceDiscoveryNewRobot(offeredServices[j].getSocket(), newRobotName);
					break;
				}
			}
		}

	}

	private synchronized void handleServiceDiscoveryNewRobot(Socket socket, String newRobot) {

		PrintWriter out = null;

		StringWriter writer;

		RegisterMessage serviceDiscoveryMessage = new RegisterMessage();
		serviceDiscoveryMessage.name = newRobot;
		serviceDiscoveryMessage.service1 = null;
		serviceDiscoveryMessage.service2 = null;
		serviceDiscoveryMessage.service3 = null;
		serviceDiscoveryMessage.portNr = 0;

		writer = marshallServiceDiscovery(serviceDiscoveryMessage);

		String message = writer.toString();

		// System.out.println("Nachricht an Client:" + message);

		// System.out.println(message.length());

		try {
			out = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		out.println(message);

	}

	/**
	 * Alle aktuell registrierten clients(Roboter) werden an den �ber socket
	 * verbundenen client gesendet
	 * 
	 * @param socket
	 * @throws IOException
	 */
	private synchronized void handleServiceDiscoveryAll(Socket socket) {
		// System.out.println("handleServiceDiscovery");

		PrintWriter out = null;

		StringWriter writer;

		HashSet<String> set = new HashSet<String>();

		for (int i = 0; i < offeredServices.length; i++) {

			if (offeredServices[i] != null) {
				if (offeredServices[i].getRobotName().equals("gui")) {

				} else {
					set.add(offeredServices[i].getRobotName());
				}

			} else {
				break;
			}

		}

		String[] connectedRobots;
		connectedRobots = set.toArray(new String[set.size()]);

		for (int i = 0; i < connectedRobots.length; i++) {
			RegisterMessage serviceDiscoveryMessage = new RegisterMessage();
			serviceDiscoveryMessage.name = connectedRobots[i];
			serviceDiscoveryMessage.service1 = null;
			serviceDiscoveryMessage.service2 = null;
			serviceDiscoveryMessage.service3 = null;
			serviceDiscoveryMessage.portNr = 0;

			writer = marshallServiceDiscovery(serviceDiscoveryMessage);

			String message = writer.toString();

			// System.out.println("Nachricht an Client:" + message);

			// System.out.println(message.length());

			try {
				out = new PrintWriter(socket.getOutputStream(), true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			out.println(message);

			// try {
			// out = new PrintWriter(socket.getOutputStream(), true);
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
			//
			// out.println(writer.toString());
		}

	}

	/**
	 * Verarbeiten einer eingehenden Nachricht vom Service-Port, sie wird an den
	 * entpsrechend registrierten client weitergeleitet.
	 * 
	 * @param sc
	 * @throws Exception
	 */
	private synchronized void processInputService(Socket sc, String inputline) throws Exception {
		// System.out.println("processInputService");

		FunctionParameter functionParameter = unmarshallServiceMessage(inputline);
		if (functionParameter == null) return;

		if (functionParameter.returnValue == 1) {
			for (int i = 0; i < offeredServices.length; i++) {
				if (offeredServices[i] == null || offeredServices[i].getRobotName() == null) continue;
				if (offeredServices[i].getRobotName().equals("gui")) {
					processOutputService(offeredServices[i].getSocket(), functionParameter);
					break;
				}

			}
		} else if (functionParameter.returnValue == 0) {

			for (int i = 0; i < offeredServices.length; i++) {
				if (offeredServices[i] == null || offeredServices[i].getRobotName() == null || functionParameter == null) continue;
				if (offeredServices[i].getRobotName().equals(functionParameter.robotName)) {
					processOutputService(offeredServices[i].getSocket(), functionParameter);
					break;
				}

			}
		}

	}

	/**
	 * 
	 * 
	 * 
	 * @param socket
	 * @param functionParameter
	 * @throws InterruptedException
	 * @throws IOException
	 */
	private synchronized void processOutputService(Socket socket, FunctionParameter functionParameter) {

		PrintWriter out = null;
		StringWriter writer;
		writer = marshallMethodCall(functionParameter);

		String outputline = writer.toString();

		try {
			out = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		out.println(outputline);

	}

	private synchronized StringWriter marshallMethodCall(FunctionParameter functionParameter) {

		StringWriter writer = new StringWriter();

		try {

			// output pretty printed
			jaxbMarshallerFunction.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);

			jaxbMarshallerFunction.marshal(functionParameter, writer);

		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return writer;

	}

	private synchronized StringWriter marshallServiceDiscovery(RegisterMessage serviceDiscoveryMessage) {

		StringWriter writer = new StringWriter();

		try {

			// output pretty printed
			jaxbMarshallerRegister.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);

			jaxbMarshallerRegister.marshal(serviceDiscoveryMessage, writer);

		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return writer;

	}

	public static synchronized RegisterMessage unmarshallRegisterMessage(String XMLinput) {

		// System.out.println("unmarshallRegisterMessage");

		StringReader reader = new StringReader(XMLinput);

		RegisterMessage message = null;
		try {
			message = (RegisterMessage) jaxbUnmarshallerRegister.unmarshal(reader);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return message;

	}

	public static synchronized FunctionParameter unmarshallServiceMessage(String XMLinput){
		if (XMLinput.isEmpty() || XMLinput == null) return null;
		StringReader reader = new StringReader(XMLinput);
		

		FunctionParameter functionParameter = null;
		try {
			functionParameter = (FunctionParameter) jaxbUnmarshallerFunction.unmarshal(reader);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			System.err.println("XML input: " + XMLinput);
			e.printStackTrace();
			
		}

		return functionParameter;

	}

}