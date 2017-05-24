package rmi.provider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

//TODO 2 Ports aufmachen ( Anmeldung und Service bearbeitung, anpassung der outputqueues!)

public class TCPConnection {

	
	
	private static TCPConnection instance;

	private Socket serviceSocket;
	private Socket registrySocket;

	private BlockingQueue<String> outputQueueRegistry;
	private BlockingQueue<String> intputQueueRegistry;

	private BlockingQueue<String> outputQueueService;
	private BlockingQueue<String> intputQueueService;

	/**
	 * Read data from socket
	 * 
	 * @return
	 */
	public BlockingQueue<String> getIntputQueueRegistry() {
		return intputQueueRegistry;
	}

	/**
	 * Write data to socket
	 * 
	 * @return
	 */
	public BlockingQueue<String> getOutputQueueRegistry() {
		return outputQueueRegistry;
	}

	/**
	 * Read data from socket
	 * 
	 * @return
	 */
	public BlockingQueue<String> getIntputQueueService() {
		return intputQueueService;
	}

	/**
	 * Write data to socket
	 * 
	 * @return
	 */
	public BlockingQueue<String> getOutputQueueService() {
		return outputQueueService;
	}

	public static synchronized TCPConnection getInstance() {
		if (TCPConnection.instance == null) {
			TCPConnection.instance = new TCPConnection();
		}
		return TCPConnection.instance;
	}

	private TCPConnection() {

		outputQueueRegistry = new LinkedBlockingDeque<>();
		intputQueueRegistry = new LinkedBlockingDeque<>();
		outputQueueService = new LinkedBlockingDeque<>();
		intputQueueService = new LinkedBlockingDeque<>();

		try {
			serviceSocket = new Socket("localhost", 8888);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			registrySocket = new Socket("localhost", 8889);
		} catch (IOException e) {
			e.printStackTrace();
		}

		new Thread(new Sender(registrySocket, outputQueueRegistry)).start();
		new Thread(new Receiver(registrySocket, intputQueueRegistry)).start();
		new Thread(new Sender(serviceSocket, outputQueueService)).start();
		new Thread(new Receiver(serviceSocket, intputQueueService)).start();

	}

	public class Sender extends Thread {

		PrintWriter out;
		BlockingQueue<String> outqueue;
		Socket localSocket;

		public Sender(Socket socket, BlockingQueue<String> outputqueue) {
			this.outqueue = outputqueue;
			
			localSocket = socket;
			try {
				out = new PrintWriter(socket.getOutputStream(), true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void run() {
			String str = "";
			while (!isInterrupted()) {
				try {
					str = outqueue.take();
					System.out.println("Provider sends : "+str);
					out.println(str);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public class Receiver extends Thread {

		private Socket socket;
		BlockingQueue<String> inputqueue;

		public Receiver(Socket socket, BlockingQueue<String> inputqueue) {
			this.socket = socket;
			this.inputqueue = inputqueue;
		}

		public void run() {
			while (!isInterrupted()) {
				try {
					System.out.println("Receiver Reading from Socket");
					BufferedReader in = new BufferedReader(
							new InputStreamReader(socket.getInputStream()));
					String inputLine = "";
					while ((inputLine = in.readLine()) != null) {
						TCPConnection.getInstance().intputQueueService.put(inputLine);
						System.out.println("Provider receives : "+inputLine);
					}
					System.out.println("Finished reading from Socket");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

}
