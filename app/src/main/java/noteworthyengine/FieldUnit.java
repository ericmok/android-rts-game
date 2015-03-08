package noteworthyengine;

import structure.DrawList2DItem;

/**
 * Created by eric on 3/8/15.
 */
public class FieldUnit extends Unit {

    public static final String NAME = "FieldUnit";

    public FieldControl fieldControl = new FieldControl();

    public FieldUnit() {
        this.name = NAME;

        RenderNode renderNode = new RenderNode(this);
        renderNode.animationName = DrawList2DItem.ANIMATION_TRIGGER_FIELDS_EXISTING;
        renderNode.width.v = 1;
        renderNode.height.v = 1;

        FieldNode fieldNode = new FieldNode(this);
        fieldNode.isFieldControl.v = 1;
    }

}
