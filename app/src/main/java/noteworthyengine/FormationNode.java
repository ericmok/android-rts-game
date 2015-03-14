package noteworthyengine;

import java.util.ArrayList;

import noteworthyframework.Coords;
import noteworthyframework.Node;
import noteworthyframework.Unit;
import utils.Vector2;

/**
 * Created by eric on 3/13/15.
 */
public class FormationNode extends Node {

    public static final String NAME = "formationNode";

    public Coords coords;
    public Vector2 formationForce;

    public FormationNode leader;
    public ArrayList<Vector2> openPositions = new ArrayList<Vector2>(8);

    public FormationNode(Unit unit) {
        super(NAME, unit);
        Node.instantiatePublicFieldsForUnit(unit, FormationNode.class, this);
    }
}
