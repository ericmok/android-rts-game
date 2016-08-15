package noteworthyengine;

import noteworthyframework.Node;
import noteworthyframework.Unit;
import utils.IntegerPtr;
import utils.Vector2;

/**
 * Created by eric on 4/10/15.
 */
public class InputNode extends Node {

    public static final String NAME = "inputNode";

    public IntegerPtr cameraIndex = new IntegerPtr() {{ v = 0; }};

    public void onDown(InputSystem inputSystem, CameraNode cameraNode, Vector2 touchPosition) {}
    public void onShowPress(InputSystem inputSystem, CameraNode cameraNode, Vector2 touchPosition) {}
    public void onSingleTapUp(InputSystem inputSystem, CameraNode cameraNode, Vector2 touchPosition) {}
    public void onScroll(InputSystem inputSystem, CameraNode cameraNode, Vector2 touchPosition, Vector2 touchPosition2, Vector2 touchScrollDeltas) {}
    public void onLongPress(InputSystem inputSystem, CameraNode cameraNode, Vector2 touchPosition) {}
    public void onFling(InputSystem inputSystem, CameraNode cameraNode, Vector2 touchPosition, Vector2 touchPosition2) {}
    public void onScale(InputSystem inputSystem, CameraNode cameraNode, float touchScale) {}

    public void update(InputSystem inputSystem, CameraNode cameraNode, int gesture, int mouseAction) {
    }

    public InputNode(Unit unit) {
        super(InputNode.class, unit);
        Node.instantiatePublicFieldsForUnit(unit, InputNode.class, this);
    }
}
