package systems;

import utils.Vector2;

/**
 * Created by eric on 3/6/15.
 */
public class MovementNode extends Node {
    public Vector2 fieldForce = new Vector2();
    public Vector2 formationForce = new Vector2();
    public Vector2 separationForce = new Vector2();

    public double crowdSpeed = 1;
}
