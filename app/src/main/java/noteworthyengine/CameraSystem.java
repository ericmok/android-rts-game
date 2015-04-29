package noteworthyengine;

import android.opengl.Matrix;

import noteworthyframework.*;
import structure.Game;

/**
 * Created by eric on 3/25/15.
 */
public class CameraSystem extends noteworthyframework.System {

    public QueueMutationList<CameraNode> nodes = new QueueMutationList<CameraNode>(127);

    private Game game;

    public CameraSystem(Game game) {
        this.game = game;
    }

    @Override
    public void addNode(Node node) {
        if (node.getClass() == CameraNode.class) {
            CameraNode cameraNode = (CameraNode)node;

//            if (!game.getGameRenderer().getCameras().contains(((CameraNode) node).camera)) {
//                game.getGameRenderer().addCamera(cameraNode.camera);
//            }

            nodes.queueToAdd(cameraNode);
        }
    }

    @Override
    public void removeNode(Node node) {
        if (node.getClass() == CameraNode.class) {
            CameraNode cameraNode = (CameraNode)node;

            game.getGameRenderer().removeCamera(cameraNode.camera);

            nodes.queueToRemove(cameraNode);
        }
    }

    @Override
    public void step(double ct, double dt) {
        for (int i = nodes.size() - 1; i >= 0; i--) {
            CameraNode cameraNode = nodes.get(i);

            Matrix.setIdentityM(cameraNode.camera.cameraMatrix, 0);

            // TODO: Refactor from:
            //Matrix.scaleM(cameraNode.camera.cameraMatrix, 0, cameraNode.camera.scale, cameraNode.camera.scale, 1);

            // To this...
            Matrix.scaleM(cameraNode.camera.cameraMatrix, 0, cameraNode.scale.v, cameraNode.scale.v, 1);
            cameraNode.camera.scale = cameraNode.scale.v;

            Matrix.translateM(cameraNode.camera.cameraMatrix, 0, -(float) cameraNode.coords.pos.x, -(float) cameraNode.coords.pos.y, -2f);

            cameraNode.camera.invalidate();
        }
    }

    @Override
    public void flushQueues() {
        nodes.flushQueues();
    }
}
