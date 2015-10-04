package structure;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;
import android.opengl.GLES30;
import android.text.AndroidCharacter;

public class VertexBuffer {
	public static int BYTES_PER_FLOAT = 4;
	
	private float[] vertices;
	private int bufferHandle;
	private int numberVertexComponents = 3;
	private int usage = GLES20.GL_STATIC_DRAW;
	
	/** 
	 * Useful for updating the vbo
	 */
	private FloatBuffer backingBuffer;
	
	/**
	 * Constructor with number of dimensions for each vertex to be set
	 * @param dimensions
	 */
	public VertexBuffer(int dimensions) {
		bufferHandle = 0;
		numberVertexComponents = dimensions;
	}
	
	public void initialize() {
		int[] buffers = new int[1];
		GLES20.glGenBuffers(1, buffers, 0);
		bufferHandle = buffers[0];
	}
	
	public void bufferData(float[] vertices) {
		this.vertices = vertices;
		FloatBuffer verticesBuffer = ByteBuffer.allocateDirect(vertices.length * BYTES_PER_FLOAT)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		verticesBuffer.put(vertices).position(0);
		
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, bufferHandle);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vertices.length * BYTES_PER_FLOAT, verticesBuffer, this.usage);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);		
		
		backingBuffer = verticesBuffer;
	}

	public int getGLHandle() {
		return bufferHandle;
	}
	
	public void bindBuffer() {
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, bufferHandle);
	}
	
	public FloatBuffer getFloatBuffer() {
		return backingBuffer;
	}

	public void bufferSubData(Buffer buffer) {
		this.bindBuffer();
		GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER, 0, vertices.length * BYTES_PER_FLOAT, buffer);
	}

	public void bufferSubData(Buffer buffer, int count) {
		this.bindBuffer();
		GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER, 0, count * BYTES_PER_FLOAT, buffer);
	}
	
	public void draw() {
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);               
	}
	
	public void delete() {
		int[] buffersToDelete = new int[1];
		buffersToDelete[0] = this.bufferHandle;
		GLES20.glDeleteBuffers(1, buffersToDelete, 0);
	}
}
