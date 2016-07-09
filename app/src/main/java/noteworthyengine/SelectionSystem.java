package noteworthyengine;

import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import noteworthyengine.players.PlayerSystem;
import noteworthyengine.players.PlayerUnit;
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
    private PlayerSystem playerSystem;
    private QuadTreeSystem quadTreeSystem;
    private Vector2 temp = new Vector2();

    private enum State {
        AWAITING_TAP,
        AWAITING_TAP_UP,
        HAS_SELECTION_AWAITING_TAP_FOR_COMMAND,
        HAS_SELECTION_AWAITING_TAP_FOR_COMMAND_AWAITING_UP,
        AWAITING_TARGET_FOR_COMMAND // For abilities that require a location (like firing a missile at location)
    }

    private State inputState = State.AWAITING_TAP;

    /// Pre-allocated temporary list
    private List<SelectionNode> tempSelectionNodes = new ArrayList<>(MAX_SELECTION_SIZE);

    /// Selections that are cached until a new selection is made
    private List<SelectionNode> cacheSelectionNodes = new ArrayList<>(MAX_SELECTION_SIZE);

    public SelectionSystem(Game game, QuadTreeSystem quadTreeSystem, PlayerSystem playerSystem) {
        this.game = game;
        this.quadTreeSystem = quadTreeSystem;
        this.playerSystem = playerSystem;
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

//        int x = gridSystem.grid.getBucketX(temp.x);
//        int y = gridSystem.grid.getBucketY(temp.y);
//        List<GridNode> gridNodes = gridSystem.grid.getSurroundingNodes(x, y, range);


        ArrayList<QuadTreeSystem.QuadTreeNode> quadTreeNodes = quadTreeSystem.qTree.queryRange(temp.x, temp.y, range);
        found = false;

        for (int i = 0; i < quadTreeNodes.size(); i++) {
            QuadTreeSystem.QuadTreeNode quadTreeNode = quadTreeNodes.get(i);

            if (quadTreeNode.coords.pos.distanceTo(temp) < TOUCH_RADIUS) {

                SelectionNode selectionNode = (SelectionNode)quadTreeNode.unit.node("selectionNode");
                if (selectionNode != null && selectionNode.playerUnitPtr.v == playerSystem.getCurrentPlayer()) {
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

    private void moveSelectedUnits(MotionEvent mocap) {
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
    }

    @Override
    public void step(double ct, double dt) {
        MotionEvent mocap = MotionEvent.obtain(game.gameInput.motionEvent);

        if (inputState == State.AWAITING_TAP &&
                mocap.getActionMasked() == MotionEvent.ACTION_DOWN) {

            inputState = State.AWAITING_TAP_UP;

        }
        else if (inputState == State.AWAITING_TAP_UP &&
                mocap.getActionMasked() == MotionEvent.ACTION_UP) {

            // To be processed further only if there was a selection
            inputState = State.AWAITING_TAP;

            if (makeSelection(mocap)) {
                inputState = State.HAS_SELECTION_AWAITING_TAP_FOR_COMMAND;
            }

        } else if (inputState == State.HAS_SELECTION_AWAITING_TAP_FOR_COMMAND &&
                mocap.getActionMasked() == MotionEvent.ACTION_DOWN) {


            List<SelectionNode> testSelectionNodes = findNodesNearTap(mocap, TOUCH_RADIUS);
            if (testSelectionNodes.size() == 0) {
                // Disabled for now
                //inputState = State.HAS_SELECTION_AWAITING_TAP_FOR_COMMAND_AWAITING_UP;
            }
            else {
                makeSelection(mocap);
            }

        } else if (mocap.getActionMasked() == MotionEvent.ACTION_UP &&
            inputState == State.HAS_SELECTION_AWAITING_TAP_FOR_COMMAND_AWAITING_UP) {

            inputState = State.HAS_SELECTION_AWAITING_TAP_FOR_COMMAND;
            moveSelectedUnits(mocap);

        }

        mocap.recycle();
    }


    @Override
    public void flushQueues() {
        selectionNodes.flushQueues();
    }
}
