package noteworthyengine;

import android.graphics.Color;

import art.Animations;
import noteworthyframework.Gamer;
import structure.Sprite2dDef;
import utils.VoidFunc;
import utils.VoidFunc2;
import utils.VoidFunc3;

/**
 * Created by eric on 3/27/15.
 */
public class Missle extends Mine {

    public Missle(Gamer gamer) {
        super(gamer);

        this.movementNode.maxSpeed.v = 1;

        this.battleNode.attackDamage.v = 50;
        this.battleNode.attackSwingTime.v = 3;
        this.battleNode.attackRange.v = 1.5;
        this.battleNode.targetAcquisitionRange.v = 20;
        this.battleNode.hp.v = 100;
        this.battleNode.fractionToWalkIntoAttackRange.v = 0.1;
        this.battleNode.stickyAttack.v = 0;
        this.battleNode.onAttackReady = new VoidFunc3<BattleSystem, BattleNode, BattleNode>() {
            @Override
            public void apply(BattleSystem battleSystem, BattleNode battleNode, BattleNode battleNode2) {
                battleNode.attackState.v = BattleNode.ATTACK_STATE_SWINGING;
                battleNode.attackProgress.v = 0;
            }
        };
        this.battleNode.onAttackCast = new VoidFunc3<BattleSystem, BattleNode, BattleNode>() {
            @Override
            public void apply(BattleSystem battleSystem, BattleNode battleNode, BattleNode battleNode2) {
                battleSystem.findEnemiesWithinRange(battleTargets, battleNode, battleNode.attackRange.v);

                for (int j = battleTargets.size() - 1; j >= 0; j--) {
                    BattleNode toInflict = battleTargets.get(j).v;
                    toInflict.inflictDamage.apply(battleSystem, toInflict, battleNode, battleNode.attackDamage);
                }

                battleNode.hp.v = 0;
            }
        };
        this.battleNode.onAttackCastFail = new VoidFunc2<BattleSystem, BattleNode>() {
            @Override
            public void apply(BattleSystem battleSystem, BattleNode arg1) {
                battleNode.hp.v = 0;
            }
        };

        this.renderNode.animationName = Animations.ANIMATION_PROJECTILE_BASIC;
        this.renderNode.width.v = 1.1f;
        this.renderNode.height.v = 1.1f;
        this.renderNode.onDraw = new VoidFunc<RenderSystem>() {
            @Override
            public void apply(RenderSystem system) {

                Sprite2dDef sprite2dDef = system.drawCompat.spriteAllocator.takeNextWritable();
                sprite2dDef.set(Animations.ANIMATION_TROOPS_SELECTED, 1,
                        (float)renderNode.coords.pos.x, (float)renderNode.coords.pos.y, 0,
                        1.3f, 1.3f,
                        90f,
                        Color.argb(60, 255, 255, 255),
                        0);

                if (battleNode.attackState.v == BattleNode.ATTACK_STATE_SWINGING) {
                    float ratio = (float)(battleNode.attackProgress.v / battleNode.attackSwingTime.v);
                    float rad = (float)(battleNode.attackRange.v * ratio);

                    sprite2dDef = system.drawCompat.spriteAllocator.takeNextWritable();
                    sprite2dDef.isGfxInterpolated = false;
                    sprite2dDef.animationName = Animations.ANIMATION_MINE_EXPLODING;
                    sprite2dDef.animationProgress = (int) (ratio * 100);
                    sprite2dDef.position.x = battleNode.coords.pos.x;
                    sprite2dDef.position.y = battleNode.coords.pos.y;
                    sprite2dDef.position.z = 3;
                    sprite2dDef.angle = 0;
                    sprite2dDef.width = 2 * rad;
                    sprite2dDef.height = 2 * rad;
                    sprite2dDef.color = renderNode.color.v;
                }
            }
        };

    }
}
