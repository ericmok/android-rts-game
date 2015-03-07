package structure;

import java.util.ArrayList;
import java.util.List;

import android.os.SystemClock;

import behaviors.Behaviors;
import model.Entity;

import networking.Command;
import processors.EngineSimulator;
import processors.MapScrollFunction;
import processors.ProjectileDrawerProcess;
import processors.SelectionProcessor;
import processors.TroopDrawerProcess;
import utils.Vector2;

public class GameLoop implements Runnable {
	
	private Game game;
	
	private Object pauseLock;	
	
	private boolean isFinished = false;
	private boolean isPaused = false;
	
	public final static long TARGET_MILLISEC_PER_FRAME = 12;

    public double accumulator = 0;

    private long originTick = 0;
	private long previousTick = 0;
	private long tickDifference = 0;

    private SelectionProcessor selectionProcessor;
	
	private final void debugLog(String a, String b) {
		//Log.i(a, b);
	}

	public GameLoop(Game game) {
		this.game = game;

        selectionProcessor = new SelectionProcessor(game);
		pauseLock = new Object();
	}
	
	/**
	 * Runs a single frame. To be called asynchronously.
	 */
	public void run() {
		debugLog("GameLoop", "Running");

        originTick = SystemClock.uptimeMillis();
        previousTick = SystemClock.uptimeMillis();
		
		while(!isFinished) {
			
			long currentTick = SystemClock.uptimeMillis();
			tickDifference = currentTick - previousTick;
			previousTick = currentTick; // Update previous tick

			//allocation unsafe!
			//debugLog("DELTA TIME", Long.toString(tickDifference));

            // Refer to:
            // http://gafferongames.com/game-physics/fix-your-timestep/

            // Produce time, the "faster" the frame, the less time produced
            accumulator += tickDifference;

            // Consume time in segments
            if (accumulator > TARGET_MILLISEC_PER_FRAME) {

                // Variable step
                //this.performGameLogic(currentTick, tickDifference);

                // Semi-fixed step
                this.performGameLogic(currentTick, TARGET_MILLISEC_PER_FRAME);

                // Chew! Chew! Accumulator Yum!
                accumulator -= TARGET_MILLISEC_PER_FRAME;
            }

			long finishedTick = SystemClock.uptimeMillis();
			long timeOfCalculation = (finishedTick - currentTick) + 1;
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
	
	private void performGameLogic(long currentTick, long elapsedTime) {

        double ct = currentTick * GameSettings.UNIT_TIME_MULTIPLIER;
        double dt = elapsedTime * GameSettings.UNIT_TIME_MULTIPLIER;

        int currentGesture = game.gameInput.takeCurrentGesture();

        game.engine.eachPlayerProcessAdded();

        Command newCommand = null;

        MapScrollFunction.apply(currentGesture, game.gameInput, game.gameCamera);

        game.uiOverlay.processInput(game.gameCamera, currentGesture, game.gameInput);



//        if (currentGesture == GameInput.GESTURE_ON_SINGLE_TAP_UP) {
//            if (selectionProcessor.userSelection.isEmpty()) {
//                //ArrayList<Entity> selectableEntities = game.engine.entityDenormalizer.getListForLabel(Entity.BEHAVIOR_GETS_SELECTED);
//                ArrayList<Entity> selectableEntities = game.engine.currentPlayer.denorms.getListForLabel(Behaviors.BEHAVIOR_GETS_SELECTED);
//                selectionProcessor.process(selectableEntities, game.gameCamera,
//                        game.gameInput.touchPosition, SelectionProcessor.FN_SELECT);
//            }
//            else {
//                //UserChooseNewDestinationFunction.apply(selectionProcessor.userSelection, game.gameCamera, game.gameInput, SelectionProcessor.FN_DESELECT);
//
//                if (game.uiOverlay.currentButton != null) {
//
//                    // Construct fire command
//                    Vector2 temp = game.gamePool.vector2s.fetchMemory();
//                    game.gameCamera.getScreenToWorldCoords(temp, game.gameInput.touchPosition);
//
//                    newCommand = game.gamePool.commands.fetchMemory();
//
//                    newCommand.command = Command.FIRE;
//                    newCommand.timeStamp = ct;
//
//                    newCommand.selection.clear();
//
//                    for (int i = 0; i < selectionProcessor.userSelection.size(); i++) {
//                        newCommand.selection.add(selectionProcessor.userSelection.get(i));
//                    }
//                    newCommand.vec.copy(temp);
//
//                    game.gamePool.vector2s.recycleMemory(temp);
//                }
//                else {
//                    // Construct move command
//                    Vector2 temp = game.gamePool.vector2s.fetchMemory();
//                    game.gameCamera.getScreenToWorldCoords(temp, game.gameInput.touchPosition);
//
//                    newCommand = game.gamePool.commands.fetchMemory();
//
//                    newCommand.command = Command.MOVE;
//                    newCommand.timeStamp = ct;
//
//                    newCommand.selection.clear();
//
//                    // Copy the selection, (we might have to replay the command)
//                    for (int i = 0; i < selectionProcessor.userSelection.size(); i++) {
//                        newCommand.selection.add(selectionProcessor.userSelection.get(i));
//                    }
//                    newCommand.vec.copy(temp);
//
//                    game.gamePool.vector2s.recycleMemory(temp);
//                }
//
//                for (int i = 0; i < selectionProcessor.userSelection.size(); i++) {
//                    Entity entity = selectionProcessor.userSelection.get(i);
//                    SelectionProcessor.FN_DESELECT.apply(entity);
//                }
//
//                // This has to go somewhere
//                selectionProcessor.userSelection.clear();
//            }
//        }

//        if (newCommand != null) {
//
//            // There was a command, so we need to send a network request
//            // and affect local copy of state
//            game.commandHistory.commands.add(newCommand);
//        }

        // If server updated the engine, then replay all commands in the history.
        // Then ack the commands that come before the servers time frame as these
        // commands are officially processed by the server

        // Since we have no network right now, the engine is never diff'd
//        if (1 == 0) {
//
//            // Engine got updated with new state, time to "rebase"
//            double replayTime = game.engine.gameTime;
//
//            for (int i = 0; i < game.commandHistory.commands.size(); i++) {
//                Command replayCommand = game.commandHistory.commands.get(i);
//
//                EngineSimulator.changeModelWithCommand(game.engine, replayCommand);
//
//                // Interpolate between the command's timeStamp and engine's current time
//                EngineSimulator.interpolate(game.engine, replayCommand.timeStamp,
//                        replayCommand.timeStamp - replayTime);
//
//                // Move time forward towards the command's time (possibly to current Time)
//                replayTime = replayCommand.timeStamp;
//            }
//        }
//        else {
//
//            // Engine has no server process, continue running naively
//            if (newCommand != null) EngineSimulator.changeModelWithCommand(game.engine, newCommand);
//
//            // If there was no command, we still want an interpolation after all
//            EngineSimulator.interpolate(game.engine, ct, dt);
//        }

        game.engine.gameTime = ct;

//        // When the network returns the state, commands will be considered ack'd or not
//        // Non-ack'd commands will have to be replayed on the networks version of the engine state
//        // Since there is no network, we'll consider commands ack'd immediately
//        while (!game.commandHistory.commands.isEmpty()) {
//            game.commandHistory.ack(ct);
//        }

        // TODO: Set the engine state to the network state


        // Begin graphics mutations
        // (The mutations don't take affect immediately until explicit finalizations)
            List<TemporaryDrawList2DItem> tempSprites = game.graphics.drawLists.temporarySprites;

            RewriteOnlyArray<DrawList2DItem> spriteAllocater = game.graphics.drawLists.regularSprites.lockWritableBuffer();
            spriteAllocater.resetWriteIndex();

            // Draw buttons when abilities are available
            game.uiOverlay.draw(game.gameCamera, spriteAllocater);

            // Draw troops
            //ArrayList<Entity> entitiesToDraw = game.engine.currentPlayer.denorms.getListForLabel(Entity.BEHAVIOR_DRAWN_AS_TROOP);
            for (int i = 0; i < game.engine.players.size(); i++) {
                TroopDrawerProcess.process(spriteAllocater, tempSprites, game.gamePool, game.engine.players.get(i), dt);
                ProjectileDrawerProcess.process(spriteAllocater, tempSprites, game.gamePool, game.engine.players.get(i), dt);
            }

            // Flush graphics at the same time so settings are sync'd

            game.graphics.drawLists.regularSprites.unlockWritableBuffer();
            game.graphics.drawLists.regularSprites.finalizeUpdate();

        game.graphics.setCameraPositionAndScale((float)game.gameCamera.x, (float)game.gameCamera.y, (float)game.gameCamera.scale);
        game.graphics.flushCameraModifications();


        game.engine.eachPlayerProcessRemoved();

        //game.graphics.flushCameraModifications(false);

//		if (game.getGameState() == Game.State.RUNNING) {
//
//			// Team 0
//			fieldMovementSystem.process(game.stage.players.get(0).units, game.stage.players.get(0).fields, elapsedTime);
//
//			// Team 1
//			fieldMovementSystem.process(game.stage.players.get(1).units, game.stage.players.get(1).fields, elapsedTime);
//
//            tempList.clear();
//            for (int i = 0; i < game.stage.players.get(0).units.size(); i++) {
//                SystemNode node = game.stage.players.get(0).units.get(i);
//                if (node.labels().contains(SystemNode.Label.Troop)) {
//                    tempList2.add((Troop) node);
//                }
//            }
//            for (int i = 0; i < game.stage.players.get(1).units.size(); i++) {
//                SystemNode node = game.stage.players.get(1).units.get(i);
//                if (node.labels().contains(SystemNode.Label.Troop)) {
//                    tempList2.add((Troop) node);
//                }
//            }
//            separationSystem.process(tempList, elapsedTime);
//            forceIntegratorSystem.process(tempList, elapsedTime);
//
//
//			orientationSystem.process(game.stage.players.get(0).units, elapsedTime);
//			orientationSystem.process(game.stage.players.get(1).units, elapsedTime);
//
//			battleResolutionSystem.process(game.stage.players.get(0).units, game.stage.players.get(1).units, elapsedTime);
//
//
//			selectionSystem.process(game.stage.players.get(0).units, selected, touchX, touchY, gestures, elapsedTime);
//
//
//			RewritableArray<DrawList2DItem> drawItems = game.graphics.drawLists.regularSprites.lockWritableBuffer();
//			drawItems.resetWriteIndex();
//
//			troopDrawSystem.process(drawItems, game.graphics.drawLists.temporarySprites, game.stage.players.get(0).units, elapsedTime);
//			troopDrawSystem.process(drawItems, game.graphics.drawLists.temporarySprites, game.stage.players.get(1).units, elapsedTime);
//
//			shipDrawSystem.process(drawItems, game.stage.players.get(0).units, elapsedTime);
//			shipDrawSystem.process(drawItems, game.stage.players.get(1).units, elapsedTime);
//
//
//			if (activeTriggerField != null) {
//				DrawList2DItem triggerField = drawItems.takeNextWritable();
//
//				triggerField.position.x = activeTriggerField.source.x;
//				triggerField.position.y = activeTriggerField.source.y;
//				triggerField.width = 0.71f; // originally .52 -> .5
//				triggerField.height = 0.71f;
//				triggerField.angle = (float) Orientation.getDegrees(activeTriggerField.source.x, activeTriggerField.source.y,
//											activeTriggerField.dest.x, activeTriggerField.dest.y);
//				triggerField.animationName = DrawList2DItem.ANIMATION_TRIGGER_FIELDS_EXISTING;
//				activeAnimation.process(elapsedTime);
//				triggerField.animationProgress = (int)activeAnimation.progress;
//			}
//
//			for (int i = 0; i < game.stage.players.get(0).fields.size(); i++) {
//				DrawList2DItem triggerField = drawItems.takeNextWritable();
//
//				triggerField.position.x = game.stage.players.get(0).fields.get(i).source.x;
//				triggerField.position.y = game.stage.players.get(0).fields.get(i).source.y;
//				triggerField.width = 0.65f;
//				triggerField.height = 0.65f;
//				triggerField.angle = game.stage.players.get(0).fields.get(i).getAngle();
//				triggerField.animationName = DrawList2DItem.ANIMATION_TRIGGER_FIELDS_EXISTING;
//				triggerField.animationProgress = 0;
//
//			}
//
//
//			if (selected.size() > 0) {
//				DrawList2DItem buttonTest = drawItems.takeNextWritable();
//				buttonTest.animationName = DrawList2DItem.ANIMATION_BUTTONS_MOVE;
//				buttonTest.position.x = -1.2;
//				buttonTest.position.y = -0.8;
//				buttonTest.angle = 0;
//				buttonTest.width = 0.2f;
//				buttonTest.height = 0.2f;
//
//				buttonTest = drawItems.takeNextWritable();
//				buttonTest.animationName = DrawList2DItem.ANIMATION_BUTTONS_ATTACK;
//				buttonTest.position.x = -1.42;
//				buttonTest.position.y = -0.8;
//				buttonTest.angle = 0;
//				buttonTest.width = 0.2f;
//				buttonTest.height = 0.2f;
//
//				buttonTest = drawItems.takeNextWritable();
//				buttonTest.animationName = "Animations/Panels/Sidebar";
//				buttonTest.position.x = -1.7;
//				buttonTest.position.y = -0.8;
//				buttonTest.angle = 0;
//				buttonTest.width = 0.5f;
//				buttonTest.height = 2f;
//
//				for (int i = 0; i < selected.size(); i++) {
//					SystemNode node = selected.get(i);
//					buttonTest = drawItems.takeNextWritable();
//					buttonTest.animationName = DrawList2DItem.ANIMATION_BUTTONS_ATTACK;
//					buttonTest.position.x = -1.42;
//					buttonTest.position.y = -0.1 * i + 1;
//					buttonTest.angle = 0;
//					buttonTest.width = 0.2f;
//					buttonTest.height = 0.1f;
//				}
//			}
//
//			game.graphics.drawLists.regularSprites.unlockWritableBuffer();
//			game.graphics.drawLists.regularSprites.finalizeUpdate();
//
//
//			RewritableArray<TextDrawItem> textDrawItems = game.graphics.drawLists.textDrawItems.lockWritableBuffer();
//			textDrawItems.resetWriteIndex();
//			TextDrawItem text = textDrawItems.takeNextWritable();
//			text.stringBuilder.setLength(0);
//			if (game.stage.players.get(1).units.size() == 0) {
//				text.stringBuilder.append("Success!");
//				text.color = Color.GREEN;
//			}
//			else {
//				// This causes unmanaged allocations!
//				//text.stringBuilder.append((char)((game.stage.players.get(1).units.size() % 10 + 48)));
//				//text.stringBuilder.append((char)((game.stage.players.get(1).units.size() % 100 + 48)));
//				text.stringBuilder.append(" units left");
//				text.color = Color.YELLOW;
//			}
//			text.position.y = 1.0;
//
//
//
//			cleanDeadUnitSystem.process(game.stage.players.get(0).units, elapsedTime);
//			cleanDeadUnitSystem.process(game.stage.players.get(1).units, elapsedTime);
//
//			if (gestures.containsKey(GESTURE_ON_SINGLE_TAP_UP)) {
//				Log.i("Make tap sprite", "Make tap sprite");
//				TemporaryDrawList2DItem reticle = game.gamePool.temporaryDrawItems.fetchMemory();
//				reticle.animationName = DrawList2DItem.ANIMATION_RETICLE_TAP;
//				reticle.position.x = touchX;
//				reticle.position.y = touchY;
//				reticle.width = (float)Constants.UNIT_RADIUS * 9;
//				reticle.height = (float)Constants.UNIT_RADIUS * 9;
//				reticle.progress.progress = 0;
//				reticle.color = Color.WHITE;
//				reticle.progress.duration = 300;
//				game.graphics.drawLists.temporarySprites.add(reticle);
//			}
//
//
//			game.graphics.drawLists.textDrawItems.unlockWritableBuffer();
//			game.graphics.drawLists.textDrawItems.finalizeUpdate();
//
//		}
		
		//gestures.clear();
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
	
//	public void onTouchEvent(MotionEvent event) {
//		motionEvent = event;
//
//        Vector2 vec = game.gamePool.vector2s.fetchMemory();
//		game.gameInput.getCoordsTranslatedToCenterOfScreen(vec, event);
//        touchX = (float)vec.x;
//        touchY = (float)vec.y;
//        game.gamePool.vector2s.recycleMemory(vec);
//
////		touchX = 2.0f * x / 1080;
////		touchY = 2.0f * y / 1920 * (1920.0f / 1080);
////
//		//Log.i("GameLoop TouchEvent", "Touch at: " + x + "," + y);
//
////		if (activeTriggerField == null) {
////			activeTriggerField = game.gamePool.triggerFields.fetchMemory();
////		}
////		if ( event.getAction() == MotionEvent.ACTION_DOWN ) {
////			activeTriggerField.source.x = touchX;
////			activeTriggerField.source.y = touchY;
////			activeTriggerField.dest.x = touchX;
////			activeTriggerField.dest.y = touchY + 0.1;
////		}
////		if ( event.getAction() == MotionEvent.ACTION_MOVE ) {
////			if (activeTriggerField.dest.x == touchX && activeTriggerField.dest.y == touchY) {
////				activeTriggerField.dest.x = touchX;
////				activeTriggerField.dest.y = touchY + 0.1;
////			}
////			else  {
////				activeTriggerField.dest.x = touchX;
////				activeTriggerField.dest.y = touchY;
////			}
////		}
////		if ( event.getAction() == MotionEvent.ACTION_UP ) {
////			if (activeTriggerField.dest.x != activeTriggerField.source.x && activeTriggerField.dest.y != activeTriggerField.source.y) {
////				activeTriggerField.dest.x = touchX;
////				activeTriggerField.dest.y = touchY;
////				game.stage.players.get(0).fields.add(activeTriggerField);
////			}
////
////			activeAnimation.resetProgress();
////			activeTriggerField = null;
////		}
////
//	}

}
