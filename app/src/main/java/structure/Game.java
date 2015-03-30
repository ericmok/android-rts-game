package structure;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;

import org.json.JSONException;

import java.io.IOException;

import art.Animations;
import noteworthyengine.BackgroundUnit;
import noteworthyengine.ButtonSystem;
import noteworthyengine.units.ButtonUnit;
import noteworthyengine.units.CameraUnit;
import noteworthyengine.DataLoader;
import noteworthyengine.LoaderUIEngine;
import noteworthyframework.BaseEngine;
import noteworthyengine.NoteworthyEngine;
import utils.VoidFunc;

public class Game {

    public BaseEngine activeEngine;
    public LoaderUIEngine loaderUIEngine;
    public NoteworthyEngine noteworthyEngine;

	private Game m = this;
	
	public static final int MAX_UNITS = 2048;
	
	private Context context;
	private GameGLSurfaceView gameGLSurfaceView;
	private GameRenderer gameRenderer;

	private Thread gameThread;
	private GameLoop gameLoop;

    public GameInput gameInput;

    public GameCamera gameCamera = new GameCamera();
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
        loaderUIEngine = new LoaderUIEngine(this);
        noteworthyEngine = new NoteworthyEngine(this);

        loaderUIEngine.initialize();
        noteworthyEngine.initialize();
    }

	public void loadLevel() {
        activeEngine = loaderUIEngine;

        CameraUnit loaderUICamera = new CameraUnit(gameRenderer.mainCamera, 4f);
        CameraUnit activeGameCamera = new CameraUnit(gameRenderer.mainCamera, 0.06f);
        CameraUnit auxGameCamera = new CameraUnit(gameRenderer.auxCamera, 4);

        loaderUIEngine.addUnit(loaderUICamera);
        loaderUIEngine.mainCamera = loaderUICamera.cameraNode.camera;
        noteworthyEngine.addUnit(activeGameCamera);
        noteworthyEngine.addUnit(auxGameCamera);
        noteworthyEngine.mainCamera = activeGameCamera.cameraNode.camera;

        backgroundUnit.renderNode.width.v = 4;
        backgroundUnit.renderNode.height.v = 4;
        loaderUIEngine.addUnit(backgroundUnit);

        ButtonUnit buttonUnit = new ButtonUnit();
        buttonUnit.renderNode.animationName.v = Animations.ANIMATION_BUTTONS_PLAY;
        buttonUnit.renderNode.coords.pos.set(-0.85, 0);
        buttonUnit.renderNode.width.v = 0.5f;// (float)(1 / gameCamera.scale);
        buttonUnit.renderNode.height.v = 0.5f; //(float)(1 / gameCamera.scale);
        buttonUnit.renderNode.color.v = Color.WHITE;
        buttonUnit.buttonNode.onTap = new VoidFunc<ButtonSystem>() {
            @Override
            public void apply(ButtonSystem element) {
                activeEngine = noteworthyEngine;
            }
        };
        loaderUIEngine.addUnit(buttonUnit);
        loaderUIEngine.flushQueues();

        LevelFileLoader levelFileLoader = new LevelFileLoader(this.context);

        try {
            String json = levelFileLoader.jsonFromFile("level0.json");
            DataLoader dataLoader = new DataLoader();
            dataLoader.loadFromJson(noteworthyEngine, json);
            //noteworthyEngine.loadFromJson(json);
        }
        catch (IOException io) {
           io.printStackTrace();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

//        uiOverlay.buttons.add(GameEntities.attackButtonPool.fetchMemory());
//        uiOverlay.buttons.add(GameEntities.defendButtonPool.fetchMemory());
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
