package structure;

/**
 * Data structure for a 2d sprite.
 *
 */
public class Game2DSprite {

	public Vector3 position;
	public float rotationAngle;
	
	/** Physical width **/
	public float width;
	
	/** Physical height **/
	public float height;

	/** 
	 * Type of unit. Expected semantic constants.
	 * Use the type to determine properties like texture 
	**/
	public int type;

	public Game2DSprite() {
		position = new Vector3();
		rotationAngle = 0;
		width = 1;
		height = 1;
	}
			
	/**
	 * Deep copy without allocation.
	 * @param unit
	 */
	public void copy(Game2DSprite sprite) {
		this.position.copy(sprite.position);
		this.width = sprite.width;
		this.height = sprite.height;
		this.type = sprite.type;
	}

}
