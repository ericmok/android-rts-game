package game.androidgame2;

/**
 * Created by eric on 11/3/14.
 */
public class GameCamera extends Vector2 {
    public double scale = GameSettings.UNIT_LENGTH_MULTIPLIER;

    private Vector2 temp = new Vector2();

    /**
     * Touch coordinates are normalized [-1, 1]
     * These coordinates are transformed to world space [x, y] where x > 1 and y > 1
     * @param output
     * @param touchCoordinates
     */
    public void getTouchToWorldCords(Vector2 output, Vector2 touchCoordinates) {

        // Reminder: The scale is less than 1.0 (ex. 0.007)
        Vector2.scaled(temp, touchCoordinates, 1.0/this.scale);
        temp.translate(this.x, this.y);
        output.copy(temp);
    }
}
