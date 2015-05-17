package noteworthyengine.units;

import android.graphics.Color;

import art.Animations;
import noteworthyengine.CameraNode;
import noteworthyengine.InputNode;
import noteworthyengine.InputSystem;
import noteworthyengine.RenderNode;
import noteworthyengine.events.GameEvents;
import noteworthyframework.Unit;
import utils.Vector2;

/**
 * Created by eric on 5/15/15.
 */
public class WinUnit extends Unit {

    public static final String NAME = "winUnit";

    public InputNode inputNode = new SwitcherInput(this);

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

        renderNode.set(0, 0, 0, 2, 2, 0, Color.WHITE, Animations.ANIMATIONS_WIN_DEFEAT_WIN, 0, 1);
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
}
