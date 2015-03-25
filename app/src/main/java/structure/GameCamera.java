package structure;

import android.opengl.Matrix;

import noteworthyengine.GameSettings;
import utils.Vector2;

/**
 * Created by eric on 11/3/14.
 */
public class GameCamera extends Vector2 {

    public static float INITIAL_ASPECT_RATIO_VALUE = (1920.0f / 1080);

    public float[] projectionMatrix = new float[16];
    public float[] cameraMatrix = new float[16];
    public float[] viewProjectionMatrix = new float[16];

    public float scale = GameSettings.UNIT_LENGTH_MULTIPLIER;
    public float aspectRatio = INITIAL_ASPECT_RATIO_VALUE;

    private float leftBounds = -INITIAL_ASPECT_RATIO_VALUE;
    private float rightBounds = INITIAL_ASPECT_RATIO_VALUE;
    private float bottomBounds = -1;
    private float topBounds = 1;
    private float near = 1;
    private float far = 1000;

    public GameCamera() {
        Matrix.setIdentityM(projectionMatrix, 0);
        Matrix.setIdentityM(cameraMatrix, 0);
        //Matrix.scaleM(cameraMatrix, 0, 0.07f, 0.07f, 1);
        Matrix.translateM(cameraMatrix, 0, 0, 0, -2);
        Matrix.setIdentityM(viewProjectionMatrix, 0);
    }

    public void invalidate() {
        Matrix.multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, cameraMatrix, 0);
    }

    public void configure(float width, float height) {
        aspectRatio = width / height;
        float leftBounds;
        float rightBounds;
        float bottomBounds;
        float topBounds;
        float near;
        float far;
        /*
         * Landscape and portrait mode will change how big a unit length looks on the screen.
         * The game is tailored for landscape. The dimensions will wind up being the reciprocal
         * of the aspect ratio larger in portrait mode. Have to rescale to normalization.
         *
         * Landscape:
         * Left and right bounds: [-asp, +asp]
         * Bottom and top bounds: [-1, +1]
         *
         * Portrait:
         * Left and right bounds: [-1, +1]
         * Bottom and top bounds: [-asp, +asp]
         */
        if (aspectRatio > 1.0f) {

            // Landscape
            leftBounds = aspectRatio * -1.0f;
            rightBounds = aspectRatio * 1.0f;

            bottomBounds = -1.0f;
            topBounds = 1.0f;
        }
        else {

            // Portrait
            bottomBounds = aspectRatio * -1.0f;
            topBounds = aspectRatio * 1.0f;

            leftBounds = -1.0f;
            rightBounds = 1.0f;
        }

        near = 1.0f;
        far = 1000.0f;

        Matrix.orthoM(projectionMatrix, 0, leftBounds, rightBounds, bottomBounds, topBounds, near, far);
        this.invalidate();
    }

    /**
     * Set the vector in screen coords to world coords using camera parameters
     * @param screenCoords
     */
    public void setScreenAsWorldCoords(Vector2 screenCoords) {

        // Reminder: The scale is less than 1.0 (ex. 0.007)
        screenCoords.scale(1.0 / this.scale, 1.0 / this.scale);
        screenCoords.translate(this.x, this.y);
    }

    /**
     * Touch coordinates are normalized [-1, 1]
     * These coordinates are transformed to world space [x, y] where x > 1 and y > 1
     * @param output
     * @param screenCoords
     */
    public void getScreenToWorldCoords(Vector2 output, Vector2 screenCoords) {
        output.copy(screenCoords);
        setScreenAsWorldCoords(output);
    }
}
