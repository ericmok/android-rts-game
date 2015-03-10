package structure;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import utils.Vector2;

/**
 * Created by eric on 11/1/14.
 * Preprocesses and routes glSurfaceView UI events to the game loop
 */
public class GameInput extends ScaleGestureDetector.SimpleOnScaleGestureListener
                                implements GestureDetector.OnGestureListener {

    public static final int GESTURE_NONE = -1;
    public static final int GESTURE_ON_DOWN = 0;
    public static final int GESTURE_ON_SHOW_PRESS = 1;
    public static final int GESTURE_ON_SINGLE_TAP_UP = 2;
    public static final int GESTURE_ON_SCROLL = 3;
    public static final int GESTURE_ON_LONG_PRESS = 4;
    public static final int GESTURE_ON_FLING = 5;
    public static final int GESTURE_ON_SCALE = 6;

    public static float INTIAL_ASPECT_RATIO_VALUE = (1920.0f / 1080);
    public static float INITIAL_WIDTH = 1920.0f;
    public static float INITIAL_HEIGHT = 1080.0f;

    private float aspectRatio = INTIAL_ASPECT_RATIO_VALUE;
    private float screenWidth = INITIAL_WIDTH;
    private float screenHeight = INITIAL_HEIGHT;

    // Direct Access
    public MotionEvent motionEvent = null;

    public int currentMotionEventAction = -1;

    public boolean touchDown = false;

    /// Primary touch position if gesture is parameterized by 1 MouseEvent
    public Vector2 touchPosition = new Vector2();

    /// Secondary touch param for gestures is parameterized by 2 MouseEvent's
    public Vector2 touchPosition2 = new Vector2();

    public Vector2 touchScrollDeltas = new Vector2();

    /** Deprecated. Pending removal. Don't use as it does not work.
     * The touch position recorded when a tap down last occurred */
    public Vector2 lastTouchDown = new Vector2();

    /** The touch position recorded when a touch up last occurred */
    public Vector2 lastTouchUp = new Vector2();

    public float touchScale= 1.0f;

    private int currentGesture = GESTURE_NONE;

    public GameInput() {
    }

    public void setScreenDimensions(float width, float height) {
        this.screenWidth = width;
        this.screenHeight = height;
        this.aspectRatio = width / height;
    }

    public void getCoordsTranslatedAndNormalized(Vector2 coords, float ex, float ey) {
        getCoordsTranslatedToCenterOfScreen(coords, ex, ey);
        getCoordsNormalizedToScreen(coords, (float)coords.x, (float)coords.y);
    }

    /**
     * MotionEvent origin is located in the top-left (4th quadrant).
     * The router translates origin to the center of the screen.
     */
    public void getCoordsTranslatedToCenterOfScreen(Vector2 coords, float ex, float ey) {

        // Translate origin to center of the screen
        float mx = ex - (screenWidth / 2);
        //float my = ( gameGLSurfaceView.getHeight() - event.getY() ) - (gameGLSurfaceView.getHeight() / 2);
        //float invertedY = - ey + screenHeight;
        //float my = invertedY - (screenHeight/ 2);
        float my = ey - (screenHeight / 2);

        coords.x = mx;
        coords.y = my;
    }

    /**
     * Normalize pixel coords to [-1, 1] and [-aspectRatio, aspectRatio]
     * The y values are inverted during normalization
     * @param coords
     */
    public void getCoordsNormalizedToScreen(Vector2 coords, float mx, float my) {

        // Invert
        my = -my;

        if (screenWidth > screenHeight) {
            // Orientation Landscape
            mx = aspectRatio * (2 * mx / screenWidth);
            my = (2 * my / screenHeight);
        }
        else {
            // Portrait Landscape
            mx = (2 * mx / screenWidth);
            my = aspectRatio * (2 * my / screenHeight);
        }

        coords.x = mx;
        coords.y = my;
    }


    public boolean isTouchDown() {
        return touchDown;
    }

    public synchronized void setCurrentGesture(int gesture) {
        this.currentGesture = gesture;
    }

    /**
     * Returns the gesture and resets gesture to none atomically
     * @return
     */
    public synchronized int takeCurrentGesture() {
        int ret = this.currentGesture;
        this.currentGesture = GESTURE_NONE;
        return ret;
    }

    /**
     * Returns the mouse event action and resets it to -1
     * @return
     */
    public synchronized int takeCurrentMouseEventAction() {
        int ret = this.currentMotionEventAction;
        this.currentMotionEventAction = -1;
        return ret;
    }

    /**
     * TODO: Rearchitect this, might not be needed
     */
    public void onTouchEvent(MotionEvent event) {
        //gameLoop.onTouchEvent(event);
        motionEvent = event;

        // TODO: This may be called duplicate
       // getCoordsTranslatedAndNormalized(touchPosition, event.getX(), event.getY());

        this.currentMotionEventAction = event.getAction();

        if (currentMotionEventAction == MotionEvent.ACTION_DOWN) {
            touchDown = true;
            //lastTouchDown.copy(touchPosition);
            Log.v("GAMEINPUT", "down");
        }

        if (currentMotionEventAction == MotionEvent.ACTION_MOVE) {
            touchDown = true;
            Log.v("GAMEINPUT", "move");
        }

        // Unclear if other actions will trigger flag to false
        if (currentMotionEventAction == MotionEvent.ACTION_UP) {
            touchDown = false;
            //lastTouchUp.copy(touchPosition);
            Log.v("GAMEINPUT", "up");
        }
    }

    @Override
    public boolean onDown(MotionEvent e) {
        //return gameLoop.gestureOnDown(e);
        getCoordsTranslatedAndNormalized(touchPosition, e.getX(), e.getY());
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        //gameLoop.gestureOnShowPress(e);
        getCoordsTranslatedAndNormalized(touchPosition, e.getX(), e.getY());
        setCurrentGesture(GESTURE_ON_SHOW_PRESS);
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        //return gameLoop.gestureOnSingleTapUp(e);
        getCoordsTranslatedAndNormalized(touchPosition, e.getX(), e.getY());
        setCurrentGesture(GESTURE_ON_SINGLE_TAP_UP);
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        //return gameLoop.gestureOnScroll(e1, e2, distanceX, distanceY);
        getCoordsTranslatedAndNormalized(touchPosition, e1.getX(), e1.getY());
        getCoordsTranslatedAndNormalized(touchPosition2, e2.getX(), e2.getY());

        // Since we did not invert the Y with normalization
        getCoordsNormalizedToScreen(touchScrollDeltas, distanceX, distanceY);
        setCurrentGesture(GESTURE_ON_SCROLL);
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        //gameLoop.gestureOnLongPress(e);
        getCoordsTranslatedAndNormalized(touchPosition, e.getX(), e.getY());
        setCurrentGesture(GESTURE_ON_LONG_PRESS);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        //return gameLoop.gestureOnFling(e1, e2, velocityX, velocityY);
        getCoordsTranslatedAndNormalized(touchPosition, e1.getX(), e1.getY());
        getCoordsTranslatedAndNormalized(touchPosition2, e2.getX(), e2.getY());
        setCurrentGesture(GESTURE_ON_FLING);
        return true;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        //return gameLoop.onScale(detector);
        touchScale = detector.getScaleFactor();
        setCurrentGesture(GESTURE_ON_SCALE);
        return true;
    }
}
