package structure;

import noteworthyengine.GameSettings;
import utils.Vector2;

/**
 * Created by eric on 11/3/14.
 */
public class GameCamera extends Vector2 {
    public double scale = GameSettings.UNIT_LENGTH_MULTIPLIER;
    public double aspectRatio = 1.0;

    private Vector2 temp = new Vector2();

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
