import org.cads.ev3.rmi.generated.cadSRMIInterface.IIDLCaDSEV3RMIMoveHorizontal;

public class testHorizontalMovement implements IIDLCaDSEV3RMIMoveHorizontal{
	
	private int percent = 0;

	@Override
	public int getCurrentHorizontalPercent() throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Hor");
		return percent;
	}

	@Override
	public int moveHorizontalToPercent(int arg0, int arg1) throws Exception {
		System.out.println("TransID: " + arg0 + " - Percent: " + arg1);
		percent = arg1;
		test.gui.setHorizontalProgressbar(arg1);
		return 0;
	}

	@Override
	public int stop(int arg0) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Stop");
		return 0;
	}

}
