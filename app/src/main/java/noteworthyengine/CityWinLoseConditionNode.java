package noteworthyengine;

import noteworthyframework.GamerPtr;
import noteworthyframework.Node;
import noteworthyframework.Unit;

/**
 * Created by eric on 5/13/15.
 */
public class CityWinLoseConditionNode extends Node {

    public static final String NAME = "cityWinLoseConditionNode";

    public GamerPtr gamer;

    public CityWinLoseConditionNode(Unit unit) {
        super(NAME, unit);
        Node.instantiatePublicFieldsForUnit(unit, CityWinLoseConditionNode.class, this);
    }

}
