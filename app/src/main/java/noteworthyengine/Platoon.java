package noteworthyengine;

import android.graphics.Color;

import java.util.ArrayList;

import model.Denormalizable;
import model.Troop;
import structure.DrawList2DItem;

/**
 * Created by eric on 3/7/15.
 */
public class Platoon extends Unit {
    public static final String NAME = "Troopy";

    public Platoon() {
        this.name = NAME;

        MovementNode movementNode = new MovementNode(this);

        RenderNode renderNode = new RenderNode(this);
        float size = Math.random() > 0.5f ? 1f : 0.7f;
        renderNode.animationName = DrawList2DItem.ANIMATION_TROOPS_IDLING;
        renderNode.animationProgress.v = 0;
        renderNode.width.v = size;
        renderNode.height.v = size;
        renderNode.color.v = Color.WHITE;

        FieldNode fieldNode = new FieldNode(this);
        fieldNode.isFieldControl.v = 0;
    }
}
