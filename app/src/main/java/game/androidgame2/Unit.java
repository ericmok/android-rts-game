package game.androidgame2;

import android.util.Log;

public class Unit implements Updateable {
	public KinematicPosition kinematicPosition;
	public KinematicRotation2D kinematicRotation;
	
	/** Physical width **/
	public double width;
	
	/** Physical height **/
	public double height;

	/** 
	 * Type of unit. Expected semantic constants.
	 * Use the type to determine properties like texture 
	**/
	public int type;

	public Unit() {
		kinematicPosition = new KinematicPosition();
		kinematicRotation = new KinematicRotation2D();
		width = 1;
		height = 1;
	}
	
	@Override
	public void update(long deltaTime) {
		kinematicPosition.position.x += deltaTime * 0.001;
	}
	
	/**
	 * Deep copy without allocation.
	 * @param unit
	 */
	public void copy(Unit unit) {
		this.kinematicPosition.copy(unit.kinematicPosition);
		this.kinematicRotation.copy(unit.kinematicRotation);
		this.width = unit.width;
		this.height = unit.height;
	}
}
