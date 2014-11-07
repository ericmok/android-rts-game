package structure;

import utils.Orientation;

public class TriggerField {
	public Vector3 source;
	public Vector3 dest;
	
	private Vector3 ret;
	
	public TriggerField() { 
		source = new Vector3();
		dest = new Vector3(1, 0, 0);
		
		ret = new Vector3();
	}
	
	public float getAngle() {
		Vector3.subtract(ret, dest, source);
		return (float) Orientation.getDegrees(ret);
	}
	
	public void getDirection(Vector3 output) {
		Vector3.subtract(output, dest, source);
	}
}
