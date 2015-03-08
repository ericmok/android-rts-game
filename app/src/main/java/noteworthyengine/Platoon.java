package noteworthyengine;

import java.util.ArrayList;

import model.Denormalizable;
import model.Troop;

/**
 * Created by eric on 3/7/15.
 */
public class Platoon extends Unit {
    public static final String NAME = "Troopy";

    public static final ArrayList<Class> NODES = new ArrayList<Class>() {{
        this.add(MovementNode.class);
        this.add(RenderNode.class);
    }};

    public Platoon() {
        this.name = NAME;

        MovementNode movementNode = new MovementNode(this);
        RenderNode renderNode = new RenderNode(this);

        double size = Math.random() > 0.5 ? 1 : 0.7;
        renderNode.width.v = size;
        renderNode.height.v = size;
    }
}
