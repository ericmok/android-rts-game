package noteworthyengine.units;

import android.graphics.Color;
import android.view.MotionEvent;

import art.Animations;
import noteworthyengine.CameraNode;
import noteworthyengine.FieldCameraNode;
import noteworthyengine.InputNode;
import noteworthyengine.InputSystem;
import noteworthyengine.NoteworthyEngine;
import noteworthyengine.RenderNode;
import noteworthyengine.RenderSystem;
import noteworthyframework.BaseEngine;
import noteworthyframework.Unit;
import structure.Game;
import utils.Orientation;
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
    int buttonPointerID = -1;
    int secondPointerID = -1;

    private Vector2 arrowFeedbackPosition = new Vector2();
    private Orientation arrowFeedbackOrientation = new Orientation();
    private boolean isMakingNewArrowCommand = false;

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
        renderNode.renderLayer.v = 1;
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

                // Draw feedback
                if (isMakingNewArrowCommand) {
                    system.defineNewSprite(Animations.ANIMATION_TRIGGER_FIELDS_EXISTING, 0,
                            (float) arrowFeedbackPosition.x, (float) arrowFeedbackPosition.y, 0,
                            7f, 7f,
                            (float)arrowFeedbackOrientation.getDegrees(),
                            Color.argb(240, 255, 255, 255),
                            0);
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
            MoveInputModifier.this.buttonIsActivated = false;

            // MotionEvent may asynchronously change during this function call, so cache this
            MotionEvent mocap = MotionEvent.obtain(game.gameInput.motionEvent);

            if (game.gameInput.touchDown) {

                int pointerCount = mocap.getPointerCount();

                // Search all touches to see if any is on the button
                for (int i = 0; i < pointerCount; i++) {
                    float x = mocap.getX(i);
                    float y = mocap.getY(i);

                    game.gameInput.getCoordsCenteredAndNormalized(temp, x, y);
                    cameraNode.camera.getScreenToWorldCoords(temp, temp);

                    if (temp.distanceTo(renderNode.coords.pos) < renderNode.width.v) {
                        buttonPointerID = mocap.getPointerId(i);

                        renderNode.width.v = 0.45f;
                        renderNode.height.v = 0.45f;
                        renderNode.color.v = Color.WHITE;
                        MoveInputModifier.this.buttonIsActivated = true;
                    }
                }

                // If button is down, check for scroll gesture
                if (MoveInputModifier.this.buttonIsActivated) {

                    // Search for second pointer
                    for (int i = 0; i < pointerCount; i++) {

                        if (mocap.getPointerId(i) != buttonPointerID) {
                            // Use the pointer that is not on the button
                            // There may be several to track...
                            // TODO: Deal with multiple pointers
                            if (secondPointerID == -1) {
                                secondPointerID = mocap.getPointerId(i);
                            }

                            // Found then handle POINTER_DOWN, MOVE, POINTER_UP
                            NoteworthyEngine noteworthyEngine = (NoteworthyEngine)inputSystem.getBaseEngine();
                            processArrowCommand(noteworthyEngine.activeGameCamera.fieldCameraNode, mocap, i);
                        }
                    }

                    if (secondPointerID != -1) {

                        // If the second pointer disappeared, then interpret this as a POINTER_UP
                        // and make new arrowCommand
                        if (mocap.findPointerIndex(secondPointerID) == -1) {
                            secondPointerID = -1;

                            // MotionEvent state sticks when there are no events (UP, MOVE, DOWN)
                            // so you may wind up with stale continuous POINTER_UP events
                            // event after the 2nd touch as lifted but modifier button is still pressed.
                            // In addition, this function body is in a loop, don't want multiple
                            // ACTION_POINTER_UP's:
                            isMakingNewArrowCommand = false;

                            ArrowCommand arrowCommand = new ArrowCommand();
                            //arrowCommand.set(game.noteworthyEngine.currentGamer,
                            arrowCommand.set(baseEngine.currentGamer,
                                    arrowFeedbackPosition.x,
                                    arrowFeedbackPosition.y,
                                    arrowFeedbackOrientation.x,
                                    arrowFeedbackOrientation.y);

                            baseEngine.addUnit(arrowCommand);
                        }
                    }
                }

                // Prevent consecutive ACTION_POINTER_UP's / ACTION_UP's
                //
                // Also Edge Case: Button finger moves off bounds and lifts before 2nd finger
                // Without this: the isMakingNewArrowCommand sprite gets displayed
                // even when button is not activated
                // processArrowCommand will not be able to capture ACTION_POINTER_UP events
                if (mocap.getPointerCount() < 2 ||
                        mocap.getActionMasked() == MotionEvent.ACTION_POINTER_UP ||
                        mocap.getActionMasked() == MotionEvent.ACTION_UP ||
                        mocap.getActionMasked() == MotionEvent.ACTION_CANCEL) {
                    isMakingNewArrowCommand = false;
                }
            } else {
                // Don't track the button finger anymore
                buttonPointerID = -1;

                // Fall through when no ACTION_POINTER_UP action, and only get ACTION_UP
                isMakingNewArrowCommand = false;
            }

            mocap.recycle();
        }


        public void processArrowCommand(FieldCameraNode camera, MotionEvent mocap, int pointerIndex) {

            int mocapAction = mocap.getActionMasked();

            if (mocapAction == MotionEvent.ACTION_POINTER_DOWN) {
                isMakingNewArrowCommand = true;

                game.gameInput.getCoordsCenteredAndNormalized(arrowFeedbackPosition, mocap.getX(pointerIndex), mocap.getY(pointerIndex));
                arrowFeedbackPosition.scale(1 / camera.scale.v, 1 / camera.scale.v);
                arrowFeedbackPosition.translate(camera.coords.pos.x, camera.coords.pos.y);
            }

            // Only when there is an ACTION_POINTER_DOWN do we consider the further mechanics
            if (isMakingNewArrowCommand) {
                if (mocapAction == MotionEvent.ACTION_MOVE) {
                    game.gameInput.getCoordsCenteredAndNormalized(temp2, mocap.getX(pointerIndex), mocap.getY(pointerIndex));
                    temp2.scale(1 / camera.scale.v, 1 / camera.scale.v);
                    temp2.translate(camera.coords.pos.x, camera.coords.pos.y);

                    Vector2.subtract(arrowFeedbackOrientation, temp2, arrowFeedbackPosition);
                    arrowFeedbackOrientation.setNormalized();
                    arrowFeedbackOrientation.set();
                }
                // ACTION_POINTER_UP's are sometimes swallowed by ACTION_MOVE's
                // So we track the second touch to make a new command
            }
        }
    }
}
