package noteworthyengine.units;

import android.graphics.Color;

import art.Animations;
import noteworthyengine.CameraNode;
import noteworthyengine.InputNode;
import noteworthyengine.InputSystem;
import noteworthyengine.RenderNode;
import noteworthyengine.events.GameEvents;
import noteworthyframework.BaseEngine;
import noteworthyframework.Unit;
import utils.Vector2;

/**
 * Created by eric on 5/15/15.
 */
public class WinUnit extends Unit {

    public static final String NAME = "winUnit";

    public InputNode inputNode = new SwitcherInput(this);
    private double animationProgress = 0;

//    public InputNode inputNode = new InputNode(this) {
//        @Override
//        public void onSingleTapUp(InputSystem inputSystem, CameraNode cameraNode, Vector2 touchPosition) {
//            super.onSingleTapUp(inputSystem, cameraNode, touchPosition);
//
//            // TODO: Defeat signal
//            inputSystem.getBaseEngine().emitEvent(GameEvents.QUIT_WIN);
//        }
//    };

    public RenderNode renderNode = new RenderNode(this);

    public WinUnit() {
        super();
        this.name = NAME;

        renderNode.set(0, 0.5f, 0, 1.2f, 1.2f, 0, Color.WHITE, Animations.ANIMATIONS_WIN_DEFEAT_WIN, 0, 1);
    }

    public static class SwitcherInput extends InputNode {
        public SwitcherInput(Unit unit) {
            super(unit);
        }

        @Override
        public void onSingleTapUp(InputSystem inputSystem, CameraNode cameraNode, Vector2 touchPosition) {
            super.onSingleTapUp(inputSystem, cameraNode, touchPosition);

            // TODO: Defeat signal
            inputSystem.getBaseEngine().emitEvent(GameEvents.QUIT_WIN);
        }
    }

    @Override
    public void step(BaseEngine engine, double dt) {
        super.step(engine, dt);
        animationProgress += dt;

        renderNode.coords.pos.x += 0.0005 * Math.sin(0.2 * animationProgress);
    }
}
