package processors;

import game.androidgame2.GameCamera;
import game.androidgame2.GameInput;

/**
 * Created by eric on 11/3/14.
 */
public class MapScrollFunction {

    /**
     * touchScrollDelta's are in screen coordinates. They are scaled up to larger world coordinates
     * and mutate the game camera position which are also in world coordinates.
     *
     * The map is also multiplicatively scaled by gameInput's scaling value
     *
     * @param currentGesture
     * @param gameInput
     * @param gameCamera
     */
    public static void apply(int currentGesture, GameInput gameInput, GameCamera gameCamera) {
        if (currentGesture == GameInput.GESTURE_ON_SCROLL) {
            gameCamera.x += gameInput.touchScrollDeltas.x / gameCamera.scale;
            gameCamera.y += gameInput.touchScrollDeltas.y / gameCamera.scale;
        }

        if (currentGesture == GameInput.GESTURE_ON_SCALE) {
            gameCamera.scale *= gameInput.touchScale;
        }
    }
}
