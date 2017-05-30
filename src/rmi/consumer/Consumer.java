package rmi.consumer;

import javax.swing.SwingUtilities;
import javax.xml.bind.JAXBException;

import org.cads.ev3.gui.ICaDSRobotGUIUpdater;
import org.cads.ev3.gui.swing.CaDSRobotGUISwing;
import org.cads.ev3.rmi.consumer.ICaDSRMIConsumer;
import org.cads.ev3.rmi.generated.cadSRMIInterface.IIDLCaDSEV3RMIMoveGripper;
import org.cads.ev3.rmi.generated.cadSRMIInterface.IIDLCaDSEV3RMIMoveHorizontal;
import org.cads.ev3.rmi.generated.cadSRMIInterface.IIDLCaDSEV3RMIMoveVertical;
import org.cads.ev3.rmi.generated.cadSRMIInterface.IIDLCaDSEV3RMIUltraSonic;


public class Consumer implements ICaDSRMIConsumer, IIDLCaDSEV3RMIMoveGripper, IIDLCaDSEV3RMIMoveHorizontal,
IIDLCaDSEV3RMIMoveVertical, IIDLCaDSEV3RMIUltraSonic {

	public static CaDSRobotGUISwing gui;

	public static void main(String[] args) throws JAXBException {

		//TCPConnection.ipAdress = args[0];
		TCPConnection.ipAdress = "localhost";
		TCPConnection comm = TCPConnection.getInstance();


		// Start Gui and Stubs/Skeleton
		ICaDSRobotGUIUpdater robotGuiUpdater = new RobotGuiUpdater();
		GuiUpdater guiUpdater = GuiUpdater.getInstance();

		Consumer c = new Consumer();
		ConsumerSkeleton skel = ConsumerSkeleton.getInstance();

		gui = new CaDSRobotGUISwing(c, c, c, c, c);
		//gui.startGUIRefresh(1000);


		//gui = new CaDSRobotGUISwing(c, rc, rc, rc, rc);
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

	@Override
	public int isUltraSonicOccupied() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getCurrentVerticalPercent() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int moveVerticalToPercent(int arg0, int arg1) throws Exception {
		ConsumerStub.getInstance().moveVerticalToPercent(arg1);
		return 0;
	}

	@Override
	public int getCurrentHorizontalPercent() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int moveHorizontalToPercent(int arg0, int arg1) throws Exception {
		ConsumerStub.getInstance().moveHorizontalToPercent(arg1);
		return 0;
	}

	@Override
	public int stop(int arg0) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int closeGripper(int arg0) throws Exception {
		ConsumerStub.getInstance().closeGripper();
		return 0;
	}

	@Override
	public int isGripperClosed() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int openGripper(int arg0) throws Exception {
		ConsumerStub.getInstance().openGripper();
		return 0;
	}




}
