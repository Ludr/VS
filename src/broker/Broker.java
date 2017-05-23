package broker;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import rmi.message.*;
import rmi.provider.RoboControl;
import rmi.provider.SessionControl;

public class Broker implements Runnable {

	private static Service[] offeredServices = new Service[50];

	private final ByteBuffer buffer = ByteBuffer.allocate(16384);

	private JAXBContext jaxbContext;
	private static Unmarshaller jaxbUnmarshaller;
	private static Marshaller jaxbMarshaller;
	// The port we will listen on
	private int service_port;
	private int register_port;

	public Broker(int service_port, int register_port) {
		this.service_port = service_port;
		this.register_port = register_port;
		new Thread(this).start();
	}

	public void run() {
		try {
			// Instead of creating a ServerSocket,
			// create a ServerSocketChannel
			ServerSocketChannel serviceSocketChannel = ServerSocketChannel
					.open();
			ServerSocketChannel registerSocketChannel = ServerSocketChannel
					.open();

			// Set it to non-blocking, so we can use select
			serviceSocketChannel.configureBlocking(false);
			registerSocketChannel.configureBlocking(false);

			// Get the serviceSocket connected to this channel, and bind it
			// to the listening port
			ServerSocket serviceSocket = serviceSocketChannel.socket();
			InetSocketAddress iservicea = new InetSocketAddress(service_port);
			serviceSocket.bind(iservicea);

			// Get the registerSocket connected to this channel, and bind it
			// to the listening port
			ServerSocket registerSocket = registerSocketChannel.socket();
			InetSocketAddress iregistera = new InetSocketAddress(register_port);
			registerSocket.bind(iregistera);

			// Create a new Selector for selecting
			Selector selector = Selector.open();

			// Register the ServerSocketChannel, so we can
			// listen for incoming connections
			serviceSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
			System.out.println("Listening on port " + service_port);

			registerSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
			System.out.println("Listening on port " + register_port);

			try {
				jaxbContext = JAXBContext.newInstance(FunctionParameter.class);
				jaxbMarshaller = jaxbContext.createMarshaller();
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			while (true) {
				// See if we've had any activity -- either
				// an incoming connection, or incoming data on an
				// existing connection
				int num = selector.select();

				// If we don't have any activity, loop around and wait
				// again
				if (num == 0) {
					continue;
				}

				// Get the keys corresponding to the activity
				// that has been detected, and process them
				// one by one
				Set keys = selector.selectedKeys();
				Iterator<SelectionKey> it = keys.iterator();
				while (it.hasNext()) {
					// Get a key representing one of bits of I/O
					// activity
					SelectionKey key = it.next();

					// What kind of activity is it?
					if ((key.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT) {

						System.out.println("acc");

						// It's an incoming connection.
						// Register this socket with the Selector
						// so we can listen for input on it

						SocketChannel sc = ((ServerSocketChannel) key.channel())
								.accept();
						System.out.println(sc.socket().getLocalPort());

						System.out
								.println("Got connection from " + sc.socket());

						// Make sure to make it non-blocking, so we can
						// use a selector on it.
						sc.configureBlocking(false);

						// Register it with the selector, for reading
						sc.register(selector, SelectionKey.OP_READ);

					} else if ((key.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ) {

						SocketChannel sc = null;

						try {

							// It's incoming data on a connection, so
							// process it
							sc = (SocketChannel) key.channel();

							if (sc.socket().getLocalPort() == 8888) {

								processInputService(sc);

							} else if (sc.socket().getLocalPort() == 8889) {

								processInputRegistry(sc);

							}

							boolean ok = true;
							// If the connection is dead, then remove it
							// from the selector and close it
							if (!ok) {
								key.cancel();

								Socket s = null;
								try {
									s = sc.socket();
									s.close();
								} catch (IOException ie) {
									System.err.println("Error closing socket "
											+ s + ": " + ie);
								}
							}

						} catch (IOException ie) {

							// On exception, remove this channel from the
							// selector
							key.cancel();

							try {
								sc.close();
							} catch (IOException ie2) {
								System.out.println(ie2);
							}

							System.out.println("Closed " + sc);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

				// We remove the selected keys, because we've dealt
				// with them.
				keys.clear();
			}
		} catch (IOException ie) {
			System.err.println(ie);
			ie.printStackTrace();
		}
	}

	private String readLine(ByteBuffer buffer) {
		// Loop over buffer and get all chars until new line appears
		String s = "";

		char newLine = '\n';
		char current = 0;
		while (current != newLine && buffer.hasRemaining()) {
			current = buffer.getChar();
			System.out.println(current);
			s += current;
		}
		System.out.println("Buffer: " + s);
		if (current != newLine) {
			return null;
		}
		buffer.compact();
		buffer.flip();
		return s;
	}

	private void writeLine(String string, ByteBuffer buffer) {
		for (int i = 0; i < string.length(); i++) {
			buffer.putChar(string.charAt(i));
		}
		buffer.putChar('\n');
		buffer.flip();
	}

	private void processInputRegistry(SocketChannel sc) throws Exception {


		sc.read(buffer);
		
		System.out.println(buffer.limit());

		buffer.flip();

		String inputline = readLine(buffer);
		
		if(inputline == null){
			return;
		}

		RegisterMessage message = unmarshallRegisterMessage(inputline);

		if (message.name == "gui") {

			for (int i = 0; i < offeredServices.length; i++) {
				if (offeredServices[i] == null) {
					offeredServices[i] = new Service(message.name, null,
							message.portNr, null, sc.socket());
				}
			}
			handleServiceDiscovery(sc.socket());

		} else {

			for (int i = 0; i < offeredServices.length; i++) {

				if (offeredServices[i] != null) {
					if (offeredServices[i].getRobotName() == message.name) {
						System.out
								.println("Duplicate registration, registration not completed!");
					}
				} else if (offeredServices[i] == null) {
					offeredServices[i] = new Service(message.name,
							message.service1, message.portNr, null, sc.socket());
					if (offeredServices[++i] == null) {
						offeredServices[i] = new Service(message.name,
								message.service2, message.portNr, null,
								sc.socket());
					} else {
						System.out
								.println("Problem while saving services, expected null but wasn't");
					}
					if (offeredServices[++i] == null) {
						offeredServices[i] = new Service(message.name,
								message.service3, message.portNr, null,
								sc.socket());
					} else {
						System.out
								.println("Problem while saving services, expected null but wasn't");
					}
					break;

				}

			}
		}

	}

	/**
	 * Alle aktuell registrierten clients(Roboter) werden an den über socket
	 * verbundenen client gesendet
	 * 
	 * @param socket
	 * @throws IOException
	 */
	private void handleServiceDiscovery(Socket socket) throws IOException {

	
		StringWriter writer;

		HashSet<String> set = new HashSet<String>();

		for (int i = 0; i < offeredServices.length; i++) {

			if (offeredServices[i] != null) {
				set.add(offeredServices[i].getRobotName());
			} else {
				break;
			}

		}

		String[] connectedRobots;
		connectedRobots = set.toArray(new String[set.size()]);

		for (int i = 0; i < connectedRobots.length; i++) {
			RegisterMessage serviceDiscoveryMessage = new RegisterMessage(
					connectedRobots[i], null, null, null, 0);
			writer = marshallServiceDiscovery(serviceDiscoveryMessage);

			String message = writer.toString();

			writeLine(message, buffer);

			socket.getChannel().write(buffer);

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
	private void processInputService(SocketChannel sc) throws Exception {


		sc.read(buffer);

		buffer.flip();

		String inputline = readLine(buffer);
		
		if(inputline == null){
			return;
		}

		FunctionParameter functionParameter = unmarshallServiceMessage(inputline);

		for (int i = 0; i < offeredServices.length; i++) {

			if (offeredServices[i].getRobotName() == functionParameter.robotName) {
				processOutputService(offeredServices[i].getSocket(),
						functionParameter);
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
	private void processOutputService(Socket socket,
			FunctionParameter functionParameter) throws InterruptedException, IOException {

		PrintWriter out = null;
		StringWriter writer = marshallMethodCall(functionParameter);

		String outputline = writer.toString();
		
		writeLine(outputline, buffer);
		
		socket.getChannel().write(buffer);

	}

	private StringWriter marshallMethodCall(FunctionParameter functionParameter)
			throws InterruptedException {

		StringWriter writer = new StringWriter();

		try {

			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);

			jaxbMarshaller.marshal(functionParameter, writer);

		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return writer;

	}

	private StringWriter marshallServiceDiscovery(
			RegisterMessage serviceDiscoveryMessage) {

		StringWriter writer = new StringWriter();

		try {

			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);

			jaxbMarshaller.marshal(serviceDiscoveryMessage, writer);

		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return writer;

	}

	public static RegisterMessage unmarshallRegisterMessage(String XMLinput)
			throws Exception {

		StringReader reader = new StringReader(XMLinput);

		RegisterMessage message = (RegisterMessage) jaxbUnmarshaller
				.unmarshal(reader);

		return message;

	}

	public static FunctionParameter unmarshallServiceMessage(String XMLinput)
			throws Exception {

		StringReader reader = new StringReader(XMLinput);

		FunctionParameter functionParameter = (FunctionParameter) jaxbUnmarshaller
				.unmarshal(reader);

		return functionParameter;

	}

	public static void main(String[] args) {
		new Broker(8888, 8889);
	}

}
