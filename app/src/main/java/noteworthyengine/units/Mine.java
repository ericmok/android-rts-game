package noteworthyengine.units;

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
import utils.VoidFunc3;

/**
 * Created by eric on 3/21/15.
 */
public class Mine extends Unit {

    public static final String NAME = "explosion";

    public static final int MAX_BATTLE_NODES_AFFECTED = 20;

    public GridNode gridNode;
    public MovementNode movementNode = new MovementNode(this);
    public SeparationNode separationNode = new SeparationNode(this);
    public BattleNode battleNode = new BattleNode(this);
    public RenderNode renderNode = new RenderNode(this);

    public RewriteOnlyArray<BattleNode.Target> battleTargets =
            new RewriteOnlyArray<BattleNode.Target>(BattleNode.Target.class, MAX_BATTLE_NODES_AFFECTED);

    public Mine(Gamer gamer) {
        this.name = NAME;

        gridNode = new GridNode(this, separationNode, battleNode);

        movementNode.maxSpeed.v = 1.3;

        battleNode.stickyAttack.v = 0;
        battleNode.fractionToWalkIntoAttackRange.v = 0.3;
        battleNode.targetAcquisitionRange.v = 8;
        battleNode.attackRange.v = 2.5;
        battleNode.attackDamage.v = 50;
        battleNode.attackSwingTime.v = 4.5;
        battleNode.attackCooldown.v = 3;
        battleNode.hp.v = 9;
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
                battleSystem.findAttackablesWithinRange(battleTargets, battleNode, battleNode.attackRange.v, BattleSystem.DEFAULT_TARGET_CRITERIA);

                for (int j = battleTargets.size() - 1; j >= 0; j--) {
                    BattleNode toInflict = battleTargets.get(j).v;
                    toInflict.inflictDamage.apply(battleSystem, toInflict, battleNode, battleNode.attackDamage);
                }
            }
        };

        renderNode.animationName.v = Animations.ANIMATION_MINE_IDLING;
        renderNode.isGfxInterpolated.v = 0;
        renderNode.color.v = Gamer.TeamColors.get(gamer.team) & 0x80ffffff;

        renderNode.width.v = 0.9f;
        renderNode.height.v = 0.9f;
        renderNode.z.v = 2;
        renderNode.onDraw = new VoidFunc<RenderSystem>() {
            @Override
            public void apply(RenderSystem system) {
                if (battleNode.hp.v <= 0) {
                    TemporarySprite2dDef tempSprite = system.drawCompat.tempSpritesMemoryPool.fetchMemory();

                    tempSprite.copy(Animations.ANIMATION_TROOPS_DYING_DEF);
                    tempSprite.setPosition((float)battleNode.coords.pos.x, (float)battleNode.coords.pos.y, 1);

                    system.drawCompat.drawTemporarySprite(tempSprite);
                    system.drawCompat.tempSpritesMemoryPool.recycleMemory(tempSprite);
                }

                if (battleNode.attackState.v == BattleNode.ATTACK_STATE_SWINGING) {
                    float ratio = (float)(battleNode.attackProgress.v / battleNode.attackSwingTime.v);
                    float rad = (float)(battleNode.attackRange.v * ratio);

                    Sprite2dDef sprite2dDef = system.drawCompat.spriteAllocator.takeNextWritable();
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

                    //renderNode.animationName = Sprite2dDef.ANIMATION_MINE_EXPLODING;
                    //renderNode.width.v = 2 * rad;
                    //renderNode.height.v = 2 * rad;
                    //renderNode.animationProgress.v = (int) (ratio * 100);
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
}
