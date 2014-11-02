package game.androidgame2;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

/**
 * SurfaceView handles UI thread.
 */
public class GameGLSurfaceView extends GLSurfaceView {

	private Game game;
	
	private GestureDetector gestureDetector;

    private ScaleGestureDetector scaleGestureDetector;

	public GameGLSurfaceView(Context context, Game game) {
		super(context);
		
		this.game = game;

		gestureDetector = new GestureDetector(this.getContext(), game.gameInput);
		scaleGestureDetector = new ScaleGestureDetector(this.getContext(), game.gameInput);

		this.setEGLContextClientVersion(2);
	}
	
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			this.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
	                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
	                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
	                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
	                | View.SYSTEM_UI_FLAG_FULLSCREEN
	                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
		}
	}
	

	public boolean onTouchEvent(MotionEvent event) {
		//if (event.getAction() == MotionEvent.ACTION_DOWN) {
//			queueEvent(new Runnable() {
//				public void run() {
//
//				}
//			});
		//}

		//float x = event.getX() - this.getWidth() / 2;

		//float x = (event.getX() - this.getWidth() / 2);
		//float y = event.getY() - this.getHeight() / 2;

		// What is this?
//		float y = this.getHeight() - event.getY() - this.getHeight() / 2;
//		x = (x / this.getWidth() ) * 32;
//		y = game.getGameRenderer().getAspectRatio() * 16 * y / (this.getHeight() / 2);

		//Log.i("GameGLSurfaceView TouchEvent", "Touch at: " + x + "," + y);

        super.onTouchEvent(event);

//		game.onTouchEvent(event);

        gestureDetector.onTouchEvent(event);
        scaleGestureDetector.onTouchEvent(event);


		return true;
	}

}
