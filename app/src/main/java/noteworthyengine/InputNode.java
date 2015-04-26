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

    public void onDown(Vector2 touchPosition) {}
    public void onShowPress(Vector2 touchPosition) {}
    public void onSingleTapUp(Vector2 touchPosition) {}
    public void onScroll(Vector2 touchPosition, Vector2 touchPosition2, Vector2 touchScrollDeltas) {}
    public void onLongPress(Vector2 touchPosition) {}
    public void onFling(Vector2 touchPosition, Vector2 touchPosition2) {}
    public void onScale(float touchScale) {}

    public void update(int gesture, int mouseAction) {
    }

    public InputNode(Unit unit) {
        super(NAME, unit);
        Node.instantiatePublicFieldsForUnit(unit, InputNode.class, this);
    }
}
