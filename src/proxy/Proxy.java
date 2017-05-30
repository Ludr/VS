package proxy;

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

public class Proxy {

	private static Proxy instance;

	private Socket serviceSocketBroker;
	private Socket registrySocketBroker;
	private ServerSocket ServerSocketService;
	private ServerSocket ServerSocketRegistry;
	private Socket serviceSocketProvider;
	private Socket registrySocketProvider;
	private int servicePort = 8888;
	private int registryPort = 8889;

	public static synchronized Proxy getInstance() {
		if (Proxy.instance == null) {
			Proxy.instance = new Proxy();
		}
		return Proxy.instance;
	}

	private Proxy() {

		try {
			serviceSocketBroker = new Socket("172.16.1.2", 8888);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			registrySocketBroker = new Socket("172.16.1.2", 8889);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			ServerSocketService = new ServerSocket(servicePort);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			ServerSocketRegistry = new ServerSocket(registryPort);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		System.out.println("Waiting for Robot-Connection");

		try {
			serviceSocketProvider = ServerSocketService.accept();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Robot connected on port " + servicePort);

		try {
			registrySocketProvider = ServerSocketRegistry.accept();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		System.out.println("Robot connected on port " + registryPort);

		new Thread(new Receiver(serviceSocketBroker, servicePort)).start();
		new Thread(new Receiver(serviceSocketProvider, servicePort)).start();
		new Thread(new Receiver(registrySocketBroker, registryPort)).start();
		new Thread(new Receiver(registrySocketProvider, registryPort)).start();

	}

	public class Receiver extends Thread {

		private Socket socket;
		private int port;

		public Receiver(Socket socket, int port) {
			this.socket = socket;
			this.port = port;

		}

		public void run() {
			while (!isInterrupted()) {
				try {
					System.out.println("Receiver Reading from Socket");

					PrintWriter outServiceProvider = new PrintWriter(
							serviceSocketProvider.getOutputStream(), true);

					PrintWriter outRegistryProvider = new PrintWriter(
							registrySocketProvider.getOutputStream(), true);

					PrintWriter outServiceBroker = new PrintWriter(
							serviceSocketBroker.getOutputStream(), true);

					PrintWriter outRegistryBroker = new PrintWriter(
							registrySocketBroker.getOutputStream(), true);

					BufferedReader in = new BufferedReader(

					new InputStreamReader(socket.getInputStream()));

					String inputLine = "";

					while ((inputLine = in.readLine()) != null) {
						System.out.println("On port " + port
								+ " proxy receives : " + inputLine);

						if (socket.equals(serviceSocketBroker)) {

							outServiceProvider.println(inputLine);

						} else if (socket.equals(registrySocketBroker)) {

							outRegistryProvider.println(inputLine);

						} else if (socket.equals(serviceSocketProvider)) {

							outServiceBroker.println(inputLine);

						} else if (socket.equals(registrySocketProvider)) {

							outRegistryBroker.println(inputLine);

						} else {
							System.out
									.println("Problem: Sockets not matching!");
						}

					}
					System.out.println("Finished reading from Socket");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}
	public static void main(String[] args) {
		Proxy proxy = Proxy.getInstance();
	}

}
