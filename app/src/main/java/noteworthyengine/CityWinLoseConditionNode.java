package noteworthyengine;

import noteworthyengine.players.PlayerUnitPtr;
import noteworthyframework.Node;
import noteworthyframework.Unit;

/**
 * Created by eric on 5/13/15.
 */
public class CityWinLoseConditionNode extends Node {

    public static final String NAME = "cityWinLoseConditionNode";

    public PlayerUnitPtr playerUnitPtr;

    public CityWinLoseConditionNode(Unit unit) {
        super(CityWinLoseConditionNode.class, unit);
        Node.instantiatePublicFieldsForUnit(unit, CityWinLoseConditionNode.class, this);
    }

}
