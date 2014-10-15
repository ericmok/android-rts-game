package game.androidgame2;

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
	public SimpleShader simpleShader;
	
	public SimpleSpriteBatch(SimpleShader simpleShader) {
		reusableQuad = new Quad();
		this.simpleShader = simpleShader;
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
	
	/**
	 * Does appropriate scaling and rotation and translation for rendering the glTexture on a quad
	 * 
	 * TODO: Use model matrix in shader
	 * 
	 * @param viewProjectionMatrix
	 * @param x Position x of sprite
	 * @param y Position y of sprite 
	 * @param z Layering
	 * @param angle In degrees
	 * @param width Quad width
	 * @param height Quad height
	 * @param glTexture GL handle
	 */
	public void draw2d(float[] viewProjectionMatrix, float x, float y, float angle, float width, float height, int glTexture, int color) {
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, glTexture);
		
		//this.setScaleQuadTempMatrix(x, y, 1, width, height);
		
		float[] scalingMatrix = tempMatrix;
		Matrix.setIdentityM(scalingMatrix, 0);
		Matrix.scaleM(scalingMatrix, 0, width, height, 1);
		
		float[] rotationMatrix = tempMatrix2;
		Matrix.setIdentityM(rotationMatrix, 0);
		
		// TODO: zero degree rotation problem
		Matrix.rotateM(rotationMatrix, 0, angle, 0, 0, 1);
		
		float[] translationMatrix = tempMatrix3;
		Matrix.setIdentityM(translationMatrix, 0);
		Matrix.translateM(translationMatrix, 0, x, y, 0);
		
		float[] finalMatrix = tempMatrix4;
		Matrix.setIdentityM(finalMatrix, 0);
		
		Matrix.multiplyMM(finalMatrix, 0, rotationMatrix, 0, scalingMatrix, 0);
		Matrix.multiplyMM(finalMatrix, 0, translationMatrix, 0, finalMatrix, 0);
		
		Matrix.multiplyMM(finalMatrix, 0, viewProjectionMatrix, 0, finalMatrix, 0);
		
		// TODO: Performance Profiling
		reusableQuad.setColor(color);
		reusableQuad.reBufferColorData();
		
		simpleShader.draw(finalMatrix, 
							reusableQuad.vertexBuffer, 
							reusableQuad.colorBuffer, 
							glTexture, 
							reusableQuad.textureBuffer, 
							GLES20.GL_TRIANGLE_STRIP, 4);
	}
	
	
	/**
	 * <strong>To be deprecated.</strong><br/>
	 * Renders a centered unit quad.<br/>
	 * A matrix is input to affect the rendering of the quad.<br/>
	 * 
	 * @param mvpMatrix Should account for view projection and other model transformations
	 * @param x [deprecated]
	 * @param y [deprecated]
	 * @param z [deprecated]
	 * @param width Width [deprecated]
	 * @param height Height [deprecated]
	 * @param glTexture Texture handle
	 */
	public void draw(float[] mvpMatrix, int glTexture) {
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, glTexture);
		//reusableQuad.setupVertices(x, y, z, width, height);
		//reusableQuad.bufferData();
		
		//this.setScaleQuadTempMatrix(x, y, z, width, height);
		
		//Matrix.multiplyMM(tempMatrix, 0,  mvpMatrix, 0, tempMatrix, 0);
		
		simpleShader.draw(mvpMatrix, 
						reusableQuad.vertexBuffer, 
						reusableQuad.colorBuffer, 
						glTexture, reusableQuad.textureBuffer, 
						GLES20.GL_TRIANGLE_STRIP, 4);
	}
	
	public void endDrawing() {
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
		GLES20.glDisable(GLES20.GL_BLEND);
	}
}
