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
public class Explosion extends Unit {

    public static final String NAME = "explosion";

    public static final int MAX_BATTLE_NODES_AFFECTED = 20;

    public BattleNode battleNode = new BattleNode(this);
    public RenderNode renderNode = new RenderNode(this);

    public RewriteOnlyArray<BattleNode.Target> battleTargets =
            new RewriteOnlyArray<BattleNode.Target>(BattleNode.Target.class, MAX_BATTLE_NODES_AFFECTED);

    public Explosion(Gamer gamer) {
        this.name = NAME;

        battleNode.stickyAttack.v = 0;
        battleNode.targetAcquisitionRange.v = 50;
        battleNode.attackRange.v = 5;
        battleNode.attackDamage.v = 4;
        battleNode.attackSwingTime.v = 5;
        battleNode.attackCooldown.v = 1;
        battleNode.hp.v = 1000000;
        battleNode.isAttackable.v = 0;
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

        renderNode.animationName = Sprite2dDef.ANIMATION_RETICLE_TAP;
        renderNode.isGfxInterpolated.v = 0;
        renderNode.color.v = Color.argb(50, 255, 255, 255);
        renderNode.width.v = 10;
        renderNode.height.v = 10;
        renderNode.z.v = 2;
        renderNode.onDraw = new VoidFunc<RenderSystem>() {
            @Override
            public void apply(RenderSystem element) {
                if (battleNode.attackState.v == BattleNode.ATTACK_STATE_SWINGING) {
                    float rad = (float)(battleNode.attackRange.v * (battleNode.attackProgress.v / battleNode.attackSwingTime.v));
                    renderNode.width.v = rad;
                    renderNode.height.v = rad;
                }
            }
        };
    }
}
