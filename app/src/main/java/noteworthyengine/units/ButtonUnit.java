package noteworthyengine.units;

import noteworthyengine.ButtonSystem;
import noteworthyengine.RenderNode;
import noteworthyframework.Unit;

/**
 * Created by eric on 3/23/15.
 */
public class ButtonUnit extends Unit {

    public ButtonSystem.ButtonNode buttonNode;
    public RenderNode renderNode;

    public ButtonUnit() {
        this.name = this.getClass().getSimpleName();

        buttonNode = new ButtonSystem.ButtonNode(this);

        renderNode = new RenderNode(this);
        renderNode.z.v = 2;
        renderNode.coords.rot.setDegrees(0);
    }
}
