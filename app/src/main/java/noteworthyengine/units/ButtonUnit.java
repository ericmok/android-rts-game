package noteworthyengine.units;

import noteworthyengine.ButtonSystem;
import noteworthyengine.CameraNode;
import noteworthyengine.InputNode;
import noteworthyengine.InputSystem;
import noteworthyengine.RenderNode;
import noteworthyframework.Unit;
import utils.Vector2;

/**
 * Created by eric on 3/23/15.
 */
public class ButtonUnit extends Unit {

    public InputNode inputNode;
    //public ButtonSystem.ButtonNode buttonNode;
    public RenderNode renderNode;

    Vector2 temp = new Vector2();

    public ButtonUnit() {
        this.name = this.getClass().getSimpleName();

        inputNode = new InputNode(this) {
            @Override
            public void onSingleTapUp(InputSystem inputSystem, CameraNode cameraNode, Vector2 touchPosition) {
                super.onSingleTapUp(inputSystem, cameraNode, touchPosition);
                temp.copy(touchPosition);
                cameraNode.camera.unProject(temp);

                if (temp.x >= renderNode.coords.pos.x - renderNode.width.v/2 &&
                        temp.x <= renderNode.coords.pos.x + renderNode.width.v/2 &&
                        temp.y >= renderNode.coords.pos.y - renderNode.height.v/2 &&
                        temp.y <= renderNode.coords.pos.y + renderNode.height.v/2) {
                    onTap();
                }
            }
        };

        //buttonNode = new ButtonSystem.ButtonNode(this);

        renderNode = new RenderNode(this);
        renderNode.z.v = 2;
        renderNode.coords.rot.setDegrees(0);
    }

    public void onTap() {

    }
}
