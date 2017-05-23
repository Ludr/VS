package Test;

import java.io.*;
import java.net.*;

import rmi.provider.TCPConnection.Receiver;
import rmi.provider.TCPConnection.Sender;

public class EchoServer 


{
	public EchoServer(int portnum1, int portnum2)
	{
		try
		{
			server1 = new ServerSocket(portnum1);
		}
		catch (Exception err)
		{
			System.out.println(err);
		}
		
		try {
			server2 = new ServerSocket(portnum2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		new Thread(new serv(server1)).start();
		new Thread(new serv(server2)).start();
	}

	
	public static void main(String[] args)
	{
		EchoServer s = new EchoServer(8888,8889);
		
	}

	private ServerSocket server1;
	private ServerSocket server2;
	
	
	public class serv extends Thread 
	{
		
		ServerSocket server;

		public serv(ServerSocket server){
			
			this.server =server;
		}
		
		@Override
		public void run() {
		try
		{
			while (true)
			{
				
				Socket client = server.accept();
				BufferedReader r = new BufferedReader(new InputStreamReader(client.getInputStream()));
				PrintWriter w = new PrintWriter(client.getOutputStream(), true);
				w.println("Welcome to the Java EchoServer.  Type 'bye' to close.");
				String line;
				do
				{
					line = r.readLine();
					if ( line != null )
						w.println("Got: "+ line);
				}
				while ( !line.trim().equals("bye") );
				client.close();
			}
		}
		catch (Exception err)
		{
			System.err.println(err);
		}
		}


	}



}

