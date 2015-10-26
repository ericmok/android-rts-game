package structure;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

/**
 * OpenGL abstraction
 */
public class Graphics {
	private Context context;
	//private SimpleShader simpleShader;
    private SimpleQuadShader simpleQuadShader;
	private SimpleSpriteBatch simpleSpriteBatch;
	private LineShader lineShader;
	private LineBatch lineBatch;
	private TextureLoader textureLoader;
	private boolean isLoaded = false;

    /**
     * Camera matrix
     */
    private float[] cameraMatrix = new float[16];

    /**
     * A copy of camera matrix to use for rendering while camera matrix is being modified
     */
    private float[] cameraMatrixCache = new float[16];

    /**
     * To avoid camera matrix read and write race conditions.
     * If set to true because it is be written to, return a cache.
     */
    private boolean cameraMatrixUnderModification = false;

    /**
     * Synchronization for cameraMatrixUnderModification
     */
    private Object cameraMatrixMutex = new Object();

    /**
     * Use drawLists as a communication bridge between game logic and lower-level renderers.
     * The draw denormalizedLists contain concurrent-safe mechanisms.
     */
	public DrawLists drawLists;
	
	/**
	 * Constructor for SimpleShader, SimpleShaderBatch, TextureLoader
	 * @param context This is needed to load resources, like the shader source and textures correctly
	 */
	public Graphics(Context context) {
		this.context = context;
		//simpleShader = new SimpleShader(context.getResources());
        simpleQuadShader = new SimpleQuadShader(context.getResources());
		lineShader = new LineShader(context.getResources());

		simpleSpriteBatch = new SimpleSpriteBatch(simpleQuadShader);
		lineBatch = new LineBatch(lineShader);
		textureLoader = new TextureLoader(context);

        cameraMatrix = new float[16];
        resetCamera();
        flushCameraModifications();

       // initializeCameraMatrixBuffer();

		drawLists = new DrawLists();
	}
	
	/**
	 * Initializes GL Shaders and reloads / generates textures.<br/>
	 * This should be called everytime the renderer comes from an invalidated state.
	 * For example, if we lose the surface and get it back again.
	 */
	public void load() {
        if (!isLoaded) {
            try {
                simpleQuadShader.initializeResources();
                simpleSpriteBatch.load();
                lineShader.initializeResources();
                lineBatch.load();

                textureLoader.loadAssetsInRoot("Animations");
                textureLoader.loadLetterTextures();

//			textureLoader.loadUnitStates(TROOPS_ASSETS, true);
//			textureLoader.loadTexture(UNIT1_TEXTURE, R.drawable.sprite1, true);

                isLoaded = true;
                Runtime rc = Runtime.getRuntime();
                rc.gc();
            } catch (Exception e) {
                Log.e("Graphics", "Failed to initialize!");
                e.printStackTrace();
            }
        }
    }

    /**
     * If the application loses graphics for some reason, ie. os switch to different app,
     * then call invalidate so that graphics will be loadable again.
     *
     * Possible deprecation due to GLSurfaceView's setPreserveEGLContextOnPause
     */
	public void invalidate() {
        //this.isLoaded = false;
        Runtime rc = Runtime.getRuntime();
		rc.gc();
	}
	
	public Context getContext() { return context; }
	//public SimpleShader getSimpleShader() { return simpleShader; }
	public TextureLoader getTextureLoader() { return textureLoader; }
	public boolean isLoaded() { return isLoaded; }

	/**
	 * Access drawing 2d api
	 * @return
	 */
	public SimpleSpriteBatch getSimpleSpriteBatch() { return this.simpleSpriteBatch; }

	public LineBatch getLineBatch() { return this.lineBatch; }
	
    /**
     * Remember to flush changes
     */
    public void resetCamera() {
        setCameraPositionAndScale(0, 0, 1);
    }

    /**
     * Flushes modifications to the matrix. Used to facilitate syncing.
     */
    public void flushCameraModifications() {
        synchronized (cameraMatrixMutex) {
            System.arraycopy(cameraMatrix, 0, cameraMatrixCache, 0, cameraMatrix.length);
            cameraMatrixUnderModification = false;
        }
    }

    /**
     * Mutate camera matrix using this method. An internal flag will note that the matrix has changed.
     *
     * Flush changes the changes with flushCameraModifications.
     *
     * @param x The position of camera (method will take into account its inversion)
     * @param y The position of camera (method will take into account its inversion)
     * @param scale
     */
    public void setCameraPositionAndScale(float x, float y, float scale) {
        synchronized (cameraMatrixMutex) {
            cameraMatrixUnderModification = true;

            Matrix.setIdentityM(cameraMatrix, 0);
            Matrix.scaleM(cameraMatrix, 0, scale, scale, 1);
            Matrix.translateM(cameraMatrix, 0, -x, -y, -2.0f);
        }
    }

    /**
     * Checks if camera is under modification and returns
     * a cache'd camera matrix if it is, otherwise it returns the current up-to-date camera matrix
     *
     * This prevents read and write race conditions along with game data syncing issues.
     *
     * <strong>Do not modify the matrix returned by this method!
     * Use the setCameraPositionAndScale method instead</strong>
     *
     *
     * @return
     */
    public float[] getCameraMatrix() {
        synchronized (cameraMatrixMutex) {
            if (cameraMatrixUnderModification) {
                return cameraMatrixCache;
            }
            return cameraMatrix;
        }
    }
}
