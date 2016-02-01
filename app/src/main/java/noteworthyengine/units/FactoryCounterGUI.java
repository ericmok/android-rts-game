package noteworthyengine.units;

import android.graphics.Color;

import art.Animations;
import art.Constants;
import noteworthyengine.FactoryCounterNode;
import noteworthyengine.RenderNode;
import noteworthyengine.RenderSystem;
import noteworthyframework.Gamer;
import noteworthyframework.Unit;
import structure.TextDrawItem;
import utils.VoidFunc;

/**
 * Created by eric on 10/29/15.
 */
public class FactoryCounterGUI extends Unit {

    public FactoryCounterNode factoryCounterNode = new FactoryCounterNode(this);
    public RenderNode renderNode = new RenderNode(this);

    public FactoryCounterGUI() {
        super();

        renderNode.onDraw = new VoidFunc<RenderSystem>() {
            @Override
            public void apply(RenderSystem renderSystem) {
                TextDrawItem textDrawItem = renderSystem.fetchTextDrawItem();
                textDrawItem.position.set(0.5f, -0.5f, 0);
                textDrawItem.cameraIndex = renderSystem.getCameraIndex(RenderNode.RENDER_LAYER_GUI);
                textDrawItem.textDirection.set(1, 0);
                textDrawItem.height = 0.1f;
                textDrawItem.stringBuilder.setLength(0);
                textDrawItem.stringBuilder.append("NUMBER FACTORIES: ");
                textDrawItem.stringBuilder.append(factoryCounterNode.numberFactories.v);
                textDrawItem.color = Color.GREEN;
            }
        };
    }

    public void configure(Gamer gamer) {
        factoryCounterNode.gamer.v = gamer;

        renderNode.animationName.v = Animations.ANIMATION_RETICLE_TAP;
        renderNode.isGfxInterpolated.v = 0;
        renderNode.width.v = 0.1f;
        renderNode.height.v = 0.1f;
        renderNode.color.v = Constants.colorForTeam(gamer.team);
    }
}
