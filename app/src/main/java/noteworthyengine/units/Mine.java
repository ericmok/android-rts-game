package noteworthyengine.units;

import android.graphics.Color;

import art.Animations;
import noteworthyengine.BattleNode;
import noteworthyengine.BattleSystem;
import noteworthyengine.GridNode;
import noteworthyengine.MovementNode;
import noteworthyengine.RenderNode;
import noteworthyengine.RenderSystem;
import noteworthyengine.SeparationNode;
import noteworthyframework.Gamer;
import noteworthyframework.Unit;
import structure.RewriteOnlyArray;
import structure.Sprite2dDef;
import structure.TemporarySprite2dDef;
import utils.VoidFunc;

/**
 * Created by eric on 3/21/15.
 */
public class Mine extends Unit {

    public static final String NAME = "explosion";

    public static final int MAX_BATTLE_NODES_AFFECTED = 20;

    public GridNode gridNode;
    public MovementNode movementNode = new MovementNode(this);
    public SeparationNode separationNode = new SeparationNode(this);
    public BattleNode battleNode = new MineBattleNode(this);
    public RenderNode renderNode = new RenderNode(this);

    public RewriteOnlyArray<BattleNode.Target> battleTargets =
            new RewriteOnlyArray<BattleNode.Target>(BattleNode.Target.class, MAX_BATTLE_NODES_AFFECTED);

    public Mine() {
        this.name = NAME;

        gridNode = new GridNode(this, separationNode, battleNode);

        renderNode.onDraw = new VoidFunc<RenderSystem>() {
            @Override
            public void apply(RenderSystem system) {

                if (!battleNode.isAlive()) {
                    TemporarySprite2dDef tempSprite = system.beginNewTempSprite();

                    tempSprite.copy(Animations.ANIMATION_TROOPS_DYING_DEF);
                    tempSprite.setPosition((float) renderNode.coords.pos.x, (float) renderNode.coords.pos.y, 0);

                    system.endNewTempSprite(tempSprite, 0);
                }

                if (battleNode.attackState.v == BattleNode.ATTACK_STATE_SWINGING) {
                    float ratio = (float)(battleNode.attackProgress.v / battleNode.attackSwingTime.v);
                    float rad = (float)(battleNode.attackRange.v * ratio);

                    Sprite2dDef sprite2dDef = system.defineNewSprite(Animations.ANIMATION_MINE_EXPLODING,
                            (int) (ratio * 100),
                            (float)battleNode.coords.pos.x,
                            (float)battleNode.coords.pos.y,
                            3,
                            2 * rad,
                            2 * rad,
                            0,
                            renderNode.color.v,
                            RenderNode.RENDER_LAYER_FOREGROUND);
                }
//                else {
//                    renderNode.animationName = Sprite2dDef.ANIMATION_MINE_IDLING;
//                    renderNode.animationProgress.v = 0;
//                    renderNode.width.v = 0.9f;
//                    renderNode.height.v = 0.9f;
//                }
            }
        };
    }

    public static class MineBattleNode extends BattleNode {
        private Mine mine;

        public MineBattleNode(Mine mine) {
            super(mine);
            this.mine = mine;
        }

        @Override
        public void reset() {
            super.reset();
            this.nonCancellableSwing.v = 0;
            this.lockOnAttack.v = 0;
            this.fractionToWalkIntoAttackRange.v = 0.2;
            this.targetAcquisitionRange.v = 8;
            this.attackRange.v = 1.9;
            this.attackDamage.v = 50;
            this.attackSwingTime.v = 1.5;
            this.attackCooldown.v = 3;
            this.startingHp.v = 400;
            this.hp.v = 400;
            this.isAttackable.v = 1;
            this.attackState.v = BattleNode.ATTACK_STATE_READY;
            this.target.v = null;

        }

        @Override
        public void onAttackCast(BattleSystem battleSystem, BattleNode target) {
            battleSystem.findAttackablesWithinRange(mine.battleTargets, mine.battleNode, mine.battleNode.attackRange.v, BattleSystem.DEFAULT_TARGET_CRITERIA);

            for (int j = mine.battleTargets.size() - 1; j >= 0; j--) {
                BattleNode toInflict = mine.battleTargets.get(j).v;
                toInflict.inflictDamage(battleSystem, this, this.attackDamage.v);
            }
        }
    }

    public void configure(Gamer gamer) {
        movementNode.maxSpeed.v = 0.9;

        battleNode.gamer.v = gamer;
        battleNode.reset();

        renderNode.color.v = Gamer.TeamColors.get(gamer.team) & 0xaaffffff;
        renderNode.animationName.v = Animations.ANIMATION_MINE_IDLING;
        renderNode.isGfxInterpolated.v = 0;
        renderNode.width.v = 0.95f;
        renderNode.height.v = 0.95f;
        renderNode.z.v = 2;
    }
}
