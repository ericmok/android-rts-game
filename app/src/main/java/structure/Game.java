package structure;

import model.GameEntities;

import android.app.Activity;
import android.content.Context;

import org.json.JSONException;

import java.io.IOException;

import model.Engine;
import networking.CommandHistory;
import noteworthyengine.BattleSystem;
import noteworthyengine.CommandSystem;
import noteworthyengine.DataLoader;
import noteworthyengine.SeparationNode;
import noteworthyengine.SeparationSystem;
import noteworthyengine.TimelineSystem;
import noteworthyframework.DrawCompat;
import noteworthyengine.FieldSystem;
import noteworthyengine.MovementSystem;
import noteworthyengine.NoteworthyEngine;
import noteworthyengine.RenderSystem;

public class Game {

    public Engine engine = new Engine();

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

    public CommandHistory commandHistory = new CommandHistory(this);

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
        CommandSystem commandSystem = new CommandSystem(this);
        TimelineSystem timelineSystem = new TimelineSystem();
        SeparationSystem separationSystem = new SeparationSystem();
        FieldSystem fieldSystem = new FieldSystem();
        MovementSystem movementSystem = new MovementSystem();
        BattleSystem battleSystem = new BattleSystem();
        RenderSystem renderSystem = new RenderSystem(new DrawCompat(this));

        noteworthyEngine = new NoteworthyEngine();
        noteworthyEngine.addSystem(commandSystem);
        noteworthyEngine.addSystem(timelineSystem);
        noteworthyEngine.addSystem(fieldSystem);
        noteworthyEngine.addSystem(separationSystem);
        noteworthyEngine.addSystem(movementSystem);
        noteworthyEngine.addSystem(battleSystem);
        noteworthyEngine.addSystem(renderSystem);
    }

	public void loadLevel() {

        engine = new Engine();

        LevelFileLoader levelFileLoader = new LevelFileLoader(this.context);

//        try {
//            EngineLoader.load(engine, "self", levelFileLoader.jsonFromFile("level0.json"));
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//        catch (JSONException e) {
//            e.printStackTrace();
//        }

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

        uiOverlay.buttons.add(GameEntities.attackButtonPool.fetchMemory());
        uiOverlay.buttons.add(GameEntities.defendButtonPool.fetchMemory());

//		// TODO: Clean this up
//		FormationSystem fs = new FormationSystem(this);
//		fs.setupSquadPositions(stage.players.get(0).units, 1);
//
//		GenerateTroopsInSquadPositionsSystem gtsps = new GenerateTroopsInSquadPositionsSystem(this);
//		gtsps.process(0, this.stage.players.get(0).units);
//
//		fs.setupSquadPositions(stage.players.get(1).units, 1);
//		gtsps.process(1, this.stage.players.get(1).units);
//
//
//		TriggerField test = this.gamePool.triggerFields.fetchMemory();
//		test.source.x = -0.2;
//		test.source.y = 1.1f;
//		test.dest.x = -1;
//		test.dest.y = -0.8f;
//		stage.players.get(1).fields.add(test);
//
//		test = this.gamePool.triggerFields.fetchMemory();
//		test.source.x = 0.2;
//		test.source.y = 1.1f;
//		test.dest.x = 1;
//		test.dest.y = -0.8f;
//		stage.players.get(1).fields.add(test);
//
//
//		test = this.gamePool.triggerFields.fetchMemory();
//		test.source.x = 0;
//		test.source.y = 0.9f;
//		test.dest.x = -1;
//		test.dest.y = -0.8f;
//		stage.players.get(1).fields.add(test);
//
//		test = this.gamePool.triggerFields.fetchMemory();
//		test.source.x = 1;
//		test.source.y = -0.7f;
//		test.dest.x = 0;
//		test.dest.y = 0f;
//		stage.players.get(1).fields.add(test);
//
//		test = this.gamePool.triggerFields.fetchMemory();
//		test.source.x = -1;
//		test.source.y = 0.7f;
//		test.dest.x = 0;
//		test.dest.y = 0f;
//		stage.players.get(1).fields.add(test);
	}
	
	public GameRenderer getGameRenderer() {
		return gameRenderer;
	}

	
	public void start() {
        Runtime r = Runtime.getRuntime();
        r.gc();
        if ( !gameThread.isAlive() ) {
        	gameThread = new Thread(gameLoop);
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
		((GameActivity)context).onSurfaceReady();
	}

    public Context getContext() { return context; }

}
