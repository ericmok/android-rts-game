package noteworthyengine;

import noteworthyengine.battle.BattleNode;
import noteworthyframework.Coords;
import noteworthyframework.Node;
import noteworthyframework.Unit;
import utils.IntegerPtr;

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
        super(GridNode.class, unit);
        Node.instantiatePublicFieldsForUnit(unit, GridNode.class, this);

        this._separationNode = separationNode;
        this._battleNode = battleNode;
    }
}
