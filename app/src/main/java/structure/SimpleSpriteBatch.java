package structure;

import android.opengl.GLES20;
import android.opengl.Matrix;

/**
 * Uses SimpleShader to draw sprites in batch.
 * Sprites are made up of a quad, as opposed to an arbitrary polygon.
 * Instead of making a new quad, and allocating new matrices for each sprite,
 * we reuse the same ones. 
 * 
 * 
 * @author eric
 *
 */
public class SimpleSpriteBatch {
	
	public Quad reusableQuad;
	
	// 4 coloring theorem. 4 Matrices to work with. Not to be allocated at runtime to save garbage collection cycles.
	private float[] tempMatrix;
	private float[] tempMatrix2;
    private float[] tempMatrix3;
    private float[] tempMatrix4;
	
	/**
	 * The shader to draw the batch
	 */
	//public SimpleShader simpleShader;
    public SimpleQuadShader simpleQuadShader;
	
	public SimpleSpriteBatch(SimpleQuadShader simpleShader) {
		reusableQuad = new Quad();
		this.simpleQuadShader = simpleShader;

		tempMatrix = new float[16];
		tempMatrix2 = new float[16];
		tempMatrix3 = new float[16];
		tempMatrix4 = new float[16];
	}
	
	public void load() {
		reusableQuad.initializeBuffers();
	}
		
	public void beginDrawing() {
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		GLES20.glEnable(GLES20.GL_BLEND);

		simpleQuadShader.setVertexAttributePointers(reusableQuad.vertexBuffer, reusableQuad.textureBuffer);
	}
	
	/** 
	 * Expand the square quad into the rectangle with that width and height<br/>
	 * Preserve the x and y translation after the scaling <br/>
	 * <strong>Postconditions:</strong> Uses up tempMatrices
	 * 
	 * @param x position x
	 * @param y position y
	 * @param width makes the square quad to this width
	 * @param height makes the square quad to this height
	 * @return
	 */
	private float[] setScaleQuadTempMatrix(float x, float y, float z, float width, float height) {
		Matrix.setIdentityM(tempMatrix, 0);
		Matrix.setIdentityM(tempMatrix2, 0);
		
		Matrix.scaleM(tempMatrix, 0, width, height, 1);
		
		Matrix.translateM(tempMatrix2, 0, x / width, y / height, z);

		Matrix.multiplyMM(tempMatrix, 0, tempMatrix, 0, tempMatrix2, 0);
				
		return tempMatrix;
	}

	int previousGlTexture = -1;

	/**
	 * Set the texture and texture coords for the internally shared quad.
	 * Texture bindings are cached for the next call using glTexture as the key.
	 *
	 * @param glTexture
	 * @return
	 */
	public SimpleSpriteBatch setTextureParams(int glTexture) {
		if (glTexture != previousGlTexture) {
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, glTexture);

			simpleQuadShader.setTextureVertexAttributePointer(reusableQuad.textureBuffer.getGLHandle());

			previousGlTexture = glTexture;
		}

		return this;
	}

	/**
	 * Change texture and also texture coords.
	 * Texture bindings are cached for the next call using glTexture as the key.
	 *
	 * @param glTexture
	 * @param glTexCoordsBuffer
	 * @return
	 */
	public SimpleSpriteBatch setTextureParams(int glTexture, int glTexCoordsBuffer) {
		if (glTexture != previousGlTexture) {
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, glTexture);

			simpleQuadShader.setTextureVertexAttributePointer(glTexCoordsBuffer);
			previousGlTexture = glTexture;
		}

		return this;
	}

	public SimpleSpriteBatch setQuadParams(float[] projectionMatrix, float x, float y, float z, float angle, float width, float height, int color) {
		simpleQuadShader.setUniforms(projectionMatrix, width, height, angle, x, y, color);
		return this;
	}

    /**
	 * Draw the internal quad. Make sure to <strong>setTextureParams</strong> and <strong>setQuadParams</strong> first.
     */
    public void draw2d() {
		simpleQuadShader.drawArrays(GLES20.GL_TRIANGLE_STRIP, 4);
    }

	public void endDrawing() {
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
		GLES20.glDisable(GLES20.GL_BLEND);
	}
}
