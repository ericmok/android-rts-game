package noteworthyengine.units;

import noteworthyengine.FieldCameraNode;
import noteworthyframework.Unit;
import structure.GameCamera;

/**
 * Created by eric on 4/29/15.
 */
public class MainGameCamera extends CameraUnit {

    public FieldCameraNode fieldCameraNode = new FieldCameraNode(this);

    public MainGameCamera(int index, GameCamera gameCamera, float scale) {
        super(index, gameCamera, scale);
    }
}
