package noteworthyengine.units;

import art.Animations;
import art.Constants;
import noteworthyengine.battle.BasicAttackEffect;
import noteworthyengine.battle.BattleNode;
import noteworthyengine.FieldNode;
import noteworthyengine.GridNode;
import noteworthyengine.MovementNode;
import noteworthyengine.RenderNode;
import noteworthyengine.RenderSystem;
import noteworthyengine.SeparationNode;
import noteworthyengine.players.PlayerUnit;
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
        battleNode.battleEffects.add(new BasicAttackEffect(battleNode));

        renderNode = new RenderNode(this);

        gridNode = new GridNode(this, separationNode, battleNode);

        fieldNode = FieldNode.createAgentFieldNode(this);

        renderNode.onDraw = getOnDrawFunction();
    }

    public void configure(PlayerUnit playerUnit) {
        movementNode.reset();
        battleNode.reset();

        battleNode.playerUnitPtr.v = playerUnit;
        renderNode.color.v = Constants.colorForTeam(playerUnit.playerNode.playerData.team);

        movementNode.maxSpeed.v = 1.1;

        battleNode.hp.v = 24;
        battleNode.targetAcquisitionRange.v = 20;
        battleNode.battleAttack.cooldownTime = 1;
        battleNode.battleAttack.amount = 10;
        //battleNode.battleAttack.cooldownTime = 1;
        battleNode.battleAttack.range = 1.3;

        battleNode.battleState.v = BattleNode.BATTLE_STATE_IDLE;
        battleNode.target.v = null;

        renderNode.animationName.v = "Animations/Nanobots/Idling";
        renderNode.animationProgress.v = 0;
        renderNode.width.v = 0.78f;
        renderNode.height.v = 0.78f;
        renderNode.isGfxInterpolated.v = 1;
        renderNode.renderLayer.v = RenderNode.RENDER_LAYER_FOREGROUND;
    }

    public VoidFunc<RenderSystem> getOnDrawFunction() {
        return new VoidFunc<RenderSystem>() {
            @Override
            public void apply(RenderSystem renderSystem) {

                if (battleNode.battleState.v == BattleNode.BATTLE_STATE_SWINGING && battleNode.target.v != null) {

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
