package rmi.consumer;

import java.io.StringReader;

import javax.xml.bind.*;

import rmi.message.FunctionParameter;

/*
 * 
 * 
 */
public class ConsumerSkeleton extends Thread {

	private static ConsumerSkeleton instance;

	private JAXBContext jaxbContext;
	private Unmarshaller jaxbUnmarshaller;

	private ConsumerSkeleton() throws JAXBException {
		jaxbContext = JAXBContext.newInstance(FunctionParameter.class);

		jaxbUnmarshaller = jaxbContext.createUnmarshaller();

	}

	public static synchronized ConsumerSkeleton getInstance()
			throws JAXBException {
		if (ConsumerSkeleton.instance == null) {
			ConsumerSkeleton.instance = new ConsumerSkeleton();
			new Thread(instance).start();
		}
		return ConsumerSkeleton.instance;
	}

	public void run() {
		while (true) {
			try {

				unmarshall(TCPConnection.getInstance().getIntputQueueService()
						.take());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void unmarshall(String XMLinput) throws Exception {
		StringReader reader = new StringReader(XMLinput);

		System.out.println("skeleton");
		FunctionParameter functionParameter = (FunctionParameter) jaxbUnmarshaller
				.unmarshal(reader);

		switch (functionParameter.functionName) {
		case "openGripper":
			if(functionParameter.robotName.equals(GuiUpdater.getInstance().getSelectedRobot())){
			GuiUpdater.getInstance().openGripper(0);
			}else{
				GuiUpdater.getInstance().getRobotData(functionParameter.robotName).setGrabbed(false);
			}
			break;

		case "closeGripper":
			if(functionParameter.robotName.equals(GuiUpdater.getInstance().getSelectedRobot())){
			GuiUpdater.getInstance().closeGripper(0);
			}else{
				GuiUpdater.getInstance().getRobotData(functionParameter.robotName).setGrabbed(true);
			}
			break;

		case "moveHorizontalToPercent":
			if(functionParameter.robotName.equals(GuiUpdater.getInstance().getSelectedRobot())){
			GuiUpdater.getInstance().moveHorizontalToPercent(0,functionParameter.percent);
			}else{
				GuiUpdater.getInstance().getRobotData(functionParameter.robotName).setHorizontalPercent(functionParameter.percent);
			}
			break;
		case "moveVerticalToPercent":
			if(functionParameter.robotName.equals(GuiUpdater.getInstance().getSelectedRobot())){
			GuiUpdater.getInstance().moveVerticalToPercent(0,functionParameter.percent);
			}else{
			GuiUpdater.getInstance().getRobotData(functionParameter.robotName).setVerticalPercent(functionParameter.percent);
			}
			break;
		}

	}

}
