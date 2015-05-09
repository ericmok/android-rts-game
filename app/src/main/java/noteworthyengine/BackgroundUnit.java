package noteworthyengine;

import android.graphics.Color;

import art.Animations;
import noteworthyframework.Unit;

/**
 * Created by eric on 3/22/15.
 */
public class BackgroundUnit extends Unit {

    public RenderNode renderNode = new RenderNode(this);

    public BackgroundUnit() {
        this.name = this.getClass().getSimpleName();

        renderNode.animationName.v = Animations.ANIMATION_SPLASH_LOADER;
        renderNode.animationProgress.v = 1;
        renderNode.width.v = 1;
        renderNode.height.v = 1;
        renderNode.color.v = Color.WHITE;

        renderNode.coords.pos.zero();
        renderNode.z.v = 0;
        renderNode.coords.rot.setDegrees(0);
        renderNode.isGfxInterpolated.v = 0;
    }
}
