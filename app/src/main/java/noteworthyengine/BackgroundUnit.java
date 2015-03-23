package noteworthyengine;

import android.graphics.Color;

import noteworthyframework.Unit;
import structure.Sprite2dDef;

/**
 * Created by eric on 3/22/15.
 */
public class BackgroundUnit extends Unit {

    public RenderNode renderNode = new RenderNode(this);

    public BackgroundUnit() {
        this.name = this.getClass().getSimpleName();

        renderNode.animationName = Sprite2dDef.ANIMATION_SPLASH_LOADER;
        renderNode.animationProgress.v = 1;
        renderNode.width.v = 1;
        renderNode.height.v = 1;
        renderNode.color.v = Color.WHITE;

        renderNode.coords.pos.zero();
        renderNode.z.v = 1;
        renderNode.coords.rot.setDegrees(90);
        renderNode.isGfxInterpolated.v = 0;
    }
}
