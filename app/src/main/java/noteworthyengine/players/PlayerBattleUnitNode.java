package noteworthyengine.players;

import noteworthyframework.Node;
import noteworthyframework.Unit;

/**
 * Created by eric on 2/2/16.
 */
public class PlayerBattleUnitNode extends Node {
    public static final String NAME = "PlayerBattleUnitNode";

    public PlayerBattleUnitNode(Unit unit) {
        super(NAME, unit);
        Node.instantiatePublicFieldsForUnit(unit, PlayerBattleUnitNode.class, this);
    }
}
