package noteworthyengine.units;

import noteworthyengine.CameraNode;
import noteworthyframework.Unit;
import structure.GameCamera;

/**
 * Created by eric on 3/25/15.
 */
public class CameraUnit extends Unit {

    public CameraNode cameraNode = new CameraNode(this);

    public CameraUnit(GameCamera gameCamera, float scale) {
        this.name = this.getClass().getSimpleName();
        cameraNode.camera = gameCamera;
        cameraNode.camera.scale = scale;
    }
}
