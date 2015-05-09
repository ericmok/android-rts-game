package noteworthyengine;

import java.util.ArrayList;

import noteworthyframework.*;
import structure.Game;
import structure.GameCamera;
import structure.GameInput;
import utils.Vector2;

/**
 * Created by eric on 4/10/15.
 */
public class InputSystem extends noteworthyframework.System {

    public QueueMutationList<InputNode> nodes = new QueueMutationList<>(127);

    final public Game game;

    final public CameraSystem cameraSystem;

    public InputSystem(Game game, CameraSystem cameraSystem) {
        this.game = game;
        this.cameraSystem = cameraSystem;
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

            CameraNode cameraNode = null;

//            for (int c = 0; c < cameraSystem.nodes.size(); c++) {
//                if (inputNode.cameraIndex.v == cameraSystem.nodes.get(c).index.v) {
//                    cameraNode = cameraSystem.nodes.get(c);
//                }
//            }

            // Get the camera as indexed by cameraSystem
            if (inputNode.cameraIndex.v >= 0 && inputNode.cameraIndex.v < cameraSystem.nodes.size()) {
                cameraNode = cameraSystem.nodes.get(inputNode.cameraIndex.v);
            }
            else {
                continue;
            }

            int mouseEventAction = game.gameInput.takeCurrentMouseEventAction();

            switch(gesture) {
                case GameInput.GESTURE_NONE:
                    break;
                case GameInput.GESTURE_ON_DOWN:
                    inputNode.onDown(cameraNode, game.gameInput.touchPosition);
                    break;
                case GameInput.GESTURE_ON_SHOW_PRESS:
                    inputNode.onShowPress(cameraNode, game.gameInput.touchPosition);
                    break;
                case GameInput.GESTURE_ON_SINGLE_TAP_UP:
                    inputNode.onSingleTapUp(cameraNode, game.gameInput.touchPosition);
                    break;
                case GameInput.GESTURE_ON_SCROLL:
                    inputNode.onScroll(cameraNode, game.gameInput.touchPosition, game.gameInput.touchPosition2, game.gameInput.touchScrollDeltas);
                    break;
                case GameInput.GESTURE_ON_LONG_PRESS:
                    inputNode.onLongPress(cameraNode, game.gameInput.touchPosition);
                    break;
                case GameInput.GESTURE_ON_FLING:
                    inputNode.onFling(cameraNode, game.gameInput.touchPosition, game.gameInput.touchPosition2);
                    break;
                case GameInput.GESTURE_ON_SCALE:
                    inputNode.onScale(cameraNode, game.gameInput.touchScale);
                    break;
            }

            inputNode.update(cameraNode, gesture, mouseEventAction);
        }
    }

    @Override
    public void flushQueues() {
        nodes.flushQueues();
    }
}
