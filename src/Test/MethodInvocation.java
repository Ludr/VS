package Test;

import java.lang.reflect.Method;

import rmi.message.FunctionParameter;

public class MethodInvocation {

	public static void main(String[] args) {
		FunctionParameter functionParameter = new FunctionParameter();
		functionParameter.functionName = "method2";
		functionParameter.percent = -1;
		try {
		    Class<?> c = MethodInvocation.class;
		    Class[] argTypes = functionParameter.percent == null ? null : new Class[] { int.class };
		    Method method = c.getDeclaredMethod(functionParameter.functionName, argTypes);
	  	    Integer[] mainArgs = functionParameter.percent == null ? null : new Integer[]{functionParameter.percent};
		    System.out.println(method.invoke(null, (Object[])mainArgs));

	        // production code should handle these exceptions more gracefully
		} catch (Exception x) {
		    x.printStackTrace();
		}
	}

	private static int method1(){
		System.out.println("Method1");
		return 0;
	}
	
	private static int method2(int arg){
		System.out.println("Method2");
		return arg;
	}

}
