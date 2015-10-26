package noteworthyengine;

import android.view.MotionEvent;

import java.util.List;

import noteworthyframework.*;
import structure.Game;
import utils.Vector2;

/**
 * Created by eric on 10/15/15.
 */
public class SelectionSystem extends noteworthyframework.System {

    public QueueMutationList<SelectionNode> selectionNodes = new QueueMutationList<>(127);

    private Game game;
    private GridSystem gridSystem;
    private Vector2 temp = new Vector2();

    // Single player selection...
    private boolean hasSelection = false;

    public SelectionSystem(Game game, GridSystem gridSystem) {
        this.game = game;
        this.gridSystem = gridSystem;
    }

    @Override
    public void addNode(Node node) {
        if (node instanceof SelectionNode) {
            selectionNodes.queueToAdd((SelectionNode) node);
        }
    }

    @Override
    public void removeNode(Node node) {
        if (node instanceof SelectionNode) {
            selectionNodes.queueToRemove((SelectionNode) node);
        }
    }

    @Override
    public void step(double ct, double dt) {
        MotionEvent mocap = MotionEvent.obtain(game.gameInput.motionEvent);

        if (mocap.getActionMasked() == MotionEvent.ACTION_DOWN) {
            game.gameInput.getCoordsCenteredAndNormalized(temp, mocap.getX(), mocap.getY());
            NoteworthyEngine noteworthyEngine = (NoteworthyEngine) this.getBaseEngine();
            double scale = noteworthyEngine.activeGameCamera.cameraNode.scale.v;
            temp.scale(1 / scale);
            temp.translate(noteworthyEngine.activeGameCamera.cameraNode.coords.pos.x, noteworthyEngine.activeGameCamera.cameraNode.coords.pos.y);

            int x = gridSystem.grid.getBucketX(temp.x);
            int y = gridSystem.grid.getBucketY(temp.y);
            List<GridNode> gridNodes = gridSystem.grid.getSurroundingNodes(x, y, 4);

            hasSelection = false;

            for (int i = 0; i < selectionNodes.size(); i++) {
                SelectionNode selectionNode = selectionNodes.get(i);
                selectionNode.isSelected.v = 0;
//
//                if (selectionNode.coords.pos.distanceTo(temp) < 4) {
//                    selectionNode.isSelected.v = 1;
//                }
            }

            for (int i = 0; i < gridNodes.size(); i++) {
                GridNode gridNode = gridNodes.get(i);

                if (gridNode.coords.pos.distanceTo(temp) < 4) {

                    SelectionNode selectionNode = (SelectionNode) gridNode.unit.node("selectionNode");
                    if (selectionNode != null) {
                        selectionNode.isSelected.v = 1;
                        hasSelection = true;
                    }
                }
            }
        }

        mocap.recycle();
    }

    @Override
    public void flushQueues() {
        selectionNodes.flushQueues();
    }
}
