package structure;

public class KinematicPosition {
	public Vector3 position;
	public Vector3 velocity;
	public Vector3 acceleration;
	
	public double velocityCoefficient;	
	public double accelerationCoefficient;
	
	public KinematicPosition() {
		position = new Vector3();
		velocity = new Vector3();
		acceleration = new Vector3();
		
		velocityCoefficient = 1.0f;
		accelerationCoefficient = 0;
	}
	
	/**
	 * Deep copy without allocation.
	 */
	public void copy(KinematicPosition kp) {
		this.position.copy(kp.position);
		this.velocity.copy(kp.velocity);
		this.acceleration.copy(kp.acceleration);
		this.velocityCoefficient = kp.velocityCoefficient;
		this.accelerationCoefficient = kp.accelerationCoefficient;
	}
}
