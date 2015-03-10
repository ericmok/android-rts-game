package noteworthyengine;

import android.graphics.Color;

import structure.Sprite2dDef;
import structure.TemporarySprite2dDef;
import utils.VoidFunc;

/**
 * Created by eric on 3/7/15.
 */
public class Platoon extends Unit {
    public static final String NAME = "Troopy";

    MovementNode movementNode;
    RenderNode renderNode;
    FieldNode fieldNode;
    BattleNode battleNode;

    RenderNode dyingRenderNode;

    public Platoon() {
        this.name = NAME;

        movementNode = new MovementNode(this);

        renderNode = new RenderNode(this);
        float size = Math.random() > 0.5f ? 1f : 0.7f;
        renderNode.animationName = Sprite2dDef.ANIMATION_TROOPS_IDLING;
        renderNode.animationProgress.v = 0;
        renderNode.width.v = size;
        renderNode.height.v = size;
        renderNode.color.v = Color.WHITE;
        renderNode.isGfxInterpolated.v = 1;
        renderNode.onDraw = this.onDraw;

        fieldNode = new FieldNode(this);
        fieldNode._fieldAgentNode = new FieldNode.FieldAgentNode(this);
        //fieldNode._movementNode = movementNode;

        battleNode = new BattleNode(this);

        dyingRenderNode = new RenderNode("dyingRenderNode", this);
        dyingRenderNode.isActive = false;
        dyingRenderNode.animationName = Sprite2dDef.ANIMATION_TROOPS_DYING;
        dyingRenderNode.color.v = Color.WHITE;
    }

    public final VoidFunc<RenderSystem> onDraw = new VoidFunc<RenderSystem>() {
        @Override
        public void apply(RenderSystem system) {
            renderNode.color.v = Gamer.TeamColors.get(battleNode.gamer.team);

            system.removeNode(renderNode);

            system.drawCompat.drawTemporarySprite(new TemporarySprite2dDef() {{

            }});
            // TODO: Should logic be delegated? Since we want full control over how to render
//            if (battleNode.hp.v < 0) {
//                renderNode.animationName = Sprite2dDef.ANIMATION_TROOPS_DYING;
//                renderNode.animationProgress.v = 80;
//            }
        }
    };
}
