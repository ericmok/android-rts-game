package noteworthyengine.units;

import noteworthyengine.FieldCameraNode;
import noteworthyframework.Unit;
import structure.GameCamera;

/**
 * Created by eric on 4/29/15.
 */
public class MainGameCamera extends CameraUnit {

    public FieldCameraNode fieldCameraNode;

    public MainGameCamera(float scale, float zoomScale) {
        super(scale);

        fieldCameraNode = new FieldCameraNode(this, scale, zoomScale);
    }
}
