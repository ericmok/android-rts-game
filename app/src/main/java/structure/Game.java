package structure;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;

import org.json.JSONException;

import java.io.IOException;

import art.Animations;
import noteworthyengine.BackgroundUnit;
import noteworthyengine.ButtonSystem;
import noteworthyengine.WrapperEngine;
import noteworthyengine.levels.LevelOne;
import noteworthyengine.levels.LevelTwo;
import noteworthyengine.units.ButtonUnit;
import noteworthyengine.units.CameraUnit;

import noteworthyengine.LoaderUIEngine;
import noteworthyengine.units.MainGameCamera;
import noteworthyframework.BaseEngine;
import noteworthyengine.NoteworthyEngine;
import utils.VoidFunc;

public class Game {

    //public BaseEngine activeEngine;
    //public LoaderUIEngine loaderUIEngine;
    //public NoteworthyEngine noteworthyEngine;

	public WrapperEngine wrapperEngine;

	private Game m = this;
	
	public static final int MAX_UNITS = 2048;
	
	private Context context;
	private GameGLSurfaceView gameGLSurfaceView;
	private GameRenderer gameRenderer;

	private Thread gameThread;
	private GameLoop gameLoop;

    public GameInput gameInput;

    public UIOverlay uiOverlay = new UIOverlay();

    BackgroundUnit backgroundUnit = new BackgroundUnit();

	/**
	 * Stores pre-loaded heap memory allocations of game objects 
	 */
	public GamePool gamePool;
	
	/**
	 * Direct access for speed
	 */
	public Graphics graphics;
	
	public static enum State {
		UNINITIALIZED,
		LOADING,
		LOADED,
		RUNNING,
		STOPPED
	}
	
	private State gameState;
	
	public Game(Activity parentActivity) {
		context = parentActivity;
		gameState = State.UNINITIALIZED;

        gameInput = new GameInput();

		gameGLSurfaceView = new GameGLSurfaceView(context, this);
		gameRenderer = new GameRenderer(parentActivity, this);
		
		gameGLSurfaceView.setRenderer(gameRenderer);
		gameGLSurfaceView.setRenderMode(GameGLSurfaceView.RENDERMODE_CONTINUOUSLY);

		gameLoop = new GameLoop(this);
		gameThread = new Thread(gameLoop);

		gamePool = new GamePool();
		gamePool.allocate();
		
		graphics = new Graphics(parentActivity);

        loadEngine();

		this.setGameState(State.LOADING);
		loadLevel();
		this.setGameState(State.LOADED);
		this.setGameState(State.RUNNING);
	}
	
	public GameGLSurfaceView getGLSurfaceView() {
		return this.gameGLSurfaceView;
	}
	
	public void setGameState(State state) {
		this.gameState = state;
	}
	
	public State getGameState() {
		return gameState;
	}
	
	public Graphics getGraphics() {
		return graphics;
	}
	
	
	//public ArrayList<Updateable> getUpdateList() {
		//return updateList;
	//}

    public void loadEngine() {

		wrapperEngine = new WrapperEngine(this);
		wrapperEngine.initialize();

        //loaderUIEngine = new LoaderUIEngine(this);
        //noteworthyEngine = new NoteworthyEngine(this);

        //loaderUIEngine.initialize();
        //noteworthyEngine.initialize(); delegated tin play button
    }

	public void loadLevel() {
		//activeEngine = loaderUIEngine;

	}
	
	public GameRenderer getGameRenderer() {
		return gameRenderer;
	}

	
	public void start() {
        Runtime r = Runtime.getRuntime();
        r.gc();
        if ( !gameThread.isAlive() ) {
        	gameThread = new Thread(gameLoop);

            // If commented out, sometimes doesn't resume properly...
        	gameLoop.resume(); // Make sure isFinished flag is set to false
        }
        if (gameThread.getState() == Thread.State.NEW ) {
        	gameThread.start(); 
        }
	}
	
	
	public void resume() {
		gameGLSurfaceView.onResume();

        gameLoop.resume();
		gameState = State.RUNNING;
	}
	
	public void pause() {
		gameGLSurfaceView.onPause();
		
		gameLoop.pause();
		gameRenderer.onSurfaceLost();
	}
	
	public void stop() {
		gameLoop.stop();
		gameRenderer.onSurfaceLost();
		try {
			gameThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		gameState = State.STOPPED;
		Runtime.getRuntime().gc();
	}
	
	public void onSurfaceReady() {
        // Only when we have a good surface do we resume the game loop
        //gameLoop.resume();
        //this.gameState = State.RUNNING;

        // Bubble upwards
		((GameActivity)context).onSurfaceReady();
	}

    public Context getContext() { return context; }

}
