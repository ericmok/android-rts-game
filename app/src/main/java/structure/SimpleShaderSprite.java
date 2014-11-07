package structure;

public class SimpleShaderSprite {
	
	public float x;
	public float y;
	public float z;
	public float width;
	public float height;
	
	public int textureID;
	
	public void reset() {
		x = 0;
		y = 0;
		z = 0;
		width = 0;
		height = 0;
		textureID = 0;
	}
	
	public void copy(SimpleShaderSprite spriteToCopy) {
		x = spriteToCopy.x;
		y = spriteToCopy.y;
		z = spriteToCopy.z;
		width = spriteToCopy.width;
		height = spriteToCopy.height;
		textureID = spriteToCopy.textureID;
	}
	
}
