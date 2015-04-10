package noteworthyengine;

import noteworthyframework.Coords;
import utils.IntegerPtr;
import noteworthyframework.Node;
import noteworthyframework.Unit;

/**
 * Created by eric on 3/20/15.
 */
public class GridNode extends Node {
    public static final String NAME = "gridNode";

    public Coords coords;
    public IntegerPtr gridX;
    public IntegerPtr gridY;

    public SeparationNode _separationNode = null;
    public BattleNode _battleNode = null;

    public GridNode(Unit unit, SeparationNode separationNode, BattleNode battleNode) {
        super(NAME, unit);
        Node.instantiatePublicFieldsForUnit(unit, GridNode.class, this);

        this._separationNode = separationNode;
        this._battleNode = battleNode;
    }
}
