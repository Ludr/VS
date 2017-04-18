package rmi.provider;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class TCPConnection {

	private static TCPConnection instance;

	private ServerSocket socket;
	private Socket client;

	private BlockingQueue<String> outputQueue;
	private BlockingQueue<String> intputQueue;

	/**
	 * Read data from socket
	 * 
	 * @return
	 */
	public BlockingQueue<String> getIntputQueue() {
		return intputQueue;
	}

	/**
	 * Write data to socket
	 * 
	 * @return
	 */
	public BlockingQueue<String> getOutputQueue() {
		return outputQueue;
	}

	private TCPConnection() {

		outputQueue = new LinkedBlockingDeque<>();
		intputQueue = new LinkedBlockingDeque<>();

		try {
			socket = new ServerSocket(8888);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try
		{
		  client = socket.accept();
		  // ...
		}
		catch ( InterruptedIOException e)
		{
		  System.err.println( "Timeout nach einer Minute!" );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		new Thread(new Sender()).start();
		new Thread(new Receiver()).start();
		ProviderSkeleton providerSkeleton = ProviderSkeleton.getInstance();
	}

	public static synchronized TCPConnection getInstance() {
		if (TCPConnection.instance == null) {
			TCPConnection.instance = new TCPConnection();
		}
		return TCPConnection.instance;
	}

	public class Sender extends Thread {

		PrintWriter out;
		
		public Sender() {
			try {
				out = new PrintWriter(client.getOutputStream(), true);
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
		
		public void run() {
			String str = "";
			while (!isInterrupted()) {
				try {
					str = outputQueue.take();
					out.println(str);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public class Receiver extends Thread {

		public void run() {
			while (!isInterrupted()) {
				try {
					System.out.println("Receiver Reading from Socket");
					BufferedReader in = new BufferedReader(
							new InputStreamReader(client.getInputStream()));
					String inputLine = "";
					 while ((inputLine = in.readLine()) != null) {
						    intputQueue.put(inputLine);
					        System.out.println(inputLine);
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
