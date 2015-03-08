package noteworthyengine;

import java.util.ArrayList;

import model.Denormalizable;
import model.Troop;

/**
 * Created by eric on 3/7/15.
 */
public class Troopy extends Unit {
    public static final String NAME = "Troopy";

    public static final ArrayList<Class> NODES = new ArrayList<Class>() {{
        this.add(MovementNode.class);
        this.add(RenderNode.class);
    }};

    public Troopy() {
        this.name = NAME;

        MovementNode movementNode = new MovementNode(this);
        RenderNode renderNode = new RenderNode(this);
    }
}
