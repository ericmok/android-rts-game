package noteworthyengine.units;

import android.graphics.Color;
import android.view.MotionEvent;

import art.Animations;
import noteworthyengine.CameraNode;
import noteworthyengine.InputNode;
import noteworthyengine.InputSystem;
import noteworthyengine.RenderNode;
import noteworthyengine.RenderSystem;
import noteworthyframework.BaseEngine;
import noteworthyframework.Unit;
import structure.Game;
import utils.Vector2;
import utils.VoidFunc;

/**
 * Created by eric on 10/5/15.
 */
public class MoveInputModifier extends Unit {
    public static final String NAME = "moveInputModifier";

    public RenderNode renderNode = new RenderNode(this);
    public InputHandler inputHandler = new InputHandler(this);

    final private Game game;
    private BaseEngine baseEngine;

    private Vector2 temp = new Vector2();
    private Vector2 temp2 = new Vector2();

    private float circularAnimation = 0;
    private boolean buttonIsActivated = false;

    public MoveInputModifier(Game game, BaseEngine baseEngine) {
        this.name = NAME;
        this.game = game;
        this.baseEngine = baseEngine;

        inputHandler.cameraIndex.v = 1;

        renderNode.isGfxInterpolated.v = 0;
        renderNode.coords.pos.x = -1.2f;
        renderNode.coords.pos.y = -0.6f;
        renderNode.coords.rot.setDegrees(0);
        renderNode.width.v = 0.42f;
        renderNode.height.v = 0.42f;
        renderNode.color.v = Color.argb(255, 255, 255, 255);
        renderNode.cameraType.v = 1;
        renderNode.animationName.v = Animations.ANIMATION_BUTTONS_MOVE;
        renderNode.animationProgress.v = 0;

        renderNode.onDraw = new VoidFunc<RenderSystem>() {
            @Override
            public void apply(RenderSystem system) {
                if (buttonIsActivated) {

                    system.defineNewSprite(Animations.ANIMATION_BUTTONS_ACTIVATED, 0,
                            (float) renderNode.coords.pos.x, (float) renderNode.coords.pos.y, 0,
                            (float) 1.07 * renderNode.width.v, (float) 1.07 * renderNode.height.v,
                            circularAnimation * 1.25f, 0xFE00FFE5, 1);

                    system.defineNewSprite(Animations.ANIMATION_BUTTONS_ACTIVATED, 0,
                            (float) renderNode.coords.pos.x, (float) renderNode.coords.pos.y, 0,
                            (float) 1.19 * renderNode.width.v, (float) 1.19 * renderNode.height.v,
                            circularAnimation, 0xFE00A692, 1);

                    circularAnimation = circularAnimation + 0.3f;
                }
            }
        };
    }

    public class InputHandler extends InputNode {

        public InputHandler(Unit unit) {
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
            renderNode.width.v = 0.42f;
            renderNode.height.v = 0.42f;
            renderNode.color.v = 0xff999999;

            MotionEvent mocap = MotionEvent.obtain(game.gameInput.motionEvent);
            MoveInputModifier.this.buttonIsActivated = false;

            if (game.gameInput.touchDown) {
                int pointerCount = mocap.getPointerCount();
                int buttonPointerID = -1;

                for (int i = 0; i < pointerCount; i++) {
                    float x = mocap.getX(i);
                    float y = mocap.getY(i);

                    game.gameInput.getCoordsTranslatedAndNormalized(temp, x, y);
                    cameraNode.camera.getScreenToWorldCoords(temp, temp);

                    if (temp.distanceTo(renderNode.coords.pos) < renderNode.width.v) {
                        buttonPointerID = mocap.getPointerId(i);

                        renderNode.width.v = 0.45f;
                        renderNode.height.v = 0.45f;
                        renderNode.color.v = Color.WHITE;
                        MoveInputModifier.this.buttonIsActivated = true;
                    }
                }

                // TODO
                if (isActive) {
                    for (int i = 0; i < pointerCount; i++) {
                        if (mocap.getPointerId(i) != buttonPointerID) {
                            // This pointer can be used for arrow commands
                            // Simpler if there was a non-variable amount of active arrow commands
                        }
                    }
                }
            }

            mocap.recycle();
        }
    }
}
