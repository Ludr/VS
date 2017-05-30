package rmi.consumer;

import javax.swing.SwingUtilities;
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
		ICaDSRobotGUIUpdater robotGuiUpdater = new RobotGuiUpdater();
		ConsumerStub rc = new ConsumerStub();
		GuiUpdater guiUpdater = GuiUpdater.getInstance();
		
		Consumer c = new Consumer();
		ConsumerSkeleton skel = ConsumerSkeleton.getInstance();
		
		//c.register(guiUpdater);
		

		gui = new CaDSRobotGUISwing(c, rc, rc, rc, rc);
		//gui.startGUIRefresh(1000);
		RegisterService registry = new RegisterService();
		registry.registerAtBroker("gui");
	}

	

	@Override
	public void update(String arg0) {
		try {
			GuiUpdater.getInstance().setSelectedRobot(arg0);
			Consumer.gui.setHorizontalProgressbar(GuiUpdater.getInstance().getRobotData(arg0).getHorizontalPercent());
			Consumer.gui.setVerticalProgressbar(GuiUpdater.getInstance().getRobotData(arg0).getVerticalPercent());
			if(GuiUpdater.getInstance().getRobotData(arg0).isGrabbed() == false){
				Consumer.gui.setGripperOpen();
			}else{
				Consumer.gui.setGripperClosed();
			}
			
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}



	@Override
	public void register(ICaDSRobotGUIUpdater arg0) {
		System.out.println("HelloRegister");
		
	}

    
    
    
    
	
}
