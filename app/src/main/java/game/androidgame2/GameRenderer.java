package game.androidgame2;


import java.util.List;
import java.util.Stack;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.annotation.SuppressLint;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

import game.androidgame2.TextureLoader.LetterTexture;

public class GameRenderer implements GLSurfaceView.Renderer  {

	private GameRenderer m = this;

    public static float INTIAL_ASPECT_RATIO_VALUE = (1920.0f / 1080);

	private Context context;
	private Game game;
	
	private Stack<float[]> matrixStack;

    private float[] tempMatrix;
	//private float[] cameraMatrix;
	private float[] projectionMatrix;

	public Object drawingMutex = new Object();
	
	private boolean surfaceIsReady = false;

	private long previousTick = 0;
	private long tickDifference = 0;

    private float aspectRatio = INTIAL_ASPECT_RATIO_VALUE;

    private float leftBounds = -INTIAL_ASPECT_RATIO_VALUE;
    private float rightBounds = INTIAL_ASPECT_RATIO_VALUE;
    private float bottomBounds = -1;
    private float topBounds = 1;
    private float near = 1;
    private float far = 1000;
    private float scale = 1.0f;
	
	public GameRenderer(Context parentActivity, Game game) {
		this.context = parentActivity;
		this.game = game;

        tempMatrix = new float[16];
		//cameraMatrix = new float[16];
		projectionMatrix = new float[16];
	}
	
	public Graphics getGraphics() {
		return game.graphics;
	}
	
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {

		GLES20.glClearColor(0.1f, 0.17f, 0.15f, 0.3f);

		game.graphics.load();

	}

    /**
     * Sets up projection matrix
     * @param width
     * @param height
     */
	public void setupViewport(int width, int height) {
		// Set the OpenGL viewport to the same size as the surface.
		GLES20.glViewport(0, 0, width, height);

        /*
         * Landscape and portrait mode will change how big a unit length looks on the screen.
         * The game is tailored for landscape. The dimensions will wind up being the reciprocal
         * of the aspect ratio larger in portrait mode. Have to rescale to normalization.
         *
         * Landscape:
         * Left and right bounds: [-asp, +asp]
         * Bottom and top bounds: [-1, +1]
         *
         * Portrait:
         * Left and right bounds: [-1, +1]
         * Bottom and top bounds: [-asp, +asp]
         */
        aspectRatio = (float) width / height;

        if (aspectRatio > 1.0f) {

            // Landscape
            leftBounds = aspectRatio * -1.0f * scale;
            rightBounds = aspectRatio * 1.0f * scale;

            bottomBounds = -1.0f * scale;
            topBounds = 1.0f * scale;
        }
        else {

            // Portrait
            bottomBounds = aspectRatio * -1.0f * scale;
            topBounds = aspectRatio * 1.0f * scale;

            leftBounds = -1.0f * scale;
            rightBounds = 1.0f * scale;
        }

        near = 1.0f;
        far = 1000.0f;

        Matrix.orthoM(projectionMatrix, 0, leftBounds, rightBounds, bottomBounds, topBounds, near, far);

		// Set up cameraMatrix
		//Matrix.setIdentityM(cameraMatrix, 0);
		//Matrix.translateM(cameraMatrix, 0, 0.0f, 0.0f, -2.0f);

        // TODO: Do this calculation in the shader!
        //Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, cameraMatrix, 0);
	}

    public float getAspectRatio() {
        return aspectRatio;
    }
    public float getLeftBounds() { return leftBounds; }
    public float rightBounds() { return rightBounds; }
    public float getBottomBounds() { return bottomBounds; }
    public float getTopBounds() { return topBounds; }
    public float getNear() { return near; }
    public float getFar() { return far; }
    public float getScale() { return scale; }

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		setupViewport(width, height);
		surfaceIsReady = true;
		game.onSurfaceReady();
	}
	

	@SuppressLint("NewApi")
	@Override
	public void onDrawFrame(GL10 gl) {
		if (!surfaceIsReady)
			return;

		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
	
		long startTick = SystemClock.uptimeMillis();
		tickDifference = startTick - previousTick;
		previousTick = startTick; // Leapfrog previous tick
		
		synchronized(drawingMutex) {

            float[] cameraMatrix = game.graphics.getCameraMatrix();

            // Temporarily rename a matrix
            float[] mvpMatrix = tempMatrix;

            Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, cameraMatrix, 0);

			game.graphics.getSimpleSpriteBatch().beginDrawing();	

			RewriteOnlyArray<DrawList2DItem> sprites = game.graphics.drawLists.regularSprites.swapBuffer();
	
			sprites.resetIterator();
			
			while (sprites.canIterateNext()) {
				
				DrawList2DItem sprite = sprites.getNextIteratorItem();
				game.graphics.getSimpleSpriteBatch().draw2d(mvpMatrix, 
															(float)sprite.position.x, (float)sprite.position.y, 
															sprite.angle, 
															sprite.width, sprite.height,
															game.graphics.getTextureLoader().animations.get(sprite.animationName).
															maxFrameUnderCriteria(sprite.animationProgress).glTexture,
															sprite.color);
				
			}
			
			// Need to buffer this
			List<TemporaryDrawList2DItem> tempSprites = game.graphics.drawLists.temporarySprites;
			for (int i = 0; i < tempSprites.size(); i++) {
				TemporaryDrawList2DItem tempSprite = tempSprites.get(i);
				if (tempSprite.progress.progress > 99) {
					tempSprites.remove(i);
					game.gamePool.temporaryDrawItems.recycleMemory(tempSprite);
					i = i - 1;
					continue;
				}
				tempSprite.progress.update(tickDifference);
				game.graphics.getSimpleSpriteBatch().draw2d(mvpMatrix,
															(float)tempSprite.position.x, (float)tempSprite.position.y,
															tempSprite.angle,
															tempSprite.width, tempSprite.height,
															game.graphics.getTextureLoader().animations.get(tempSprite.animationName).
															maxFrameUnderCriteria((int)tempSprite.progress.progress).glTexture,
															tempSprite.color);
			}
			
			RewriteOnlyArray<TextDrawItem> textDrawItems = game.graphics.drawLists.textDrawItems.swapBuffer();
			textDrawItems.resetIterator();
			
			while (textDrawItems.canIterateNext()) {
				TextDrawItem textDrawItem = textDrawItems.getNextIteratorItem();
				float accumulator = 0;
				for (int i = 0; i < textDrawItem.stringBuilder.length(); i++) {
					Character characterToDraw = textDrawItem.stringBuilder.charAt(i);
					LetterTexture texture = game.graphics.getTextureLoader().letterTextures.get(characterToDraw);
					game.graphics.getSimpleSpriteBatch().draw2d(mvpMatrix,
							(float) (accumulator + textDrawItem.position.x), (float)textDrawItem.position.y,
							(float)textDrawItem.angle,
							textDrawItem.height * texture.widthRatio, textDrawItem.height,
							texture.glTexture,
							textDrawItem.color);	
					accumulator += texture.widthRatio * 0.1f;
				}
			}

			game.graphics.getSimpleSpriteBatch().endDrawing();
						
		}
	}
	
	/**
	 * Called when MainActivity/Game pauses/stops 
	 */
	public void onSurfaceLost() {
		Log.i("SURFACELOST", "SurfaceLost");
		surfaceIsReady = false;
		game.graphics.invalidate();
	}
	
	
}
