package noteworthyengine;

import java.util.ArrayList;

import noteworthyframework.*;
import structure.Game;
import structure.GameCamera;
import structure.GameInput;

/**
 * Created by eric on 4/10/15.
 */
public class InputSystem extends noteworthyframework.System {

    public QueueMutationList<InputNode> nodes = new QueueMutationList<>(127);

    final public Game game;

    public InputSystem(Game game) {
        this.game = game;
    }

    @Override
    public void addNode(Node node) {
        if (node.getClass() == InputNode.class || node instanceof InputNode) {
            nodes.queueToAdd((InputNode)node);
        }
    }

    @Override
    public void removeNode(Node node) {
        if (node.getClass() == InputNode.class || node instanceof InputNode) {
            nodes.queueToRemove((InputNode) node);
        }
    }

    @Override
    public void step(double ct, double dt) {
        int gesture = game.gameInput.takeCurrentGesture();

        for (int i = 0; i < nodes.size(); i++) {
            InputNode inputNode = nodes.get(i);
            int mouseEventAction = game.gameInput.takeCurrentMouseEventAction();

            switch(gesture) {
                case GameInput.GESTURE_NONE:
                    break;
                case GameInput.GESTURE_ON_DOWN:
                    inputNode.onDown(game.gameInput.touchPosition);
                    break;
                case GameInput.GESTURE_ON_SHOW_PRESS:
                    inputNode.onShowPress(game.gameInput.touchPosition);
                    break;
                case GameInput.GESTURE_ON_SINGLE_TAP_UP:
                    inputNode.onSingleTapUp(game.gameInput.touchPosition);
                    break;
                case GameInput.GESTURE_ON_SCROLL:
                    inputNode.onScroll(game.gameInput.touchPosition, game.gameInput.touchPosition2, game.gameInput.touchScrollDeltas);
                    break;
                case GameInput.GESTURE_ON_LONG_PRESS:
                    inputNode.onLongPress(game.gameInput.touchPosition);
                    break;
                case GameInput.GESTURE_ON_FLING:
                    inputNode.onFling(game.gameInput.touchPosition, game.gameInput.touchPosition2);
                    break;
                case GameInput.GESTURE_ON_SCALE:
                    inputNode.onScale(game.gameInput.touchScale);
                    break;
            }

            inputNode.update(gesture, mouseEventAction);
        }
    }

    @Override
    public void flushQueues() {
        nodes.flushQueues();
    }
}
