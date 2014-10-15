package game.androidgame2;

import android.view.MotionEvent;

/**
 * Input can be processed concurrently with both graphics and gameLoop threads running.
 *
 */
public class InputProcessor {
	
	Game game;
	
	public InputProcessor(Game game) {
		this.game = game;
	}
	
	public void touchEvent(MotionEvent event, float x, float y) {
		
	}
}
