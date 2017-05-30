package broker;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.bind.JAXBException;

public class MultiThreadBroker implements Runnable{

	
	
	
    protected int          serverPort;
    protected ServerSocket serverSocket = null;
    protected boolean      isStopped    = false;
    protected Thread       runningThread= null;
    protected ExecutorService threadPool =
        Executors.newFixedThreadPool(10);

    public MultiThreadBroker(int port){
        this.serverPort = port;
    }

    public void run(){
        synchronized(this){
            this.runningThread = Thread.currentThread();
        }
        openServerSocket(serverPort);
        
        System.out.println("Listening on port "+serverPort);
        while(! isStopped()){
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
                System.out.println("New connection on port "+serverPort);
            } catch (IOException e) {
                if(isStopped()) {
                    System.out.println("Server Stopped.") ;
                    break;
                }
                throw new RuntimeException(
                    "Error accepting client connection", e);
            }
            try {
				
            	if(serverPort == 8888){
            	this.threadPool.execute(
				    new WorkerRunnable(clientSocket,"service"));
            	}else if (serverPort == 8889){
            		this.threadPool.execute(
        				    new WorkerRunnable(clientSocket,"registry"));
            	}
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        this.threadPool.shutdown();
        System.out.println("Server Stopped.") ;
    }


    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop(){
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    private void openServerSocket(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
            
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port "+port, e);
        }
    }
}