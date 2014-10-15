package tenth.system;

public class Constants {
	public static final float UNIT_RADIUS = 0.065f;
	public static final float UNIT_INNER_RADIUS = 0.06f;
	
	public static final float VELOCITY_DRAG = 0.7f;
	
	/**
	 * A unit length divided by as many milliseconds it takes to traverse it
	 */
	public static final float MAX_MOVE_SPEED = UNIT_RADIUS / 314; // 0.007
	public static final float MAX_FORCE = 0.0005f;
	public static final float TURNING_RATE = 2.0f;
	
	private Constants singleton = null;;
	
	public static final String EVENT_DEAD = "DEAD";
	
	private Constants() {
	}
	
	public Constants get() {
		if (singleton == null) {
			singleton = new Constants();
			return singleton;
		}
		else {
			return singleton;
		}
	}
}
