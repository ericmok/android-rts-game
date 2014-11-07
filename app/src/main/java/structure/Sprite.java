package structure;

public class Sprite {
	
	public Quad quad;
	public int glTexture;
	
	public Sprite(int x, int y, int width, int height, int glTexture) {
		quad = new Quad(x, y, width, height);
		this.glTexture = glTexture;
		
		this.initializeBuffers();
	}
	
	/** NOT IMPLEMENTED **/
	public void setPosition(double x, double y) {
		
	}
	
	public void initializeBuffers() {
		quad.initializeBuffers();
	}
	
}
