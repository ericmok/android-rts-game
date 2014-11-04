package processors;

import game.androidgame2.GameCamera;
import game.androidgame2.GameInput;

/**
 * Created by eric on 11/3/14.
 */
public class MapScrollFunction {
    public static void process(int currentGesture, GameInput gameInput, GameCamera gameCamera) {
        if (currentGesture == GameInput.GESTURE_ON_SCROLL) {
            gameCamera.x += gameInput.touchScrollDeltas.x / gameCamera.scale;
            gameCamera.y += gameInput.touchScrollDeltas.y / gameCamera.scale;
        }

        if (currentGesture == GameInput.GESTURE_ON_SCALE) {
            gameCamera.scale *= gameInput.touchScale;
        }
    }
}
