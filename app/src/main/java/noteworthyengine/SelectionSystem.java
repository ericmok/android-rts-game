package noteworthyengine;

import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
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

    public static final int MAX_SELECTION_SIZE = 31;
    public QueueMutationList<SelectionNode> selectionNodes = new QueueMutationList<>(127);

    private Game game;
    private GridSystem gridSystem;
    private Vector2 temp = new Vector2();

    // Single player selection...
    private boolean hasSelection = false;

    //private boolean awaitActionUpForTap = false;

    private enum State {
        AWAITING_TAP,
        AWAITING_TAP_UP,
        HAS_SELECTION_AWAITING_TAP_FOR_COMMAND,
        HAS_SELECTION_AWAITING_TAP_FOR_COMMAND_AWAITING_UP,
        AWAITING_TARGET_FOR_COMMAND // For abilities that require a location (like firing a missile at location)
    }

    private List<SelectionNode> tempSelectionNodes = new ArrayList<>(MAX_SELECTION_SIZE);
    private List<SelectionNode> cacheSelectionNodes = new ArrayList<>(MAX_SELECTION_SIZE);
    private State inputState = State.AWAITING_TAP;

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

    /**
     * Returns list of nodes near range.
     * Uses the class's internal temp list to populate and return
     * @param motionEvent
     * @return
     */
    private List<SelectionNode> findNodesNearTap(MotionEvent motionEvent, double range) {

        boolean found = false;
        tempSelectionNodes.clear();

        game.gameInput.getCoordsCenteredAndNormalized(temp, motionEvent.getX(), motionEvent.getY());
        NoteworthyEngine noteworthyEngine = (NoteworthyEngine) this.getBaseEngine();
        double scale = noteworthyEngine.activeGameCamera.cameraNode.scale.v;
        temp.scale(1 / scale);
        temp.translate(noteworthyEngine.activeGameCamera.cameraNode.coords.pos.x, noteworthyEngine.activeGameCamera.cameraNode.coords.pos.y);

        int x = gridSystem.grid.getBucketX(temp.x);
        int y = gridSystem.grid.getBucketY(temp.y);
        List<GridNode> gridNodes = gridSystem.grid.getSurroundingNodes(x, y, range);

        found = false;

        for (int i = 0; i < gridNodes.size(); i++) {
            GridNode gridNode = gridNodes.get(i);

            if (gridNode.coords.pos.distanceTo(temp) < TOUCH_RADIUS) {

                SelectionNode selectionNode = (SelectionNode)gridNode.unit.node("selectionNode");
                if (selectionNode != null) {
                    tempSelectionNodes.add(selectionNode);
                }
//                SelectionNode selectionNode = (SelectionNode) gridNode.unit.node("selectionNode");
//                if (selectionNode != null && selectionNode.gamer.v == getBaseEngine().currentGamer) {
//                    selectionNode.isSelected.v = 1;
//                    found = true;
//                    inputState = State.HAS_SELECTION_AWAITING_TAP_FOR_COMMAND;
//                }
            }
        }

        return tempSelectionNodes;
    }

    private boolean makeSelection(MotionEvent mocap) {
        boolean foundSelection = false;

        for (int i = 0; i < selectionNodes.size(); i++) {
            SelectionNode selectionNode = selectionNodes.get(i);
            selectionNode.isSelected.v = 0;
        }

        List<SelectionNode> tappedNodes = findNodesNearTap(mocap, TOUCH_RADIUS);
        cacheSelectionNodes.clear();
        for (int i = 0; i < tappedNodes.size(); i++) {
            tappedNodes.get(i).isSelected.v = 1;
            cacheSelectionNodes.add(tappedNodes.get(i));
        }
        if (tappedNodes.size() > 0) {
            foundSelection = true;
        }

        SelectionTap selectionTap = new SelectionTap();
        selectionTap.configure();
        selectionTap.renderNode.coords.set(temp.x, temp.y);
        selectionTap.renderNode.width.v = TOUCH_RADIUS + 2;
        selectionTap.renderNode.height.v = TOUCH_RADIUS + 2;
        this.getBaseEngine().addUnit(selectionTap);

        return foundSelection;
    }

    @Override
    public void step(double ct, double dt) {
        MotionEvent mocap = MotionEvent.obtain(game.gameInput.motionEvent);

        if (inputState == State.AWAITING_TAP &&
                mocap.getActionMasked() == MotionEvent.ACTION_DOWN) {
            inputState = State.AWAITING_TAP_UP;
            mocap.recycle();
            return;
        }

        if (inputState == State.AWAITING_TAP_UP &&
                mocap.getActionMasked() == MotionEvent.ACTION_UP) {

            // To be processed further only if there was a selection
            inputState = State.AWAITING_TAP;

            hasSelection = false;
            if (makeSelection(mocap)) {
                inputState = State.HAS_SELECTION_AWAITING_TAP_FOR_COMMAND;
            }

            mocap.recycle();
            return;
        }

        if (inputState == State.HAS_SELECTION_AWAITING_TAP_FOR_COMMAND && mocap.getActionMasked() == MotionEvent.ACTION_DOWN) {
            //inputState = State.HAS_SELECTION_AWAITING_TAP_FOR_COMMAND_AWAITING_UP;
            List<SelectionNode> testSelectionNodes = findNodesNearTap(mocap, TOUCH_RADIUS);
            if (testSelectionNodes.size() == 0) {
                inputState = State.HAS_SELECTION_AWAITING_TAP_FOR_COMMAND_AWAITING_UP;
            }
            else {
                makeSelection(mocap);
            }
            mocap.recycle();
            return;
        }

        // Now wait for pointer up
        if (mocap.getActionMasked() == MotionEvent.ACTION_UP &&
            inputState == State.HAS_SELECTION_AWAITING_TAP_FOR_COMMAND_AWAITING_UP) {

            for (int i = 0; i < cacheSelectionNodes.size(); i++) {
                SelectionNode selectionNode = cacheSelectionNodes.get(i);

                if (selectionNode.isSelected.v == 1) {
                    //selectionNode.isSelected.v = 0;
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
            inputState = State.HAS_SELECTION_AWAITING_TAP_FOR_COMMAND;

            mocap.recycle();
            return;
        }

        mocap.recycle();
    }

    @Override
    public void flushQueues() {
        selectionNodes.flushQueues();
    }
}
