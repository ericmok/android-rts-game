package noteworthyengine.units;

import android.graphics.Color;

import art.Animations;
import noteworthyengine.DecayNode;
import noteworthyengine.RenderNode;
import noteworthyengine.RenderSystem;
import noteworthyframework.Unit;
import utils.VoidFunc;

/**
 * Created by eric on 10/31/15.
 */
public class SelectionTap extends Unit {

    public DecayNode decayNode = new DecayNode(this);
    public RenderNode renderNode = new RenderNode(this);

    public SelectionTap() {
        super();

        renderNode.onDraw = new VoidFunc<RenderSystem>() {
            @Override
            public void apply(RenderSystem renderSystem) {
                renderNode.animationProgress.v = (renderNode.animationProgress.v + 1) % 100;
                renderNode.coords.rot.setDegrees(renderNode.coords.rot.getDegrees() + 10);
                renderNode.color.v = Color.argb(Color.alpha(renderNode.color.v) - 20, 255, 255, 255);
            }
        };
    }

    public void configure() {
        decayNode.timeToLive.v = 0.7;

        renderNode.animationName.v = Animations.ANIMATION_TROOPS_TARGETED;
        renderNode.animationProgress.v = 1;
        renderNode.width.v = 4;
        renderNode.height.v = 4;
        renderNode.color.v = Color.argb(255, 255, 255, 255);
    }
}
