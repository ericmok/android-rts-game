package noteworthyengine;

import android.graphics.Color;

import structure.DrawList2DItem;
import utils.Vector2;

/**
 * Created by eric on 3/8/15.
 */
public class FieldControl extends Unit {

    public static final String NAME = "FieldControl";

    public Vector2 position = new Vector2();
    public Vector2 direction = new Vector2();

    public FieldControl() {
        this.name = NAME;

        RenderNode renderNode = new RenderNode(this);
        renderNode.animationName = DrawList2DItem.ANIMATION_TRIGGER_FIELDS_EXISTING;
        renderNode.color.v = Color.WHITE;

        FieldNode fieldNode = new FieldNode(this);
        fieldNode.isFieldControl.v = 1;
    }
}
