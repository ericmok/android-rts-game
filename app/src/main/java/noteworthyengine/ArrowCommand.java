package noteworthyengine;

import android.graphics.Color;

import noteworthyframework.Unit;
import structure.Sprite2dDef;

/**
 * Created by eric on 3/8/15.
 */
public class ArrowCommand extends Unit {

    public static final String NAME = "ArrowCommand";

    public static final float WIDTH = 3f;
    public static final float HEIGHT = 3f;

    public ArrowCommand() {
        this.name = NAME;

        FieldNode fieldNode = new FieldNode(this);
        fieldNode._fieldArrowNode = new FieldNode.FieldArrowNode(this);

        RenderNode renderNode = new RenderNode(this);
        renderNode.animationName = Sprite2dDef.ANIMATION_TRIGGER_FIELDS_EXISTING;
        renderNode.color.v = Color.WHITE;
        renderNode.width.v = WIDTH;
        renderNode.height.v = HEIGHT;
    }
}
