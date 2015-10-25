package noteworthyengine.units;

import art.Animations;
import noteworthyengine.BattleNode;
import noteworthyengine.FieldNode;
import noteworthyengine.GridNode;
import noteworthyengine.MovementNode;
import noteworthyengine.RenderNode;
import noteworthyengine.RenderSystem;
import noteworthyengine.SeparationNode;
import noteworthyframework.Gamer;
import noteworthyframework.Unit;
import structure.TemporarySprite2dDef;
import utils.VoidFunc;

/**
 * Created by eric on 5/1/15.
 */
public class Nanobot extends Unit {

    public final GridNode gridNode;
    public final FieldNode fieldNode;
    public final MovementNode movementNode;
    public final SeparationNode separationNode;

    public final BattleNode battleNode;
    public final RenderNode renderNode;

    public Nanobot() {
        this.name = this.getClass().getSimpleName();

        movementNode = new MovementNode(this);
        separationNode = new SeparationNode(this);
        battleNode = new BattleNode(this);

        renderNode = new RenderNode(this);

        gridNode = new GridNode(this, separationNode, battleNode);

        fieldNode = FieldNode.createAgentFieldNode(this);

        renderNode.onDraw = getOnDrawFunction();

        reset();
    }

    public void reset() {
        movementNode.maxSpeed.v = 1.1;

        battleNode.hp.v = 24;
        battleNode.targetAcquisitionRange.v = 20;
        battleNode.attackCooldown.v = 1;
        battleNode.attackDamage.v = 2;
        battleNode.attackCooldown.v = 1;
        battleNode.attackRange.v = 1.0;

        battleNode.attackState.v = BattleNode.ATTACK_STATE_READY;
        battleNode.target.v = null;

        renderNode.animationName.v = "Animations/Nanobots/Idling";
        renderNode.animationProgress.v = 0;
        renderNode.width.v = 0.78f;
        renderNode.height.v = 0.78f;
        renderNode.isGfxInterpolated.v = 1;
        renderNode.renderLayer.v = 0;
    }

    public void configure(Gamer gamer) {
        battleNode.gamer.v = gamer;
        renderNode.color.v = Gamer.TeamColors.get(gamer.team);
    }

    public VoidFunc<RenderSystem> getOnDrawFunction() {
        return new VoidFunc<RenderSystem>() {
            @Override
            public void apply(RenderSystem renderSystem) {

                if (battleNode.attackState.v == BattleNode.ATTACK_STATE_SWINGING && battleNode.target.v != null) {

                    // Attack animation here...

                }

                if (!battleNode.isAlive()) {
                    //TemporarySprite2dDef temporarySprite2dDef = renderSystem.defineNewTempSprite(Animations.ANIMATION_TROOPS_DYING_DEF, 0);
                    TemporarySprite2dDef temporarySprite2dDef = renderSystem.beginNewTempSprite();

                    //renderSystem.drawCompat.tempSpritesMemoryPool.fetchMemory();
                    temporarySprite2dDef.copy(Animations.ANIMATION_TROOPS_DYING_DEF);
                    temporarySprite2dDef.position.x = battleNode.coords.pos.x;
                    temporarySprite2dDef.position.y = battleNode.coords.pos.y;
                    temporarySprite2dDef.width = 0.78f;
                    temporarySprite2dDef.height = 0.78f;

                    renderSystem.endNewTempSprite(temporarySprite2dDef, 0);

                    //renderSystem.drawCompat.drawTemporarySprite(temporarySprite2dDef);
                    //renderSystem.drawCompat.tempSpritesMemoryPool.recycleMemory(temporarySprite2dDef);
                }
            }
        };
    }
}
