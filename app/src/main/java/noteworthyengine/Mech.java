package noteworthyengine;

import noteworthyframework.Unit;

/**
 * Created by eric on 3/6/15.
 */
public class Mech extends Unit {

    public static final String NAME = "Mech";

    public MovementNode movementNode;
    public RenderNode renderNode;

    public Mech() {
        this.name = NAME;
        movementNode = new MovementNode(this);
        renderNode = new RenderNode(this);

        renderNode.width.v = 0.7f;
        renderNode.height.v = 0.7f;
    }

    public static Unit createMech() {
        return new Mech();
    }
}
