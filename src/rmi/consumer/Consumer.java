package rmi.consumer;

import java.net.InetAddress;

import javax.xml.bind.JAXBException;

import org.cads.ev3.gui.ICaDSRobotGUIUpdater;
import org.cads.ev3.gui.swing.CaDSRobotGUISwing;
import org.cads.ev3.rmi.consumer.ICaDSRMIConsumer;


public class Consumer implements ICaDSRMIConsumer{
	
	public static CaDSRobotGUISwing gui;
	
	public static void main(String[] args) throws JAXBException {
		
		//TCPConnection.ipAdress = args[0];
		TCPConnection.ipAdress = "localhost";
		TCPConnection comm = TCPConnection.getInstance();
		
		
		// Start Gui and Stubs/Skeleton
		ICaDSRobotGUIUpdater guiUpdater = new RobotGuiUpdater();
		ConsumerStub rc = new ConsumerStub();
		
		ICaDSRMIConsumer c = new Consumer();
		ConsumerSkeleton skel = ConsumerSkeleton.getInstance();
		//c.register(guiUpdater);
		
		
		gui = new CaDSRobotGUISwing(null, rc, rc, rc, rc);
		gui.startGUIRefresh(1000);
		gui.addService("Fuckable");
		gui.addService("lukas der stricher");
		RegisterService registry = new RegisterService();
		registry.registerAtBroker("gui");
	}

	@Override
	public void register(ICaDSRobotGUIUpdater observer) {
		System.out.println("New Observer");
		observer.addService("Service 1");
		observer.addService("Service 2");
		observer.setChoosenService("Service 2", -1, -1, false);
	}

    @Override
    public void update(String comboBoxText) {
        System.out.println("Combo Box updated " + comboBoxText);
    }
    
    
    
	
}
