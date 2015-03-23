package structure;

import android.os.SystemClock;

import noteworthyengine.GameSettings;

public class GameLoop implements Runnable {
	
	private Game game;
	
	private Object pauseLock;	
	
	private boolean isFinished = false;
	private boolean isPaused = true; // Loop is started in paused state

    private boolean firstRun = true;
	
	public final static long TARGET_MILLISEC_PER_FRAME = 12;

    public double accumulator = 0;

    private long originTick = 0;
	private long previousTick = 0;
	private long tickDifference = 0;

    public static final double TIME_PER_FRAME = 1.0 / TARGET_MILLISEC_PER_FRAME;
    public double accumulatedFrames = 0;
	
	private final void debugLog(String a, String b) {
		//Log.i(a, b);
	}

	public GameLoop(Game game) {
		this.game = game;

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
                this.performGameLogicV3(currentTick, TARGET_MILLISEC_PER_FRAME);

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

		} // End Game Loop
		
		debugLog("GameLoop", "Is finished.");
	}

    private void performGameLogicV3(long currentTick, long elapsedTime) {
        accumulatedFrames += TIME_PER_FRAME;

        if (firstRun) {
            game.noteworthyEngine.initialize();
            firstRun = false;
        }
        game.noteworthyEngine.step(accumulatedFrames, TIME_PER_FRAME);
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
}
