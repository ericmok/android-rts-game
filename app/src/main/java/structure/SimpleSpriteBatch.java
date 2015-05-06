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

		simpleQuadShader.setupShaderBuffers(reusableQuad.vertexBuffer, reusableQuad.textureBuffer);
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
	 * Texture bindings are cached for the next call.
	 *
	 * @param glTexture
	 * @param x1 Upper Left x
	 * @param y1 Upper Left y
	 * @param x2 Lower Right x
	 * @param y2 Lower Right y
	 * @return
	 */
	public SimpleSpriteBatch setTextureParams(int glTexture, float x1, float y1, float x2, float y2) {
		if (glTexture != previousGlTexture) {
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, glTexture);
			reusableQuad.reBufferTextureData(x1, y1, x2, y2);

			previousGlTexture = glTexture;
		}

		return this;
	}

    /**
	 * Draw a sprite
     */
    public void draw2d(float[] projectionMatrix, float x, float y, float z, float angle, float width, float height, int color) {

//        simpleQuadShader.draw(projectionMatrix, width, height, angle, x, y,
//                reusableQuad.vertexBuffer,
//                color,
//                glTexture,
//                reusableQuad.textureBuffer,
//                GLES20.GL_TRIANGLE_STRIP, 4);

		simpleQuadShader.drawUsingShaderBuffers(projectionMatrix, width, height, angle, x, y, color, GLES20.GL_TRIANGLE_STRIP, 4);
    }

//	/**
//	 * Does appropriate scaling and rotation and translation for rendering the glTexture on a quad
//	 *
//	 * TODO: Use model matrix in shader
//	 *
//	 * @param viewProjectionMatrix
//	 * @param x Position x of sprite
//	 * @param y Position y of sprite
//	 * @param z Layering
//	 * @param angle In degrees
//	 * @param width Quad width
//	 * @param height Quad height
//	 * @param glTexture GL handle
//	 */
//	public void draw2d(float[] viewProjectionMatrix, float x, float y, float angle, float width, float height, int glTexture, int color) {
//        this.draw2dz(viewProjectionMatrix, x, y, 0, angle, width, height, glTexture, color);
//	}
	
	
//	/**
//	 * <strong>To be deprecated.</strong><br/>
//	 * Renders a centered unit quad.<br/>
//	 * A matrix is input to affect the rendering of the quad.<br/>
//	 *
//	 * @param mvpMatrix Should account for view projection and other model transformations
//	 * @param x [deprecated]
//	 * @param y [deprecated]
//	 * @param z [deprecated]
//	 * @param width Width [deprecated]
//	 * @param height Height [deprecated]
//	 * @param glTexture Texture handle
//	 */
//	public void draw(float[] mvpMatrix, int glTexture) {
//		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, glTexture);
//		simpleShader.draw(mvpMatrix,
//						reusableQuad.vertexBuffer,
//						reusableQuad.colorBuffer,
//						glTexture, reusableQuad.textureBuffer,
//						GLES20.GL_TRIANGLE_STRIP, 4);
//	}
	
	public void endDrawing() {
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
		GLES20.glDisable(GLES20.GL_BLEND);
	}
}
