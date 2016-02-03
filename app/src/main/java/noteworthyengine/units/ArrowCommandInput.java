package noteworthyengine.units;

import android.graphics.Color;
import android.view.MotionEvent;

import art.Animations;
import noteworthyengine.CameraNode;
import noteworthyengine.InputNode;
import noteworthyengine.InputSystem;
import noteworthyengine.RenderNode;
import noteworthyengine.players.PlayerNode;
import noteworthyengine.players.PlayerUnit;
import noteworthyframework.BaseEngine;
import noteworthyframework.Unit;
import structure.Game;
import structure.GameInput;
import utils.Vector2;

/**
 * Created by eric on 4/10/15.
 */
public class ArrowCommandInput extends Unit {
    public static final String NAME = "arrowCommandInput";

    public PlayerUnit playerUnit;

    public PlayerNode playerNode = new PlayerNode(this);
    public ArrowCommandInputNode inputNode = new ArrowCommandInputNode(this);
    public RenderNode renderNode = new RenderNode(this);

    final private Game game;
    private BaseEngine baseEngine;

    private Vector2 temp = new Vector2();

    public ArrowCommandInput(Game game, BaseEngine baseEngine, PlayerUnit playerUnit) {
        this.name = NAME;
        this.game = game;
        this.baseEngine = baseEngine;
        this.playerUnit = playerUnit;

        renderNode.isGfxInterpolated.v = 0;
        renderNode.width.v = 6;
        renderNode.height.v = 6;
        renderNode.color.v = Color.argb(0, 255, 255, 255);
        renderNode.animationName.v = Animations.ANIMATION_TRIGGER_FIELDS_EXISTING;
    }

    public class ArrowCommandInputNode extends InputNode {

        public ArrowCommandInputNode(Unit unit) {
            super(unit);
        }

        public void onDown(InputSystem inputSystem, CameraNode cameraNode, Vector2 touchPosition) {}
        public void onShowPress(InputSystem inputSystem, CameraNode cameraNode, Vector2 touchPosition) {}
        public void onSingleTapUp(InputSystem inputSystem, CameraNode cameraNode, Vector2 touchPosition) {}

        public void onScroll(InputSystem inputSystem, CameraNode cameraNode, Vector2 touchPosition, Vector2 touchPosition2, Vector2 touchScrollDeltas) {

        }

        public void onLongPress(InputSystem inputSystem, CameraNode cameraNode, Vector2 touchPosition) {}
        public void onFling(InputSystem inputSystem, CameraNode cameraNode, Vector2 touchPosition, Vector2 touchPosition2) {}
        public void onScale(InputSystem inputSystem, CameraNode cameraNode, float touchScale) {}

        public void update(InputSystem inputSystem, CameraNode cameraNode, int currentGesture, int currentAction) {

            //GameCamera gameCamera = game.getGameRenderer().mainCamera;

            if (currentGesture == GameInput.GESTURE_ON_SCROLL) {
                renderNode.coords.pos.copy(game.gameInput.touchPosition);
                renderNode.coords.pos.scale(1 / cameraNode.camera.scale, 1 / cameraNode.camera.scale);
                renderNode.coords.pos.translate(cameraNode.coords.pos.x, cameraNode.coords.pos.y);

                Vector2.subtract(temp, game.gameInput.touchPosition2, game.gameInput.touchPosition);
                renderNode.coords.rot.setDirection(temp.x, temp.y);
            }
            if (currentAction == MotionEvent.ACTION_DOWN) {
                renderNode.coords.pos.copy(game.gameInput.touchPosition);
                renderNode.coords.pos.scale(1 / cameraNode.camera.scale, 1 / cameraNode.camera.scale);
                renderNode.coords.pos.translate(cameraNode.coords.pos.x, cameraNode.coords.pos.y);

                //FloatPtr width = (FloatPtr)feedback.field("width");
                renderNode.width.v = 8;
                //FloatPtr height = (FloatPtr)feedback.field("height");
                renderNode.height.v = 8;

                //IntegerPtr color = (IntegerPtr)feedback.field("color");
                renderNode.color.v = Color.argb(255, 255, 255, 255);
            }
            if (currentAction == MotionEvent.ACTION_UP) {
                ArrowCommand arrowCommand = new ArrowCommand();
                //arrowCommand.set(game.noteworthyEngine.currentGamer,
                arrowCommand.set(playerUnit,
                        renderNode.coords.pos.x,
                        renderNode.coords.pos.y,
                        renderNode.coords.rot.x,
                        renderNode.coords.rot.y);

                baseEngine.addUnit(arrowCommand);

                //IntegerPtr color = (IntegerPtr)feedback.field("color");
                renderNode.color.v = Color.argb(0, 255, 255, 255);
            }

        }
    }
}
