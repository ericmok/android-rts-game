package structure;

import java.nio.FloatBuffer;

import android.graphics.Color;
import android.opengl.Matrix;

/**
 * A 2D quad with single z value. 
 * If I omit z values...Crap.
 * @author Eric
 */
public class Quad {
	private float x;
	private float y;
	private float z;
	
	private float width;
	private float height;
	
	public VertexBuffer vertexBuffer;
	public VertexBuffer colorBuffer;
	public VertexBuffer textureBuffer;

	private Vector3[] vertices;
	
	private float[] colorData = {
//			0.0f, 1.0f, 1.0f, 1.0f,
//			0.0f, 1.0f, 1.0f, 1.0f,
//			0.0f, 1.0f, 1.0f, 1.0f,
//			0.0f, 1.0f, 1.0f, 1.0f
			1.0f, 1.0f, 1.0f, 1.0f,
			1.0f, 1.0f, 1.0f, 1.0f,
			1.0f, 1.0f, 1.0f, 1.0f,
			1.0f, 1.0f, 1.0f, 1.0f
	};

	
	private float[] textureData = {
			0.0f, 1.0f,
			1.0f, 1.0f,
			0.0f, 0.0f,
			1.0f, 0.0f
	};
	
	public Quad() {
		this(0, 0, 1, 1);
	}
		
	public Quad(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.z = 0;
		this.width = width;
		this.height = height;
		
		vertices = new Vector3[4];
		for (int i = 0; i < 4; i++) {
			vertices[i] = new Vector3();
		}
		
		setupVertices(x, y, 0, width, height);
	}
	
	public Quad(float x, float y, float z, float width, float height) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.width = width;
		this.height = height;
		
		vertices = new Vector3[4];
		for (int i = 0; i < 4; i++) {
			vertices[i] = new Vector3();
		}
		
		setupVertices(x, y, z, width, height);
	}
	
	/**
	 * Sets 4 vertices of quad's positions
	 * @param tx
	 * @param ty
	 * @param width
	 * @param height
	 */
	public void setupVertices(float tx, float ty, float z, float width, float height) {
		vertices[0].x = tx - (width/2);
		vertices[0].y = ty - (height/2);
		vertices[0].z = z;
		
		vertices[1].x = tx + (width/2);
		vertices[1].y = ty - (width/2);
		vertices[1].z = z;
		
		vertices[2].x = tx - (width/2);
		vertices[2].y = ty + (height/2);
		vertices[2].z = z;
						
		vertices[3].x = tx + (width/2);
		vertices[3].y = ty + (height/2);
		vertices[3].z = z;
	}

	
	
	/**
	 * Shallow copies vectors. 
	 * @param p0
	 * @param p1
	 * @param p2
	 * @param p3
	 */
	public void setVertices(Vector3 p0, Vector3 p1, Vector3 p2, Vector3 p3) {
		vertices[0] = p0;
		vertices[1] = p1;
		vertices[2] = p2;
		vertices[3] = p3;
	}
	
	/**
	 * Packs 4 vertices of quad into a packed float array
	 * @param p0
	 * @param p1
	 * @param p2
	 * @param p3
	 * @return
	 */
	public float[] packVertices(Vector3 p0, Vector3 p1, Vector3 p2, Vector3 p3) {
		return new float[] {
			(float)p0.x, (float)p0.y, (float)p0.z,
			(float)p1.x, (float)p1.y, (float)p1.z,
			(float)p2.x, (float)p2.y, (float)p2.z,
			(float)p3.x, (float)p3.y, (float)p3.z
		};
	}
	
	/**
	 * This will remake a new quad but to translated coordinates! 
	 * Use transform instead for composed transformations.
	 * @param x2
	 * @param y2
	 */
	public void translate(float x2, float y2) {
		this.setupVertices(x2, y2, this.z, width, height);
		vertexBuffer.bufferData(
				packVertices(vertices[0], vertices[1], vertices[2], vertices[3]) );
	}
	
	public void transform(float[] matrix) {
		float[] temp = new float[4]; // EVIL
		for (int i = 0; i < 4; i++) {
			Matrix.multiplyMV(temp, 0, matrix, 0, vertices[i].asFloatArray(), 0);
			vertices[i].set(temp);
		}
		vertexBuffer.bufferData(
				packVertices(vertices[0], vertices[1], vertices[2], vertices[3]) );
	}
	
	/**
	 * Buffers quad vertices into VBO
	 */
	public void bufferData() {
		vertexBuffer.bufferData( 
				packVertices(vertices[0], vertices[1], vertices[2], vertices[3]) );
	}

	
	/**
	 * Buffers vertices with the color VBO instead of the vertex VBO
	 */
	public void reBufferColorData() {
		FloatBuffer buffer = colorBuffer.getFloatBuffer();
		buffer.put(colorData, 0, colorData.length);
		buffer.flip();
		colorBuffer.bufferSubData(buffer);
	}
	
	/**
	 * Sets the color of the color vertices of the quad.<br/>
	 * You should call <strong>bufferColorData()</strong> to update the associated VBOs
	 * @param color
	 */
	public void setColor(int color) {
		for (int i = 0; i < 4; i++) {
			colorData[i * 4 + 0] = (float)Color.red(color) / 255;
			colorData[i * 4 + 1] = (float)Color.green(color) / 255;
			colorData[i * 4 + 2] = (float)Color.blue(color) / 255;
			colorData[i * 4 + 3] = (float)Color.alpha(color) / 255;
		}
	}
	
	/**
	 * Preconditions: Must have GL context
	 */
	public void initializeBuffers() {
		vertexBuffer = new VertexBuffer(3);
		vertexBuffer.initialize();
		vertexBuffer.bufferData( 
				packVertices(vertices[0], vertices[1], vertices[2], vertices[3]) );
		
		colorBuffer = new VertexBuffer(4);
		colorBuffer.initialize();
		colorBuffer.bufferData(colorData);
		
		textureBuffer = new VertexBuffer(2);
		textureBuffer.initialize();
		textureBuffer.bufferData(textureData);
	}

}
