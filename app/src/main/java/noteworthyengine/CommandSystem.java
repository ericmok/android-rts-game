package noteworthyengine;

import android.graphics.Color;
import android.view.MotionEvent;

import java.util.ArrayList;

import noteworthyframework.*;
import structure.Game;
import structure.GameInput;
import structure.Sprite2dDef;
import utils.Vector2;

/**
 * Created by eric on 3/9/15.
 */
public class CommandSystem extends noteworthyframework.System {

    private Game game;
    public ArrayList<CommandNode> commandNodes = new ArrayList<CommandNode>(63);
    private Command feedback = new Command();
    private CommandNode commandNode = (CommandNode)feedback.node(CommandNode._NAME);

    Vector2 temp = new Vector2();

    public CommandSystem(Game game) {
        this.game = game;
    }

    @Override
    public void initialize() {
        this.getBaseEngine().addUnit(feedback);
    }

    @Override
    public void addNode(Node node) {
        if (node.getClass() == CommandNode.class) {
            commandNodes.add((CommandNode)node);
        }
    }

    @Override
    public void removeNode(Node node) {
        if (node.getClass() == CommandNode.class) {
            commandNodes.remove((CommandNode) node);
        }
    }

    @Override
    public void step(double ct, double dt) {
        int currentGesture = game.gameInput.takeCurrentGesture();
        int currentAction = game.gameInput.takeCurrentMouseEventAction();

        if (currentGesture == GameInput.GESTURE_ON_SCROLL) {
            commandNode.coords.pos.copy(game.gameInput.touchPosition);
            commandNode.coords.pos.scale(1 / game.gameCamera.scale, 1 / game.gameCamera.scale);

            Vector2.subtract(temp, game.gameInput.touchPosition2, game.gameInput.touchPosition);
            commandNode.coords.rot.setDirection(temp.x, temp.y);
        }
        if (currentAction == MotionEvent.ACTION_DOWN) {
            commandNode.coords.pos.copy(game.gameInput.touchPosition);
            commandNode.coords.pos.scale(1 / game.gameCamera.scale, 1 / game.gameCamera.scale);

            FloatPtr width = (FloatPtr)feedback.field("width");
            width.v = Command.ACTIVE_RADIUS;
            FloatPtr height = (FloatPtr)feedback.field("height");
            height.v = Command.ACTIVE_RADIUS;

            IntegerPtr color = (IntegerPtr)feedback.field("color");
            color.v = Color.argb(255, 255, 255, 255);
        }
        if (currentAction == MotionEvent.ACTION_UP) {
            ArrowCommand arrowCommand = new ArrowCommand();
            Coords coords = (Coords)arrowCommand.field("coords");
            coords.pos.copy(commandNode.coords.pos);
            coords.rot.copy(commandNode.coords.rot);

            this.getBaseEngine().addUnit(arrowCommand);

            IntegerPtr color = (IntegerPtr)feedback.field("color");
            color.v = Color.argb(0, 255, 255, 255);
        }

    }

    @Override
    public void flushQueues() {
    }

    public static class CommandNode extends Node {
        public static final String _NAME = "command";

        public static final String _CREATE_ARROW_FIELD = "create arrow field";

        public String name = _NAME;

        public String commandType;

        public Coords coords;
        public Vector2 lastTouchUp;


        public CommandNode(Unit unit) {
            super(_NAME, unit);
            Node.instantiatePublicFieldsForUnit(unit, CommandNode.class, this);
        }
    }

    public static class Command extends Unit {

        public static final float RADIUS = 6;
        public static final float ACTIVE_RADIUS = 8;

        public Command() {
            CommandNode commandNode = new CommandNode(this);

            RenderNode renderNode = new RenderNode(this);
            renderNode.isGfxInterpolated.v = 0;
            renderNode.width.v = RADIUS;
            renderNode.height.v = RADIUS;
            renderNode.color.v = Color.argb(0, 0, 0, 0);
            renderNode.animationName = Sprite2dDef.ANIMATION_TRIGGER_FIELDS_EXISTING;
        }
    }
}
