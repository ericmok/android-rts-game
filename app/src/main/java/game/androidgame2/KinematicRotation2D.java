package game.androidgame2;

public class KinematicRotation2D {
	public Orientation orientation;
	public double angularVelocity;
	public double angularAcceleration;
	
	public KinematicRotation2D() {
		orientation = new Orientation();
		angularVelocity = 0;
		angularAcceleration = 0;
	}
	
	/**
	 * Deep copy without allocation.
	 * @param rot
	 */
	public void copy(KinematicRotation2D rot) {
		this.orientation.copy( rot.orientation );
		this.angularVelocity = rot.angularVelocity;
		this.angularAcceleration = rot.angularAcceleration;
	}
}
