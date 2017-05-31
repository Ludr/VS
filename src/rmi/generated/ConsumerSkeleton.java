// package
package rmi.generated;

import java.io.StringReader;
import java.lang.reflect.Method;

import javax.xml.bind.*;

import rmi.message.FunctionParameter;

import rmi.consumer.*;


/*
 *
 *
 */
public class ConsumerSkeleton extends Thread {

	private FunctionParameter functionParameter;
	private JAXBContext jaxbContext;
	private Unmarshaller jaxbUnmarshaller;

	public FunctionParameter getFunctionParameter() {
		return functionParameter;
	}

	// Singleton
	private static ConsumerSkeleton instance;

private ConsumerSkeleton() {
  try {
    jaxbContext = JAXBContext.newInstance(FunctionParameter.class);
    jaxbUnmarshaller = jaxbContext.createUnmarshaller();
  } catch (Exception e) {
    e.printStackTrace();
  }

}

public static synchronized ConsumerSkeleton getInstance() {
  if (instance == null) {
    instance = new ConsumerSkeleton();
    new Thread(instance).start();
  }
  return instance;
}


	public void run() {
		while (true) {
			try {
				unmarshall(TCPConnection.getInstance().getIntputQueueService().take());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public synchronized void unmarshall(String XMLinput) {
		StringReader reader = new StringReader(XMLinput);

		System.out.println("skeleton");
		try {
			functionParameter = (FunctionParameter) jaxbUnmarshaller.unmarshal(reader);

			Class<?> c = GuiUpdater.class;
			Class[] argTypes = functionParameter.percent == null ? null : new Class[] { int.class };
			Method method = c.getDeclaredMethod(functionParameter.functionName, argTypes);
			Integer[] mainArgs = functionParameter.percent == null ? null : new Integer[] { functionParameter.percent };
			method.invoke(GuiUpdater.getInstance(), (Object[]) mainArgs);

			// production code should handle these exceptions more gracefully
		} catch (Exception x) {
			x.printStackTrace();
		}
	}
}
