package noteworthyengine;

import android.graphics.Color;

import noteworthyframework.Gamer;
import noteworthyframework.Unit;
import structure.Sprite2dDef;
import structure.TemporarySprite2dDef;
import utils.VoidFunc;
import utils.VoidFunc2;
import utils.VoidFunc3;

/**
 * Created by eric on 3/7/15.
 */
public class Platoon extends Unit {
    public static final String NAME = "Troopy";

    MovementNode movementNode;

    FieldNode fieldNode;
    BattleNode battleNode;
    SeparationNode separationNode;
    FormationNode formationNode;

    RenderNode renderNode;
    RenderNode dyingRenderNode;
    RenderNode selectedRenderNode;

    public Platoon() {
        this.name = NAME;

        movementNode = new MovementNode(this);
        movementNode.maxSpeed.v = 0.8;

        fieldNode = new FieldNode(this);
        fieldNode._fieldAgentNode = new FieldNode.FieldAgentNode(this);
        //fieldNode._movementNode = movementNode;

        battleNode = new BattleNode(this);
        battleNode.hp.v = 100;
        battleNode.targetAcquisitionRange.v = 15;
        battleNode.onTargetAcquired = onTargetAcquired;

        renderNode = new RenderNode(this);
        float size = Math.random() > 0.5f ? 1f : 0.7f;
        renderNode.animationName = Sprite2dDef.ANIMATION_TROOPS_IDLING;
        renderNode.animationProgress.v = 0;
        renderNode.width.v = size;
        renderNode.height.v = size;
        renderNode.color.v = Color.WHITE;
        renderNode.isGfxInterpolated.v = 1;
        renderNode.onDraw = this.onDraw;

        dyingRenderNode = new RenderNode("dyingRenderNode", this);
        dyingRenderNode.isActive = false;
        dyingRenderNode.animationName = Sprite2dDef.ANIMATION_TROOPS_DYING;
        dyingRenderNode.color.v = Color.WHITE;

        separationNode = new SeparationNode(this);
        formationNode = new FormationNode(this);

        selectedRenderNode = new RenderNode("selectedRenderNode", this);
        selectedRenderNode.animationName = Sprite2dDef.ANIMATION_TROOPS_TARGETED;
        selectedRenderNode.isActive = false;
    }

    public final VoidFunc3<BattleSystem, BattleNode, BattleNode> onTargetAcquired =
        new VoidFunc3<BattleSystem, BattleNode, BattleNode>() {
            @Override
            public void apply(BattleSystem battleSystem, BattleNode battleNode, BattleNode battleNode2) {
                //renderNode.animationName = Sprite2dDef.ANIMATION_TROOPS_TARGETED;
                //selectedRenderNode.isActive = true;
            }
        };

    public final VoidFunc<RenderSystem> onDraw = new VoidFunc<RenderSystem>() {
        @Override
        public void apply(RenderSystem system) {
            renderNode.color.v = Gamer.TeamColors.get(battleNode.gamer.v.team);

            if (battleNode.hp.v <= 0) {
                system.drawCompat.drawTemporarySprite(new TemporarySprite2dDef() {{
                    this.position.x = battleNode.coords.pos.x;
                    this.position.y = battleNode.coords.pos.y;
                    this.position.z = 1;
                    this.progress.progress = 1;
                    this.progress.duration = 1200;
                    this.isGfxInterpolated = false;
                    this.animationName = Sprite2dDef.ANIMATION_TROOPS_DYING;
                    this.animationProgress = 0;
                    this.color = Color.WHITE;
                    this.angle = 90;
                }});
            }
            //dyingRenderNode.isActive = true;
        }
    };
}
