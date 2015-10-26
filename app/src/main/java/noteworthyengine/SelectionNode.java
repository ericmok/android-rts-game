package noteworthyengine;

import noteworthyframework.Coords;
import noteworthyframework.GamerPtr;
import noteworthyframework.Node;
import noteworthyframework.Unit;
import utils.IntegerPtr;

/**
 * Created by eric on 10/15/15.
 */
public class SelectionNode extends Node {
    public GamerPtr gamer;

    public Coords coords;
    public IntegerPtr gridX;
    public IntegerPtr gridY;

    public IntegerPtr isSelected = new IntegerPtr() {{
        v = 0;
    }};

    public SelectionNode(Unit unit) {
        super("selectionNode", unit);
        this._name = "selectionNode";
        Node.instantiatePublicFieldsForUnit(unit, SelectionNode.class, this);
    }

    public void reset() {
        this.isSelected.v = 0;
    }
}
