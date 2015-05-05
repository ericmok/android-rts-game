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
            cameraNode.camera = game.getGameRenderer().registerNewOrthoCamera();
            cameraNode.index.v = cameraNode.camera.getCameraIndex();
            cameraNode.camera.setView((float)cameraNode.coords.pos.x, (float)cameraNode.coords.pos.y, (float)cameraNode.scale.v);

            nodes.queueToAdd(cameraNode);
        }
    }

    @Override
    public void removeNode(Node node) {
        if (node.getClass() == CameraNode.class) {
            CameraNode cameraNode = (CameraNode)node;

            // TODO: Possible bug, where cameras are removed out of order
            game.getGameRenderer().removeCamera(cameraNode.camera);

            nodes.queueToRemove(cameraNode);
        }
    }

    @Override
    public void step(double ct, double dt) {
        for (int i = nodes.size() - 1; i >= 0; i--) {
            CameraNode cameraNode = nodes.get(i);

            cameraNode.camera.setView((float) cameraNode.coords.pos.x, (float) cameraNode.coords.pos.y, (float) cameraNode.scale.v);
        }
    }

    @Override
    public void flushQueues() {
        nodes.flushQueues();
    }
}
