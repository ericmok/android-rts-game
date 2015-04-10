package noteworthyengine;

import android.util.Log;

import noteworthyframework.*;
import structure.Game;
import utils.FloatPtr;
import utils.StringPtr;
import utils.VoidFunc;

/**
 * Created by eric on 3/23/15.
 */
public class ButtonSystem extends noteworthyframework.System {

    private Game game;

    public QueueMutationList<ButtonNode> nodes = new QueueMutationList<ButtonNode>(127);

    public ButtonSystem(Game game) {
        this.game = game;
    }

    @Override
    public void addNode(Node node) {
        if (node.getClass() == ButtonNode.class) {
            nodes.queueToAdd((ButtonNode)node);
        }
    }

    @Override
    public void removeNode(Node node) {
        if (node.getClass() == ButtonNode.class) {
            nodes.queueToRemove((ButtonNode)node);
        }
    }

    @Override
    public void step(double ct, double dt) {

        for (int i = nodes.size() - 1; i >= 0; i--) {
            ButtonNode buttonNode = nodes.get(i);

            if (game.gameInput.isTouchDown()) {
                if (game.gameInput.touchPosition.distanceTo(buttonNode.coords.pos) <
                        Math.min(buttonNode.width.v, buttonNode.height.v)) {
                    buttonNode.onTap.apply(this);

                    buttonNode.coords.pos.copy(game.gameInput.touchPosition);
                    //buttonNode.coords.pos.scale(0.5, 0.5);
                }
            }
        }
    }

    @Override
    public void flushQueues() {
        nodes.flushQueues();
    }

    public static class ButtonNode extends Node {
        public static final String NAME = "buttonNode";

        public Coords coords;
        public FloatPtr width;
        public FloatPtr height;

        public StringPtr animationName;

        public String text;

        public VoidFunc<ButtonSystem> onTap = ON_TAP_DEFAULT;

        public ButtonNode(Unit unit) {
            super(NAME, unit);
            Node.instantiatePublicFieldsForUnit(unit, ButtonNode.class, this);
        }

        public static final VoidFunc<ButtonSystem> ON_TAP_DEFAULT =
                new VoidFunc<ButtonSystem>() {
                    @Override
                    public void apply(ButtonSystem element) {
                        // do nothing
                        Log.d("Button", "TAP");
                    }
                };
    }
}
