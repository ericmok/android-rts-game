package noteworthyengine.units;

import android.graphics.Color;
import android.view.MotionEvent;

import java.util.ArrayList;

import art.Animations;
import noteworthyengine.CommandSystem;
import noteworthyengine.InputNode;
import noteworthyengine.NoteworthyEngine;
import noteworthyengine.RenderNode;
import noteworthyframework.Coords;
import noteworthyframework.Unit;
import structure.Game;
import structure.GameCamera;
import structure.GameInput;
import utils.FloatPtr;
import utils.IntegerPtr;
import utils.Vector2;

/**
 * Created by eric on 4/10/15.
 */
public class ArrowCommandInput extends Unit {
    public static final String NAME = "arrowCommandInput";

    public ArrowCommandInputNode inputNode = new ArrowCommandInputNode(this);
    public RenderNode renderNode = new RenderNode(this);

    final private Game game;

    private CommandSystem.Command feedback = new CommandSystem.Command();

    private Vector2 temp = new Vector2();

    public ArrowCommandInput(Game game) {
        this.name = NAME;
        this.game = game;

        renderNode.isGfxInterpolated.v = 0;
        renderNode.width.v = 6;
        renderNode.height.v = 6;
        renderNode.color.v = Color.argb(200, 255, 255, 255);
        renderNode.animationName.v = Animations.ANIMATION_TRIGGER_FIELDS_EXISTING;
    }

    public class ArrowCommandInputNode extends InputNode {

        public ArrowCommandInputNode(Unit unit) {
            super(unit);
        }

        public void onDown(Vector2 touchPosition) {}
        public void onShowPress(Vector2 touchPosition) {}
        public void onSingleTapUp(Vector2 touchPosition) {}

        public void onScroll(Vector2 touchPosition, Vector2 touchPosition2, Vector2 touchScrollDeltas) {

        }

        public void onLongPress(Vector2 touchPosition) {}
        public void onFling(Vector2 touchPosition, Vector2 touchPosition2) {}
        public void onScale(float touchScale) {}

        public void update(int currentGesture, int currentAction) {

            GameCamera gameCamera = game.getGameRenderer().mainCamera;

            if (currentGesture == GameInput.GESTURE_ON_SCROLL) {
                renderNode.coords.pos.copy(game.gameInput.touchPosition);
                renderNode.coords.pos.scale(1 /gameCamera.scale , 1 / gameCamera.scale);

                Vector2.subtract(temp, game.gameInput.touchPosition2, game.gameInput.touchPosition);
                renderNode.coords.rot.setDirection(temp.x, temp.y);
            }
            if (currentAction == MotionEvent.ACTION_DOWN) {
                renderNode.coords.pos.copy(game.gameInput.touchPosition);
                renderNode.coords.pos.scale(1 / gameCamera.scale, 1 / gameCamera.scale);

                //FloatPtr width = (FloatPtr)feedback.field("width");
                renderNode.width.v = 8;
                //FloatPtr height = (FloatPtr)feedback.field("height");
                renderNode.height.v = 8;

                //IntegerPtr color = (IntegerPtr)feedback.field("color");
                renderNode.color.v = Color.argb(255, 255, 255, 255);
            }
            if (currentAction == MotionEvent.ACTION_UP) {
                ArrowCommand arrowCommand = new ArrowCommand();
                arrowCommand.set(game.noteworthyEngine.currentGamer,
                        renderNode.coords.pos.x,
                        renderNode.coords.pos.y,
                        renderNode.coords.rot.x,
                        renderNode.coords.rot.y);

                game.noteworthyEngine.addUnit(arrowCommand);

                //IntegerPtr color = (IntegerPtr)feedback.field("color");
                renderNode.color.v = Color.argb(0, 255, 255, 255);
            }

        }
    }
}
