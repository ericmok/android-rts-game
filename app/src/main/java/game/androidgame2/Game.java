package game.androidgame2;

import components.Entity;
import components.GameEntities;

import android.app.Activity;
import android.content.Context;

import components.Engine;

public class Game {

    public Engine engine = new Engine();

	private Game m = this;
	
	public static final int MAX_UNITS = 2048;
	
	private Context context;
	private GameGLSurfaceView gameGLSurfaceView;
	private GameRenderer gameRenderer;

	private Thread gameThread;
	private GameLoop gameLoop;

    public GameInput gameInput;

    public UIOverlay uiOverlay = new UIOverlay();

	/**
	 * Stores pre-loaded heap memory allocations of game objects 
	 */
	public GamePool gamePool;
	
	/**
	 * Stores all game objects. Public access to save access time.
	 */
	public Stage stage;
	
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
	
	public void loadLevel() {

        engine = new Engine();

        LevelLoader levelLoader = new LevelLoader(this.context);
        levelLoader.load(engine, "level0.json");

        uiOverlay.buttons.add(GameEntities.buildAttackButton());
        uiOverlay.buttons.add(GameEntities.buildAttackButton());

////
//		stage = new Stage();
//
////		Troop troop = gamePool.troops.fetchMemory();
////		troop.position.x = 0.0f;
////		troop.position.y = -1.0f;
////		troop.type = Troop.Type.BIG_TROOP;
////		//troop.formationType[0] = FormationNode.Type.CAPTAIN;
////		troop.radius[0] = Constants.UNIT_RADIUS;
////		troop.orientation.setDegrees(90);
////		troop.states.add(States.TEAM_1);
////		troop.team[0] = Stage.TEAM_0;
////		Player player = stage.players.get(0);
////		player.units.add(troop);
//
//		SmallShip smallShip = new SmallShip();
//		smallShip.position.x = 0;
//		smallShip.position.y = -1.0f;
//		smallShip.states.put(States.ALIVE, true);
//		stage.players.get(0).units.add(smallShip);
//
////
////		// Demo small troops
//		int number = 4;
//		for (int i = 0; i < number; i++) {
//			Troop temp = gamePool.troops.fetchMemory();
//
//			temp.position.x = 0.0f + i * 0.35 - (0.35 * number / 2);
//			temp.position.y = -0.9f + (Math.random() * 0.2f);
//			temp.orientation.setDegrees(90);
//			temp.type = Math.random() > 0 ? Troop.Type.BIG_TROOP : Troop.Type.SMALL_TROOP;
//            if (temp.type == Troop.Type.BIG_TROOP) {
//                temp.getLabels().add(SystemNode.Label.BigTroop);
//            }
//            else {
//                temp.getLabels().add(SystemNode.Label.SmallTroop);
//            }
//			if (temp.type == Troop.Type.BIG_TROOP) {
//				temp.radius[0] = Constants.UNIT_RADIUS;
//			}
//			if (temp.type == Troop.Type.SMALL_TROOP) {
//				temp.radius[0] = Constants.UNIT_RADIUS * 0.5f;
//			}
//			temp.states.put(States.ALIVE, true);
//			temp.player[0] = 0;
//			stage.players.get(0).units.add( temp );
//		}
//
//		Troop enemyTroop = gamePool.troops.fetchMemory();
//		enemyTroop.position.x = 0.0f;
//		enemyTroop.position.y = 1.3f;
//		enemyTroop.radius[0] = Constants.UNIT_RADIUS;
//		enemyTroop.orientation.setDegrees(270);
//		//enemyTroop.team = Troop.Team.TEAM_1;
//		//enemyTroop.stateProgress = 1;
//		enemyTroop.state = Troop.State.IDLE;
//		enemyTroop.player[0] = 1;
//		enemyTroop.states.put(States.TEAM_1, true);
//		enemyTroop.states.put(States.ALIVE, true);
//
//		stage.players.get(1).units.add( enemyTroop );
//
//		for (int i = 0; i < number; i++) {
//			enemyTroop = gamePool.troops.fetchMemory();
//			enemyTroop.radius[0] = Constants.UNIT_RADIUS;
//			enemyTroop.position.x = 0.0f + i * 0.35 - (0.35 * number / 2);
//			enemyTroop.position.y = 1.4f + (Math.random() * 0.3f);
//			enemyTroop.orientation.setDegrees(90);
//			enemyTroop.type = Math.random() > 0 ? Troop.Type.BIG_TROOP : Troop.Type.SMALL_TROOP;
//            if (enemyTroop.type == Troop.Type.BIG_TROOP) {
//                enemyTroop.getLabels().add(SystemNode.Label.BigTroop);
//            }
//            else {
//                enemyTroop.getLabels().add(SystemNode.Label.SmallTroop);
//            }
//			enemyTroop.player[0] = 1;
//			//enemyTroop.team = Troop.Team.TEAM_1;
//			if (enemyTroop.type == Troop.Type.BIG_TROOP) {
//				//enemyTroop.formationType[0] = FormationNode.Type.CAPTAIN;
//				enemyTroop.radius[0] = Constants.UNIT_RADIUS;
//			}
//			if (enemyTroop.type == Troop.Type.SMALL_TROOP) {
//				//enemyTroop.formationType[0] = FormationNode.Type.SOLDIER;
//				enemyTroop.radius[0] = Constants.UNIT_RADIUS * 0.5f;
//			}
//			enemyTroop.states.put(States.ALIVE, true);
//			enemyTroop.states.put(States.TEAM_1, true);
//
//			enemyTroop.velocity.x = 0;
//			enemyTroop.velocity.y = -1;
//			enemyTroop.velocity.setNormalized2d();
//			enemyTroop.velocity.scale(0.08f, 0.08f, 1f);
//
//			enemyTroop.player[0] = 1;
//
//			stage.players.get(1).units.add( enemyTroop );
//		}
//
//		// TODO: Clean this up
//		FormationSystem fs = new FormationSystem(this);
//		fs.setupSquadPositions(stage.players.get(0).units, 1);
//
//		GenerateTroopsInSquadPositionsSystem gtsps = new GenerateTroopsInSquadPositionsSystem(this);
//		gtsps.update(0, this.stage.players.get(0).units);
//
//		fs.setupSquadPositions(stage.players.get(1).units, 1);
//		gtsps.update(1, this.stage.players.get(1).units);
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
