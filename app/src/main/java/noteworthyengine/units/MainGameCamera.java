package noteworthyengine.units;

import noteworthyengine.FieldCameraNode;
import noteworthyframework.Unit;
import structure.GameCamera;

/**
 * Created by eric on 4/29/15.
 */
public class MainGameCamera extends CameraUnit {

    public FieldCameraNode fieldCameraNode;

    public MainGameCamera(int index, GameCamera gameCamera, float scale, float zoomScale) {
        super(index, gameCamera, scale);

        fieldCameraNode = new FieldCameraNode(this, scale, zoomScale);
    }
}
