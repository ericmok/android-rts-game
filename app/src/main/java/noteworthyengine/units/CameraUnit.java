package noteworthyengine.units;

import noteworthyengine.CameraNode;
import noteworthyframework.Unit;
import structure.GameCamera;

/**
 * Created by eric on 3/25/15.
 */
public class CameraUnit extends Unit {

    public CameraNode cameraNode = new CameraNode(this);

    public CameraUnit(int index, GameCamera gameCamera, float scale) {
        this.name = this.getClass().getSimpleName();
        cameraNode.index.v = index;
        cameraNode.camera = gameCamera;
        cameraNode.camera.scale = scale;
        cameraNode.scale.v = scale;
    }
}
