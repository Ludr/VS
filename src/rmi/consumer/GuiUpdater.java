package rmi.consumer;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.cads.ev3.rmi.generated.cadSRMIInterface.IIDLCaDSEV3RMIMoveGripper;
import org.cads.ev3.rmi.generated.cadSRMIInterface.IIDLCaDSEV3RMIMoveHorizontal;
import org.cads.ev3.rmi.generated.cadSRMIInterface.IIDLCaDSEV3RMIMoveVertical;
import org.cads.ev3.rmi.generated.cadSRMIInterface.IIDLCaDSEV3RMIUltraSonic;

import rmi.message.FunctionParameter;
import rmi.message.RegisterMessage;

public class GuiUpdater extends Thread {

	private static GuiUpdater instance;

	static int maxRobots = 20;

	private String selectedRobot;
	
	private rmi.generated.ConsumerSkeleton skeleton = rmi.generated.ConsumerSkeleton.getInstance();

	Unmarshaller jaxbUnmarshaller;
	JAXBContext jaxbContext;

	public static connectedRobot[] connectedRobots = new connectedRobot[maxRobots];

	public static synchronized GuiUpdater getInstance() {
		if (GuiUpdater.instance == null) {
			GuiUpdater.instance = new GuiUpdater();
			new Thread(instance).start();
		}
		return GuiUpdater.instance;
	}
	
	private GuiUpdater() {

		try {
			jaxbContext = JAXBContext.newInstance(RegisterMessage.class);
			jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		RobotGuiUpdater robotGuiUpdater = new RobotGuiUpdater();

	}

	public void run() {
		while (true) {
			try {

				unmarshall(TCPConnection.getInstance().getIntputQueueRegistry().take());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public int moveVerticalToPercent(int percent) {
		Consumer.gui.setVerticalProgressbar(percent);
		FunctionParameter parameter = skeleton.getFunctionParameter();
		getRobotData(parameter.robotName).setVerticalPercent(percent);
		return 0;
	}

	public int moveHorizontalToPercent(int percent) {
		Consumer.gui.setHorizontalProgressbar(percent);
		FunctionParameter parameter = skeleton.getFunctionParameter();
		getRobotData(parameter.robotName).setHorizontalPercent(percent);;
		return 0;
	}

	public int closeGripper(){
		Consumer.gui.setGripperClosed();
		FunctionParameter parameter = skeleton.getFunctionParameter();
		getRobotData(parameter.robotName).setGrabbed(true);
		return 0;
	}


	public int openGripper() {
		Consumer.gui.setGripperOpen();
		FunctionParameter parameter = skeleton.getFunctionParameter();
		System.out.println();
		getRobotData(parameter.robotName).setGrabbed(false);
		return 0;
	}

	public void unmarshall(String XMLinput) {

		StringReader reader = new StringReader(XMLinput);

		RegisterMessage newRobot = null;
		try {
			newRobot = (RegisterMessage) jaxbUnmarshaller.unmarshal(reader);
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		System.out.println(newRobot.name);

		// Consumer.gui.setChoosenService(newRobot.name);

		for (int i = 0; i < connectedRobots.length; i++) {

			if (connectedRobots[i] == null) {
				connectedRobots[i] = new connectedRobot(newRobot.name, 0, 0, false);
				System.out.println("Neuer Roboter! " + connectedRobots[i].getRobotName());
				break;

			} else if (connectedRobots[i].getRobotName() == newRobot.name) {
				System.out.println("Duplicate Robot received");
				break;
			}

		}

		Consumer.gui.addService(newRobot.name);
		Consumer.gui.setChoosenService(newRobot.name);

	}

	public synchronized connectedRobot getRobotData(String selectedRobot) {
		for (int i = 0; i < connectedRobots.length; i++) {

			if (connectedRobots[i].getRobotName().equals(selectedRobot)) {
				System.out.println("Return Data: " + connectedRobots[i].getRobotName());
				return connectedRobots[i];
			}
		}
		return null;
	}

	public synchronized String getSelectedRobot() {
		return selectedRobot;
	}

	public synchronized void setSelectedRobot(String selectedRobot) {
		this.selectedRobot = selectedRobot;
	}
}
