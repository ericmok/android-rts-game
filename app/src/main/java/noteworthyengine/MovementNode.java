package noteworthyengine;

import utils.Vector2;

/**
 * Created by eric on 3/6/15.
 */
public class MovementNode extends Node {

    public Coords coords;

    public Vector2 velocity;
    public Vector2 acceleration;

    public Vector2 fieldForce;
    public Vector2 formationForce;
    public Vector2 separationForce;
    public Vector2 enemyAttractionForce;

    public double crowdSpeed = 1;

    public MovementNode(Unit unit) {
        super(unit);
        Node.instantiatePublicFieldsForUnit(unit, MovementNode.class, this);
    }
}
