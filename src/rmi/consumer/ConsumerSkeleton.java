package rmi.consumer;

import java.io.StringReader;
import java.lang.reflect.Method;

import javax.xml.bind.*;

import Test.MethodInvocation;
import rmi.message.FunctionParameter;

/*
 * 
 * 
 */
public class ConsumerSkeleton extends Thread {

	private static ConsumerSkeleton instance;

	private JAXBContext jaxbContext;
	private Unmarshaller jaxbUnmarshaller;

	private FunctionParameter functionParameter;

	public FunctionParameter getFunctionParameter() {
		return functionParameter;
	}

	private ConsumerSkeleton() {
		try {
			jaxbContext = JAXBContext.newInstance(FunctionParameter.class);
			jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	public static synchronized ConsumerSkeleton getInstance() {
		if (ConsumerSkeleton.instance == null) {
			ConsumerSkeleton.instance = new ConsumerSkeleton();
			new Thread(instance).start();
		}
		return ConsumerSkeleton.instance;
	}

	public void run() {
		while (true) {
			try {

				unmarshall(TCPConnection.getInstance().getIntputQueueService().take());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void unmarshall(String XMLinput) {
		StringReader reader = new StringReader(XMLinput);

		System.out.println("skeleton");
		try {
			functionParameter = (FunctionParameter) jaxbUnmarshaller.unmarshal(reader);

			Class<?> c = MethodInvocation.class;
			Class[] argTypes = functionParameter.percent == null ? null : new Class[] { Integer.class };
			Method method = c.getDeclaredMethod(functionParameter.functionName, argTypes);
			Integer[] mainArgs = functionParameter.percent == null ? null : new Integer[] { functionParameter.percent };
			method.invoke(GuiUpdater.getInstance(), (Object[]) mainArgs);

			// production code should handle these exceptions more gracefully
		} catch (Exception x) {
			x.printStackTrace();
		}

		// switch (functionParameter.functionName) {
		// case "openGripper":
		// if(functionParameter.robotName.equals(GuiUpdater.getInstance().getSelectedRobot())){
		// GuiUpdater.getInstance().openGripper();
		// }else{
		// GuiUpdater.getInstance().getRobotData(functionParameter.robotName).setGrabbed(false);
		// }
		// break;
		//
		// case "closeGripper":
		// if(functionParameter.robotName.equals(GuiUpdater.getInstance().getSelectedRobot())){
		// GuiUpdater.getInstance().closeGripper();
		// }else{
		// GuiUpdater.getInstance().getRobotData(functionParameter.robotName).setGrabbed(true);
		// }
		// break;
		//
		// case "moveHorizontalToPercent":
		// if(functionParameter.robotName.equals(GuiUpdater.getInstance().getSelectedRobot())){
		// GuiUpdater.getInstance().moveHorizontalToPercent(functionParameter.percent);
		// }else{
		// GuiUpdater.getInstance().getRobotData(functionParameter.robotName).setHorizontalPercent(functionParameter.percent);
		// }
		// break;
		// case "moveVerticalToPercent":
		// if(functionParameter.robotName.equals(GuiUpdater.getInstance().getSelectedRobot())){
		// GuiUpdater.getInstance().moveVerticalToPercent(functionParameter.percent);
		// }else{
		// GuiUpdater.getInstance().getRobotData(functionParameter.robotName).setVerticalPercent(functionParameter.percent);
		// }
		// break;
		// }

	}

}
