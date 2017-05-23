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

public class GuiUpdater extends Thread implements IIDLCaDSEV3RMIMoveGripper, IIDLCaDSEV3RMIMoveHorizontal, IIDLCaDSEV3RMIMoveVertical,
		IIDLCaDSEV3RMIUltraSonic  {

	private static GuiUpdater instance;
	
	int maxRobots = 20;
	
	Unmarshaller jaxbUnmarshaller;
	JAXBContext jaxbContext;
	
	public String[] connectedRobots = new String[maxRobots];
	
	public static synchronized GuiUpdater getInstance() throws JAXBException{
		if (GuiUpdater.instance == null) {
			GuiUpdater.instance = new GuiUpdater();
			new Thread(instance).start();
		}
		return GuiUpdater.instance;
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
	
	private GuiUpdater() throws JAXBException {
		
		
			jaxbContext = JAXBContext.newInstance(RegisterMessage.class);
			jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		
		
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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getCurrentHorizontalPercent() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int moveHorizontalToPercent(int arg0, int arg1) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int stop(int arg0) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int closeGripper(int arg0) throws Exception {
		// TODO Auto-generated method stub
		Consumer.gui.setGripperClosed();
		return 0;
	}

	@Override
	public int isGripperClosed() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int openGripper(int arg0) throws Exception {
		// TODO Auto-generated method stub
		Consumer.gui.setGripperOpen();
		return 0;
	}
	
	
	private void unmarshall(String XMLinput) throws JAXBException{
		
		StringReader reader = new StringReader(XMLinput);
		
		RegisterMessage newRobot = (RegisterMessage) jaxbUnmarshaller.unmarshal(reader);
		
		for (int i = 0; i < connectedRobots.length; i++) {
			
			if(connectedRobots[i] == null){
				connectedRobots[i] = newRobot.name;
			}
		else if(connectedRobots[i] == newRobot.name){
				System.out.println("Duplicate Robot received");
			}
				
		}
		
	}
}


