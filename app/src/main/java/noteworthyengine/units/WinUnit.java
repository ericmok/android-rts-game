package noteworthyengine.units;

import android.graphics.Color;

import art.Animations;
import noteworthyengine.CameraNode;
import noteworthyengine.InputNode;
import noteworthyengine.RenderNode;
import noteworthyframework.Unit;
import utils.Vector2;

/**
 * Created by eric on 5/15/15.
 */
public class WinUnit extends Unit {

    public static final String NAME = "winUnit";

    public RenderNode renderNode = new RenderNode(this);

    public WinUnit() {
        super();
        this.name = NAME;

        renderNode.set(0, 0, 0, 2, 2, 0, Color.WHITE, Animations.ANIMATIONS_WIN_DEFEAT_WIN, 0, 1);
    }
}
