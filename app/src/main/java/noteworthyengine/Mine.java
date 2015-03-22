package noteworthyengine;

import android.graphics.Color;

import noteworthyframework.Gamer;
import noteworthyframework.Unit;
import structure.RewriteOnlyArray;
import structure.Sprite2dDef;
import utils.VoidFunc;
import utils.VoidFunc2;
import utils.VoidFunc3;

/**
 * Created by eric on 3/21/15.
 */
public class Mine extends Unit {

    public static final String NAME = "explosion";

    public static final int MAX_BATTLE_NODES_AFFECTED = 20;

    public BattleNode battleNode = new BattleNode(this);
    public RenderNode renderNode = new RenderNode(this);

    public RewriteOnlyArray<BattleNode.Target> battleTargets =
            new RewriteOnlyArray<BattleNode.Target>(BattleNode.Target.class, MAX_BATTLE_NODES_AFFECTED);

    public Mine(Gamer gamer) {
        this.name = NAME;

        battleNode.stickyAttack.v = 0;
        battleNode.targetAcquisitionRange.v = 7;
        battleNode.attackRange.v = 7;
        battleNode.attackDamage.v = 20;
        battleNode.attackSwingTime.v = 5;
        battleNode.attackCooldown.v = 1;
        battleNode.hp.v = 10;
        battleNode.isAttackable.v = 1;
        battleNode.attackState.v = BattleNode.ATTACK_STATE_READY;
        battleNode.gamer.v = gamer;
        battleNode.onAttackReady = new VoidFunc3<BattleSystem, BattleNode, BattleNode>() {
            @Override
            public void apply(BattleSystem battleSystem, BattleNode battleNode, BattleNode battleNode2) {
                //battleSystem.getBaseEngine().removeUnit(battleNode.unit);
                // Else go to swing
            }
        };
        battleNode.onAttackCast = new VoidFunc3<BattleSystem, BattleNode, BattleNode>() {
            @Override
            public void apply(BattleSystem battleSystem, BattleNode battleNode, BattleNode battleNode2) {
                battleSystem.findEnemiesWithinRange(battleTargets, battleNode, battleNode.attackRange.v);

                for (int j = battleTargets.size() - 1; j >= 0; j--) {
                    BattleNode toInflict = battleTargets.get(j).v;
                    toInflict.inflictDamage.apply(battleSystem, toInflict, battleNode, battleNode.attackDamage.v);
                }
            }
        };

        renderNode.animationName = Sprite2dDef.ANIMATION_MINE_IDLING;
        renderNode.isGfxInterpolated.v = 0;
        renderNode.color.v = Gamer.TeamColors.get(gamer.team) & 0x80ffffff;

        renderNode.width.v = 0.9f;
        renderNode.height.v = 0.9f;
        renderNode.z.v = 2;
        renderNode.onDraw = new VoidFunc<RenderSystem>() {
            @Override
            public void apply(RenderSystem element) {

                if (battleNode.attackState.v == BattleNode.ATTACK_STATE_SWINGING) {
                    renderNode.animationName = Sprite2dDef.ANIMATION_MINE_EXPLODING;
                    float ratio = (float)(battleNode.attackProgress.v / battleNode.attackSwingTime.v);
                    float rad = (float)(battleNode.attackRange.v * ratio);
                    renderNode.width.v = rad;
                    renderNode.height.v = rad;
                    renderNode.animationProgress.v = (int) (ratio * 100);
                } else {
                    renderNode.animationName = Sprite2dDef.ANIMATION_MINE_IDLING;
                    renderNode.animationProgress.v = 0;
                    renderNode.width.v = 0.9f;
                    renderNode.height.v = 0.9f;
                }
            }
        };
    }
}