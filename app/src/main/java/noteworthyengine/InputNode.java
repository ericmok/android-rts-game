package noteworthyengine;

import noteworthyframework.Node;
import noteworthyframework.Unit;
import structure.GameCamera;
import utils.IntegerPtr;
import utils.Vector2;

/**
 * Created by eric on 4/10/15.
 */
public class InputNode extends Node {

    public static final String NAME = "inputNode";

    public IntegerPtr cameraIndex = new IntegerPtr() {{ v = 0; }};

    public void onDown(CameraNode cameraNode, Vector2 touchPosition) {}
    public void onShowPress(CameraNode cameraNode, Vector2 touchPosition) {}
    public void onSingleTapUp(CameraNode cameraNode, Vector2 touchPosition) {}
    public void onScroll(CameraNode cameraNode, Vector2 touchPosition, Vector2 touchPosition2, Vector2 touchScrollDeltas) {}
    public void onLongPress(CameraNode cameraNode, Vector2 touchPosition) {}
    public void onFling(CameraNode cameraNode, Vector2 touchPosition, Vector2 touchPosition2) {}
    public void onScale(CameraNode cameraNode, float touchScale) {}

    public void update(CameraNode cameraNode, int gesture, int mouseAction) {
    }

    public InputNode(Unit unit) {
        super(NAME, unit);
        Node.instantiatePublicFieldsForUnit(unit, InputNode.class, this);
    }
}
