package game.androidgame2;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

/**
 * OpenGL abstraction
 */
public class Graphics {
	private Context context;
	private SimpleShader simpleShader;
	private SimpleSpriteBatch simpleSpriteBatch;
	private TextureLoader textureLoader;
	private boolean isLoaded = false;
	
	public final static String TROOPS_ASSETS = "Troops";
	public final static String ENEMY_TROOPS_ASSETS = "EnemyTroops";
	public final static String TRIGGER_FIELDS_ASSETS = "TriggerFields";
	public final static String CAPITAL_SHIPS_ASSETS = "CapitalShips";

	public DrawLists drawLists;
	
	/**
	 * Constructor for SimpleShader, SimpleShaderBatch, TextureLoader
	 * @param context This is needed to load resources, like the shader source and textures correctly
	 */
	public Graphics(Context context) {
		this.context = context;
		simpleShader = new SimpleShader(context.getResources());
		simpleSpriteBatch = new SimpleSpriteBatch(simpleShader);
		textureLoader = new TextureLoader(context);
		
		drawLists = new DrawLists();
	}
	
	/**
	 * Initializes GL Shaders and reloads / generates textures.<br/>
	 * This should be called everytime the renderer comes from an invalidated state.
	 * For example, if we lose the surface and get it back again.
	 */
	public void load() {
		try {
			simpleShader.initializeResources();
			simpleSpriteBatch.load();
//			textureLoader.loadTextureFromAssets(TROOPS_ASSETS, true);
//			textureLoader.loadTextureFromAssets(ENEMY_TROOPS_ASSETS, true);
//			textureLoader.loadTextureFromAssets(TRIGGER_FIELDS_ASSETS, true);
//			textureLoader.loadTextureFromAssets(CAPITAL_SHIPS_ASSETS, true);
			
			textureLoader.loadAllAssetsInFolder("Animations", true);
			textureLoader.loadLetterTextures();
			
//			textureLoader.loadTexture(UNIT1_TEXTURE, R.drawable.sprite1, true);
//			textureLoader.loadTexture(UNIT2_TEXTURE, R.drawable.sprite2, true);
//			textureLoader.loadTexture(UNIT3_TEXTURE, R.drawable.sprite3, true);
//			textureLoader.loadTexture(UNIT4_TEXTURE, R.drawable.sprite4, true);
			isLoaded = true;
			Runtime rc = Runtime.getRuntime();
			rc.gc();
		} catch (Exception e) {
			Log.e("Graphics", "Failed to initialize!");
		}
	}

    /**
     * If the application loses graphics for some reason, ie. os switch to different app,
     * then call invalidate so that graphics will be loadable again.
     */
	public void invalidate() {
		this.isLoaded = false;
		Runtime rc = Runtime.getRuntime();
		rc.gc();
	}
	
	public Context getContext() { return context; }
	public SimpleShader getSimpleShader() { return simpleShader; }
	public TextureLoader getTextureLoader() { return textureLoader; }
	public boolean isLoaded() { return isLoaded; }
	
	/**
	 * On path to deprecation
	 */
	public void beginDrawSprite() {
		if (!this.isLoaded) {
			return;
		}
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		GLES20.glEnable(GLES20.GL_BLEND);
	}
	
	/**
	 * On path to deprecation
	 * @param mvpMatrix
	 * @param sprite
	 */
	public void drawSprite(float[] mvpMatrix, Sprite sprite) {
		if (!this.isLoaded) {
			return;
		}
 
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, sprite.glTexture);
		simpleShader.draw(mvpMatrix, 
						sprite.quad.vertexBuffer, 
						sprite.quad.colorBuffer, 
						sprite.glTexture, sprite.quad.textureBuffer, 
						GLES20.GL_TRIANGLE_STRIP, 4);
		
	}
	
	/**
	 * On path to deprecation
	 */
	public void endDrawSprite() {
		if (!this.isLoaded) {
			return;
		}
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
		GLES20.glDisable(GLES20.GL_BLEND);
	}
	
	/**
	 * Access drawing 2d api
	 * @return
	 */
	public SimpleSpriteBatch getSimpleSpriteBatch() { return this.simpleSpriteBatch; }
}
