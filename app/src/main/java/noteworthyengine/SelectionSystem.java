package noteworthyengine;

import android.view.MotionEvent;

import java.util.List;

import noteworthyengine.units.SelectionTap;
import noteworthyframework.*;
import structure.Game;
import utils.Vector2;

/**
 * Created by eric on 10/15/15.
 */
public class SelectionSystem extends noteworthyframework.System {

    public static final float TOUCH_RADIUS = 3;

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

    private boolean awaitActionUpForTap = false;

    @Override
    public void step(double ct, double dt) {
        MotionEvent mocap = MotionEvent.obtain(game.gameInput.motionEvent);

        if (mocap.getActionMasked() == MotionEvent.ACTION_DOWN) {
            awaitActionUpForTap = true;
        }

        if (mocap.getActionMasked() == MotionEvent.ACTION_UP && awaitActionUpForTap && !hasSelection) {
            awaitActionUpForTap = false;

                game.gameInput.getCoordsCenteredAndNormalized(temp, mocap.getX(), mocap.getY());
                NoteworthyEngine noteworthyEngine = (NoteworthyEngine) this.getBaseEngine();
                double scale = noteworthyEngine.activeGameCamera.cameraNode.scale.v;
                temp.scale(1 / scale);
                temp.translate(noteworthyEngine.activeGameCamera.cameraNode.coords.pos.x, noteworthyEngine.activeGameCamera.cameraNode.coords.pos.y);

                int x = gridSystem.grid.getBucketX(temp.x);
                int y = gridSystem.grid.getBucketY(temp.y);
                List<GridNode> gridNodes = gridSystem.grid.getSurroundingNodes(x, y, TOUCH_RADIUS);

                hasSelection = false;

                for (int i = 0; i < selectionNodes.size(); i++) {
                    SelectionNode selectionNode = selectionNodes.get(i);
                    selectionNode.isSelected.v = 0;
//
//                if (selectionNode.coords.pos.distanceTo(temp) < TOUCH_RADIUS) {
//                    selectionNode.isSelected.v = 1;
//                }
                }

                for (int i = 0; i < gridNodes.size(); i++) {
                    GridNode gridNode = gridNodes.get(i);

                    if (gridNode.coords.pos.distanceTo(temp) < TOUCH_RADIUS) {

                        SelectionNode selectionNode = (SelectionNode) gridNode.unit.node("selectionNode");
                        if (selectionNode != null && selectionNode.gamer.v == getBaseEngine().currentGamer) {
                            selectionNode.isSelected.v = 1;
                            hasSelection = true;
                        }
                    }
                }

            SelectionTap selectionTap = new SelectionTap();
            selectionTap.configure();
            selectionTap.renderNode.coords.set(temp.x, temp.y);
            selectionTap.renderNode.width.v = TOUCH_RADIUS + 2;
            selectionTap.renderNode.height.v = TOUCH_RADIUS + 2;
            this.getBaseEngine().addUnit(selectionTap);
        }


        // Now wait for pointer up
        if (mocap.getActionMasked() == MotionEvent.ACTION_UP && awaitActionUpForTap && hasSelection) {
            awaitActionUpForTap = false;

            for (int i = 0; i < selectionNodes.size(); i++) {
                SelectionNode selectionNode = selectionNodes.get(i);

                if (selectionNode.isSelected.v == 1) {
                    selectionNode.isSelected.v = 0;
                    MovementNode movementNode = (MovementNode) selectionNode.unit.node(MovementNode._NAME);

                    game.gameInput.getCoordsCenteredAndNormalized(temp, mocap.getX(), mocap.getY());
                    NoteworthyEngine noteworthyEngine = (NoteworthyEngine) this.getBaseEngine();
                    double scale = noteworthyEngine.activeGameCamera.cameraNode.scale.v;
                    temp.scale(1 / scale);
                    temp.translate(noteworthyEngine.activeGameCamera.cameraNode.coords.pos.x, noteworthyEngine.activeGameCamera.cameraNode.coords.pos.y);

                    movementNode.destination.set(temp.x, temp.y);
                    movementNode.hasDestination.v = 1;
                }
            }

            hasSelection = false;
        }

        mocap.recycle();
    }

    @Override
    public void flushQueues() {
        selectionNodes.flushQueues();
    }
}
