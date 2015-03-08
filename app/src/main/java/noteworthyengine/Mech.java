package noteworthyengine;

/**
 * Created by eric on 3/6/15.
 */
public class Mech extends Unit {

    public static final String NAME = "Mech";

    public Mech() {
        this.name = NAME;
        MovementNode movementNode = new MovementNode(this);
        RenderNode renderNode = new RenderNode(this);

        renderNode.width.v = 0.7;
        renderNode.height.v = 0.7;
    }

    public static Unit createMech() {
        return new Mech();
    }
}
