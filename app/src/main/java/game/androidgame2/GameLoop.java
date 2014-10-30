package game.androidgame2;

import java.util.ArrayList;
import java.util.Hashtable;

import android.graphics.Color;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;

import tenth.system.BattleSystem;
import tenth.system.CleanDeadUnitSystem;
import tenth.system.Constants;
import tenth.system.FieldMovementSystem;
import tenth.system.ForceIntegratorSystem;
import tenth.system.FormationSystem;
import tenth.system.GenerateTroopsInSquadPositionsSystem;
import tenth.system.OrientationSystem;
import tenth.system.SelectionSystem;
import tenth.system.SeparationSystem;
import tenth.system.ShipDrawSystem;
import tenth.system.StateProgressUpdateSystem;
import tenth.system.SystemNode;
import tenth.system.TroopDrawSystem;

public class GameLoop implements Runnable {
	
	private Game game;
	
	private Object pauseLock;	
	
	private boolean isFinished = false;
	private boolean isPaused = false;
	
	public final static long TARGET_MILLISEC_PER_FRAME = 12;
	
	private long previousTick = 0;
	private long tickDifference = 0;
	private long startTime = 0;
	
	private MotionEvent motionEvent; // Pointer
	private float touchX = 0;
	private float touchY = 0;

	
	private Hashtable<Integer, Boolean> gestures = new Hashtable<Integer, Boolean>(8);
	public static final int GESTURE_ON_DOWN = 0;
	public static final int GESTURE_ON_SHOW_PRESS = 1;
	public static final int GESTURE_ON_SINGLE_TAP_UP = 2;
	public static final int GESTURE_ON_SCROLL = 3;
	public static final int GESTURE_ON_LONG_PRESS = 4;
	public static final int GESTURE_ON_FLING = 5;
	
	
	private FieldMovementSystem fieldMovementSystem;
	
	private OrientationSystem orientationSystem = new OrientationSystem(this.game);
	
	private StateProgressUpdateSystem stateProgressUpdateSystem;
	private TroopDrawSystem troopDrawSystem;
	private ShipDrawSystem shipDrawSystem = new ShipDrawSystem(game);

	private GenerateTroopsInSquadPositionsSystem generateTroopsInSquadPositionsSystem;
	
	private BattleSystem battleResolutionSystem;
	
	private CleanDeadUnitSystem cleanDeadUnitSystem;
	
	private FormationSystem formationSystem;
	private SeparationSystem separationSystem;
	
	private ForceIntegratorSystem forceIntegratorSystem;
	
	private SelectionSystem selectionSystem;
	
	private TriggerField activeTriggerField = null;
	private TimedProgress activeAnimation;
	
	private final void debugLog(String a, String b) {
		//Log.i(a, b);
	}
	
	public ArrayList<SystemNode> selected = new ArrayList<SystemNode>(Player.MAX_UNITS);
	
	public ArrayList<SystemNode> tempList = new ArrayList<SystemNode>(Player.MAX_UNITS);
	public ArrayList<SystemNode> tempList2 = new ArrayList<SystemNode>(Player.MAX_UNITS);
	
	
	public GameLoop(Game game) {
		this.game = game;
		
		fieldMovementSystem = new FieldMovementSystem(game);
		stateProgressUpdateSystem = new StateProgressUpdateSystem(game);
		troopDrawSystem = new TroopDrawSystem(game);
		
		generateTroopsInSquadPositionsSystem = new GenerateTroopsInSquadPositionsSystem(game);
		
		battleResolutionSystem = new BattleSystem(game);
		
		cleanDeadUnitSystem = new CleanDeadUnitSystem(game);
		
		formationSystem = new FormationSystem(game);
		separationSystem = new SeparationSystem(game);
		
		forceIntegratorSystem = new ForceIntegratorSystem(game);
		
		selectionSystem = new SelectionSystem(game);

		pauseLock = new Object();
		
		activeAnimation = new TimedProgress();
		activeAnimation.duration = 4000;
	}
	
	/**
	 * Runs a single frame. To be called asynchronously.
	 */
	public void run() {
		debugLog("GameLoop", "Running");
		previousTick = SystemClock.uptimeMillis();
		
			startTime = SystemClock.uptimeMillis();
		
		while(!isFinished) {
			
			long startTick = SystemClock.uptimeMillis();
			tickDifference = startTick - previousTick;			
			previousTick = startTick; // Update previous tick
			
			//allocation unsafe!
			//debugLog("DELTA TIME", Long.toString(tickDifference));

			
			// Game calculations go here
			this.performGameLogic(tickDifference); 
			
			
			
			long finishedTick = SystemClock.uptimeMillis();
			long timeOfCalculation = (finishedTick - startTick);
			//allocation unsafe!
			//debugLog("GameLoop", "Time Of Calculation: " + Long.toString(timeOfCalculation));
			
			debugLog("GameLoop", "Is not finished. Entering PausLock");

			try {
				// Only if the thread is fast enough do we want to sleep
				long timeLeftUntilThreadShouldRunAgain = TARGET_MILLISEC_PER_FRAME - timeOfCalculation;
				if (timeLeftUntilThreadShouldRunAgain > 0) {
					Thread.sleep(timeLeftUntilThreadShouldRunAgain);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			synchronized(pauseLock) {
				debugLog("GameLoop", "Entering while paused");
				
				while (isPaused) {
					debugLog("GameLoop", "Waiting on pause");
					try {
						pauseLock.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				debugLog("GameLoop", "End while in pause sync");
			}
			
		} // End Game Loop
		
		debugLog("GameLoop", "Is finished.");
	}
	
	private void performGameLogic(long elapsedTime) {
		
		// TODO: Allow scrolling of the screen

		if (game.getGameState() == Game.State.RUNNING) {
			
			// Team 0
			fieldMovementSystem.update(game.stage.players.get(0).units, game.stage.players.get(0).fields, elapsedTime);

			// Team 1
			fieldMovementSystem.update(game.stage.players.get(1).units, game.stage.players.get(1).fields, elapsedTime);

            tempList.clear();
            for (int i = 0; i < game.stage.players.get(0).units.size(); i++) {
                SystemNode node = game.stage.players.get(0).units.get(i);
                if (node.getLabels().contains(SystemNode.Label.Troop)) {
                    tempList2.add((Troop) node);
                }
            }
            for (int i = 0; i < game.stage.players.get(1).units.size(); i++) {
                SystemNode node = game.stage.players.get(1).units.get(i);
                if (node.getLabels().contains(SystemNode.Label.Troop)) {
                    tempList2.add((Troop) node);
                }
            }
            separationSystem.update(tempList, elapsedTime);
            forceIntegratorSystem.update(tempList, elapsedTime);


			orientationSystem.update(game.stage.players.get(0).units, elapsedTime);
			orientationSystem.update(game.stage.players.get(1).units, elapsedTime);

			battleResolutionSystem.update(game.stage.players.get(0).units, game.stage.players.get(1).units, elapsedTime);


			selectionSystem.update(game.stage.players.get(0).units, selected, touchX, touchY, gestures, elapsedTime);


			RewritableArray<DrawList2DItem> drawItems = game.graphics.drawLists.drawListSprites.lockWritableBuffer();
			drawItems.resetWriteIndex();

			troopDrawSystem.update(drawItems, game.graphics.drawLists.temporarySprites, game.stage.players.get(0).units, elapsedTime);
			troopDrawSystem.update(drawItems, game.graphics.drawLists.temporarySprites, game.stage.players.get(1).units, elapsedTime);

			shipDrawSystem.update(drawItems, game.stage.players.get(0).units, elapsedTime);
			shipDrawSystem.update(drawItems, game.stage.players.get(1).units, elapsedTime);


			if (activeTriggerField != null) {
				DrawList2DItem triggerField = drawItems.takeNextWritable();

				triggerField.position.x = activeTriggerField.source.x;
				triggerField.position.y = activeTriggerField.source.y;
				triggerField.width = 0.71f; // originally .52 -> .5
				triggerField.height = 0.71f;
				triggerField.angle = (float) Orientation.getDegrees(activeTriggerField.source.x, activeTriggerField.source.y,
											activeTriggerField.dest.x, activeTriggerField.dest.y);
				triggerField.animationName = DrawList2DItem.ANIMATION_TRIGGER_FIELDS_EXISTING;
				activeAnimation.update(elapsedTime);
				triggerField.animationProgress = (int)activeAnimation.progress;
			}

			for (int i = 0; i < game.stage.players.get(0).fields.size(); i++) {
				DrawList2DItem triggerField = drawItems.takeNextWritable();

				triggerField.position.x = game.stage.players.get(0).fields.get(i).source.x;
				triggerField.position.y = game.stage.players.get(0).fields.get(i).source.y;
				triggerField.width = 0.65f;
				triggerField.height = 0.65f;
				triggerField.angle = game.stage.players.get(0).fields.get(i).getAngle();
				triggerField.animationName = DrawList2DItem.ANIMATION_TRIGGER_FIELDS_EXISTING;
				triggerField.animationProgress = 0;

			}


			if (selected.size() > 0) {
				DrawList2DItem buttonTest = drawItems.takeNextWritable();
				buttonTest.animationName = DrawList2DItem.ANIMATION_BUTTONS_MOVE;
				buttonTest.position.x = -1.2;
				buttonTest.position.y = -0.8;
				buttonTest.angle = 0;
				buttonTest.width = 0.2f;
				buttonTest.height = 0.2f;

				buttonTest = drawItems.takeNextWritable();
				buttonTest.animationName = DrawList2DItem.ANIMATION_BUTTONS_ATTACK;
				buttonTest.position.x = -1.42;
				buttonTest.position.y = -0.8;
				buttonTest.angle = 0;
				buttonTest.width = 0.2f;
				buttonTest.height = 0.2f;

				buttonTest = drawItems.takeNextWritable();
				buttonTest.animationName = "Animations/Panels/Sidebar";
				buttonTest.position.x = -1.7;
				buttonTest.position.y = -0.8;
				buttonTest.angle = 0;
				buttonTest.width = 0.5f;
				buttonTest.height = 2f;

				for (int i = 0; i < selected.size(); i++) {
					SystemNode node = selected.get(i);
					buttonTest = drawItems.takeNextWritable();
					buttonTest.animationName = DrawList2DItem.ANIMATION_BUTTONS_ATTACK;
					buttonTest.position.x = -1.42;
					buttonTest.position.y = -0.1 * i + 1;
					buttonTest.angle = 0;
					buttonTest.width = 0.2f;
					buttonTest.height = 0.1f;
				}
			}

			game.graphics.drawLists.drawListSprites.unlockWritableBuffer();
			game.graphics.drawLists.drawListSprites.finalizeUpdate();


			RewritableArray<TextDrawItem> textDrawItems = game.graphics.drawLists.textDrawItems.lockWritableBuffer();
			textDrawItems.resetWriteIndex();
			TextDrawItem text = textDrawItems.takeNextWritable();
			text.stringBuilder.setLength(0);
			if (game.stage.players.get(1).units.size() == 0) {
				text.stringBuilder.append("Success!");
				text.color = Color.GREEN;
			}
			else {
				// This causes unmanaged allocations!
				//text.stringBuilder.append((char)((game.stage.players.get(1).units.size() % 10 + 48)));
				//text.stringBuilder.append((char)((game.stage.players.get(1).units.size() % 100 + 48)));
				text.stringBuilder.append(" units left");
				text.color = Color.YELLOW;
			}
			text.position.y = 1.0;



			cleanDeadUnitSystem.update(game.stage.players.get(0).units, elapsedTime);
			cleanDeadUnitSystem.update(game.stage.players.get(1).units, elapsedTime);

			if (gestures.containsKey(GESTURE_ON_SINGLE_TAP_UP)) {
				Log.i("Make tap sprite", "Make tap sprite");
				TemporaryDrawList2DItem reticle = game.gamePool.temporaryDrawItems.fetchMemory();
				reticle.animationName = DrawList2DItem.ANIMATION_RETICLE_TAP;
				reticle.position.x = touchX;
				reticle.position.y = touchY;
				reticle.width = (float)Constants.UNIT_RADIUS * 9;
				reticle.height = (float)Constants.UNIT_RADIUS * 9;
				reticle.progress.progress = 0;
				reticle.color = Color.WHITE;
				reticle.progress.duration = 300;
				game.graphics.drawLists.temporarySprites.add(reticle);
			}


			game.graphics.drawLists.textDrawItems.unlockWritableBuffer();
			game.graphics.drawLists.textDrawItems.finalizeUpdate();

		}
		
		gestures.clear();
	}
	
	
	/**
	 * Make sure isFinished flag is set to false so that resuming the thread doesn't lead it to finished!
	 */
	public void resume() {
		synchronized(pauseLock) {
			previousTick = SystemClock.uptimeMillis();
			isPaused = false;
			isFinished = false;
			
			pauseLock.notifyAll();
		}
	}
	
	public void pause() {
		synchronized(pauseLock) {
			isPaused = true;
		}
	}
	
	public void stop() {
		synchronized(pauseLock) {
			isPaused = false;
			isFinished = true; // ...Stop = Stop game?
			pauseLock.notifyAll();
		}
	}
	
	public void onTouchEvent(MotionEvent event, float x, float y) {
		motionEvent = event;
		touchX = x;
		touchY = y;
		
//		touchX = 2.0f * x / 1080;
//		touchY = 2.0f * y / 1920 * (1920.0f / 1080);
//		
		//Log.i("GameLoop TouchEvent", "Touch at: " + x + "," + y);
		
		if (activeTriggerField == null) {
			activeTriggerField = game.gamePool.triggerFields.fetchMemory();	
		}
		if ( event.getAction() == MotionEvent.ACTION_DOWN ) {
			activeTriggerField.source.x = touchX;
			activeTriggerField.source.y = touchY;	
			activeTriggerField.dest.x = touchX;
			activeTriggerField.dest.y = touchY + 0.1;
		}
		if ( event.getAction() == MotionEvent.ACTION_MOVE ) {
			if (activeTriggerField.dest.x == touchX && activeTriggerField.dest.y == touchY) {
				activeTriggerField.dest.x = touchX;
				activeTriggerField.dest.y = touchY + 0.1;
			}
			else  {
				activeTriggerField.dest.x = touchX;
				activeTriggerField.dest.y = touchY;
			}
		}
		if ( event.getAction() == MotionEvent.ACTION_UP ) {
			if (activeTriggerField.dest.x != activeTriggerField.source.x && activeTriggerField.dest.y != activeTriggerField.source.y) {
				activeTriggerField.dest.x = touchX;
				activeTriggerField.dest.y = touchY;
				game.stage.players.get(0).fields.add(activeTriggerField);
			}
			
			activeAnimation.resetProgress();
			activeTriggerField = null;
		}
		
	}

	
	public boolean gestureOnDown(MotionEvent e) {
		Log.i("gestureOnDown", "gestureOnDown");
		gestures.put(GESTURE_ON_DOWN, true);
		return true;
	}

	public void gestureOnShowPress(MotionEvent e) {
		Log.i("gestureOnShowPress", "gestureOnShowPress");
		gestures.put(GESTURE_ON_SHOW_PRESS, true);
	}

	public boolean gestureOnSingleTapUp(MotionEvent e) {
		Log.i("gestureOnSingleTapUp", "gestureOnSingleTapUp");
		gestures.put(GESTURE_ON_SINGLE_TAP_UP, true);
		return true;
	}

	public boolean gestureOnScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		Log.i("gestureOnScroll", "gestureOnScroll");
		gestures.put(GESTURE_ON_SCROLL, true);
		return true;
	}

	public void gestureOnLongPress(MotionEvent e) {
		Log.i("gestureOnLongPress", "gestureOnLongPress");
		gestures.put(GESTURE_ON_LONG_PRESS, true);
	}

	public boolean gestureOnFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		Log.i("gestureOnFling", "gestureOnFling");
		gestures.put(GESTURE_ON_FLING, true);
		return true;
	}

}
