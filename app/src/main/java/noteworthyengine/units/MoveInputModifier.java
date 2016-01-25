package noteworthyengine.units;

import android.graphics.Color;
import android.util.Log;
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
import structure.GameInput;
import utils.Orientation;
import utils.Vector2;
import utils.VoidFunc;

/**
 * Created by eric on 10/5/15.
 */
public class MoveInputModifier extends Unit {
    public static final String NAME = "moveInputModifier";

    public RenderNode renderNode = new RenderNode(this);
    public InputNode inputNode = new OneTouchHandler(this);

    final private Game game;
    private BaseEngine baseEngine;

    private Vector2 temp = new Vector2();
    private Vector2 temp2 = new Vector2();

    private float circularAnimation = 0;
    private boolean buttonIsActivated = false;

    private Vector2 arrowFeedbackPosition = new Vector2();
    private Orientation arrowFeedbackOrientation = new Orientation();
    private boolean isMakingNewArrowCommand = false;

    public MoveInputModifier(Game game, BaseEngine baseEngine) {
        this.name = NAME;
        this.game = game;
        this.baseEngine = baseEngine;

        inputNode.cameraIndex.v = 1;

        renderNode.isGfxInterpolated.v = 0;
        renderNode.coords.pos.x = -1.2f;
        renderNode.coords.pos.y = -0.6f;
        renderNode.coords.rot.setDegrees(0);
        renderNode.width.v = 0.42f;
        renderNode.height.v = 0.42f;
        renderNode.color.v = Color.argb(255, 255, 255, 255);
        renderNode.renderLayer.v = RenderNode.RENDER_LAYER_GUI;
        renderNode.animationName.v = Animations.ANIMATION_BUTTONS_MOVE;
        renderNode.animationProgress.v = 0;

        renderNode.onDraw = new VoidFunc<RenderSystem>() {
            @Override
            public void apply(RenderSystem system) {
                if (buttonIsActivated) {

                    system.defineNewSprite(Animations.ANIMATION_BUTTONS_ACTIVATED, 0,
                            (float) renderNode.coords.pos.x, (float) renderNode.coords.pos.y, 0,
                            (float) 1.07 * renderNode.width.v, (float) 1.07 * renderNode.height.v,
                            circularAnimation * 1.25f, 0xFE00FFE5, RenderNode.RENDER_LAYER_GUI);

                    system.defineNewSprite(Animations.ANIMATION_BUTTONS_ACTIVATED, 0,
                            (float) renderNode.coords.pos.x, (float) renderNode.coords.pos.y, 0,
                            (float) 1.19 * renderNode.width.v, (float) 1.19 * renderNode.height.v,
                            circularAnimation, 0xFE00A692, RenderNode.RENDER_LAYER_GUI);

                    circularAnimation = circularAnimation + 0.3f;
                }

                // Draw feedback
                if (isMakingNewArrowCommand) {
                    system.defineNewSprite(Animations.ANIMATION_TRIGGER_FIELDS_EXISTING, 0,
                            (float) arrowFeedbackPosition.x, (float) arrowFeedbackPosition.y, 0,
                            7f, 7f,
                            (float)arrowFeedbackOrientation.getDegrees(),
                            Color.argb(240, 255, 255, 255),
                            RenderNode.RENDER_LAYER_FOREGROUND);
                }
            }
        };
    }

    public class TwoTouchHandler extends InputNode {
        int buttonPointerID = -1;
        int secondPointerID = -1;

        public TwoTouchHandler(Unit unit) {
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

        public final int PRISTINE = 0;
        public final int MODIFIER_PRESSED = 1;
        public final int SECOND_POINTER_PRESSED = 2;
        public int state = PRISTINE;

        /**
         * Search all touches to see if any is on the button
         * @param gameInput
         * @param mocap
         * @param cameraNode
         * @param renderNode
         * @return
         */
        public boolean trackIfModifierHeld(GameInput gameInput, MotionEvent mocap, CameraNode cameraNode, RenderNode renderNode) {
            MoveInputModifier.this.buttonIsActivated = false;
            int pointerCount = mocap.getPointerCount();

            if (!gameInput.isTouchDown()) {
                return false;
            }

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
                    return true;
                }
            }
            return false;
        }

        /**
         *  Use the pointer that is not on the button
         * There may be several to track...
         * TODO: Deal with multiple pointers
         * @param id
         * @return
         */
        public boolean setSecondPointerIfNotAlreadyTracked(MotionEvent mocap, int id) {
            if (secondPointerID == -1 || mocap.findPointerIndex(secondPointerID) == -1) {
                secondPointerID = id;
                return true;
            }
            return false;
        }

        /**
         * Search for second pointer
         * @return Returns -1 if none found
         */
        public int findPointerIdNotEqualTo(MotionEvent mocap, int id) {
            for (int i = 0; i < mocap.getPointerCount(); i++) {
                int pointerIdToTest = mocap.getPointerId(i);
                if (pointerIdToTest != id) {
                    return pointerIdToTest;
                }
            }

            return -1;
        }

        public void update(InputSystem inputSystem, CameraNode cameraNode, int currentGesture, int currentAction) {
            renderNode.width.v = 0.42f;
            renderNode.height.v = 0.42f;
            renderNode.color.v = 0xff999999;

            // MotionEvent may asynchronously change during this function call, so cache this
            // This will be recycled at the end of the method
            MotionEvent mocap = MotionEvent.obtain(game.gameInput.motionEvent);

            // TODO: Downcasting may be expensive...So probably want to guard this with a condition
            NoteworthyEngine noteworthyEngine = (NoteworthyEngine)inputSystem.getBaseEngine();
            FieldCameraNode fieldCameraNode = noteworthyEngine.activeGameCamera.fieldCameraNode;

            switch(state) {
                case PRISTINE:
                    //Log.v("MoveInputModifier", "PRISINE");
                    if (trackIfModifierHeld(game.gameInput, mocap, cameraNode, renderNode)) {
                        //Log.v("MoveInputModifier", "MODIFIER_HELD");
                        state = MODIFIER_PRESSED;
                    }
                    break;
                case MODIFIER_PRESSED:
                    //Log.v("MoveInputModifier", "MODIFIER_PRESSED");
                    if (!trackIfModifierHeld(game.gameInput, mocap, cameraNode, renderNode)) {
                        state = PRISTINE;
                    }

                    if (mocap.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN) {

                        int testSecondPointer = findPointerIdNotEqualTo(mocap, buttonPointerID);

                        // If second pointer is found, set it
                        if (setSecondPointerIfNotAlreadyTracked(mocap, testSecondPointer)) {
                        }

                        if (testSecondPointer != -1) {
                            //Log.v("MoveInputModifier", "SECOND_POINTER_FOUND");
                            int pointerIndex = mocap.findPointerIndex(secondPointerID);
                            game.gameInput.getCoordsCenteredAndNormalized(arrowFeedbackPosition, mocap.getX(pointerIndex), mocap.getY(pointerIndex));

                            arrowFeedbackPosition.scale(1 / fieldCameraNode.scale.v, 1 / fieldCameraNode.scale.v);
                            arrowFeedbackPosition.translate(fieldCameraNode.coords.pos.x, fieldCameraNode.coords.pos.y);
                            state = SECOND_POINTER_PRESSED;
                        }
                    }
                    break;
                case SECOND_POINTER_PRESSED:
                    //Log.v("MoveInputModifier", "SECOND_POINTER_PRESSED");
                    if (!trackIfModifierHeld(game.gameInput, mocap, cameraNode, renderNode)) {
                        state = PRISTINE;
                    }
                    else {
                        int pointerIndex = mocap.findPointerIndex(secondPointerID);

                        if (pointerIndex != -1) {

                            // Track move and up
                            //Log.v("MoveInputModifier", "SECOND_POINTER_PRESSED_USING_CACHED_SECOND_POINTER_ID");
                            isMakingNewArrowCommand = true;

                            int mocapAction = mocap.getActionMasked();

                            //Log.v("MoveInputModifier", "SECOND_POINTER_PRESSED_MOVE");

                            game.gameInput.getCoordsCenteredAndNormalized(temp2, mocap.getX(pointerIndex), mocap.getY(pointerIndex));
                            temp2.scale(1 / fieldCameraNode.scale.v, 1 / fieldCameraNode.scale.v);
                            temp2.translate(fieldCameraNode.coords.pos.x, fieldCameraNode.coords.pos.y);

                            Vector2.subtract(arrowFeedbackOrientation, temp2, arrowFeedbackPosition);
                            arrowFeedbackOrientation.setNormalized();
                            arrowFeedbackOrientation.set();

                            if (mocapAction == MotionEvent.ACTION_UP ||
                                    mocapAction == MotionEvent.ACTION_POINTER_UP) {
                                //Log.v("MoveInputModifier", "SECOND_POINTER_PRESSED_UP");

                                // Actually not needed, SECOND_POINTER_PRESSED is effectively guarded
                                // by the POINTER_DOWN condition
                                // secondPointerID = -1;

                                ArrowCommand arrowCommand = new ArrowCommand();
                                //arrowCommand.set(game.noteworthyEngine.currentGamer,
                                arrowCommand.set(baseEngine.currentGamer,
                                        arrowFeedbackPosition.x,
                                        arrowFeedbackPosition.y,
                                        arrowFeedbackOrientation.x,
                                        arrowFeedbackOrientation.y);

                                baseEngine.addUnit(arrowCommand);
                                isMakingNewArrowCommand = false;
                                state = PRISTINE;
                            }
                        } else {
                            // When pointerIndex == -1
                            // Second pointer was somehow lost:
                            // perhaps because an ACTION_UP or ACTION_CANCEL was issued
                            isMakingNewArrowCommand = false;
                            state = PRISTINE;
                        }
                    }
                    break;
            }

            // This must be reached
            mocap.recycle();
        }
    }

    public class OneTouchHandler extends InputNode {

        public OneTouchHandler(Unit unit) {
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

        public final int PRISTINE = 0;
        public final int POINTER_PRESSED = 2;
        public int state = PRISTINE;

        public void update(InputSystem inputSystem, CameraNode cameraNode, int currentGesture, int currentAction) {
            renderNode.width.v = 0.42f;
            renderNode.height.v = 0.42f;
            renderNode.color.v = 0xff999999;

            // MotionEvent may asynchronously change during this function call, so cache this
            // This will be recycled at the end of the method
            MotionEvent mocap = MotionEvent.obtain(game.gameInput.motionEvent);

            // TODO: Downcasting may be expensive...So probably want to guard this with a condition
            NoteworthyEngine noteworthyEngine = (NoteworthyEngine)inputSystem.getBaseEngine();
            FieldCameraNode fieldCameraNode = noteworthyEngine.activeGameCamera.fieldCameraNode;

            switch(state) {
                case PRISTINE:
                    if (mocap.getActionMasked() == MotionEvent.ACTION_DOWN) {
                        //Log.v("MoveInputModifier", "SECOND_POINTER_FOUND");
                        game.gameInput.getCoordsCenteredAndNormalized(arrowFeedbackPosition, mocap.getX(0), mocap.getY(0));

                        arrowFeedbackPosition.scale(1 / fieldCameraNode.scale.v, 1 / fieldCameraNode.scale.v);
                        arrowFeedbackPosition.translate(fieldCameraNode.coords.pos.x, fieldCameraNode.coords.pos.y);
                        state = POINTER_PRESSED;

                    }
                    break;
                case POINTER_PRESSED:
                    // Track move and up
                    //Log.v("MoveInputModifier", "SECOND_POINTER_PRESSED_USING_CACHED_SECOND_POINTER_ID");
                    isMakingNewArrowCommand = true;

                    int mocapAction = mocap.getActionMasked();

                    //Log.v("MoveInputModifier", "SECOND_POINTER_PRESSED_MOVE");

                    game.gameInput.getCoordsCenteredAndNormalized(temp2, mocap.getX(0), mocap.getY(0));
                    temp2.scale(1 / fieldCameraNode.scale.v, 1 / fieldCameraNode.scale.v);
                    temp2.translate(fieldCameraNode.coords.pos.x, fieldCameraNode.coords.pos.y);

                    Vector2.subtract(arrowFeedbackOrientation, temp2, arrowFeedbackPosition);
                    arrowFeedbackOrientation.setNormalized();
                    arrowFeedbackOrientation.set();

                    if (mocapAction == MotionEvent.ACTION_UP ||
                            mocapAction == MotionEvent.ACTION_POINTER_UP) {
                        //Log.v("MoveInputModifier", "SECOND_POINTER_PRESSED_UP");

                        // Actually not needed, SECOND_POINTER_PRESSED is effectively guarded
                        // by the POINTER_DOWN condition
                        // secondPointerID = -1;

                        ArrowCommand arrowCommand = new ArrowCommand();
                        //arrowCommand.set(game.noteworthyEngine.currentGamer,
                        arrowCommand.set(baseEngine.currentGamer,
                                arrowFeedbackPosition.x,
                                arrowFeedbackPosition.y,
                                arrowFeedbackOrientation.x,
                                arrowFeedbackOrientation.y);

                        baseEngine.addUnit(arrowCommand);
                        isMakingNewArrowCommand = false;
                        state = PRISTINE;
                    }
                break;
            }
            mocap.recycle();
        }
    }
}
