package noteworthyengine;

import android.graphics.Color;

import structure.DrawList2DItem;
import utils.Vector2;

/**
 * Created by eric on 3/8/15.
 */
public class ArrowCommand extends Unit {

    public static final String NAME = "ArrowCommand";

    public ArrowCommand() {
        this.name = NAME;

        FieldNode fieldNode = new FieldNode(this);
        fieldNode._fieldArrowNode = new FieldNode.FieldArrowNode(this);

        RenderNode renderNode = new RenderNode(this);
        renderNode.animationName = DrawList2DItem.ANIMATION_TRIGGER_FIELDS_EXISTING;
        renderNode.color.v = Color.WHITE;
    }
}
