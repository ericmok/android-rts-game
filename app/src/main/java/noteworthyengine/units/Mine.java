package noteworthyengine.units;

import art.Animations;
import art.Constants;
import noteworthyengine.GridNode;
import noteworthyengine.MovementNode;
import noteworthyengine.QuadTreeSystem;
import noteworthyengine.RenderNode;
import noteworthyengine.RenderSystem;
import noteworthyengine.SeparationNode;
import noteworthyengine.battle.BattleNode;
import noteworthyengine.battle.SuicidalAOEAttackBattleEffect;
import noteworthyengine.players.PlayerUnit;
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

        this.addNode(QuadTreeSystem.QuadTreeNode.class, new QuadTreeSystem.QuadTreeNode(this));

        gridNode = new GridNode(this, separationNode, battleNode);
        // TODO: Add a different BattleEffect that isn't suicidal
        battleNode.battleEffects.add(new SuicidalAOEAttackBattleEffect());

        renderNode.onDraw = new VoidFunc<RenderSystem>() {
            @Override
            public void apply(RenderSystem system) {

                if (!battleNode.isAlive()) {
                    TemporarySprite2dDef tempSprite = system.beginNewTempSprite();

                    tempSprite.copy(Animations.ANIMATION_TROOPS_DYING_DEF);
                    tempSprite.setPosition((float) renderNode.coords.pos.x, (float) renderNode.coords.pos.y, 0);

                    system.endNewTempSprite(tempSprite, 0);
                }

                if (battleNode.battleState.v == BattleNode.BATTLE_STATE_SWINGING) {
                    float ratio = (float)(battleNode.battleProgress.v / battleNode.battleAttack.swingTime);
                    float rad = (float)(battleNode.battleAttack.range * ratio);

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
            this.battleAttack.range = 1.9;
            this.battleAttack.amount = 50;
            this.battleAttack.swingTime = 1.5;
            this.battleAttack.cooldownTime = 3;
            this.startingHp.v = 200;
            this.hp.v = 200;
            this.isAttackable.v = 1;
            this.target.v = null;

        }

//        @Override
//        public void onAttackCast(BattleSystem battleSystem, BattleNode target) {
//            ArrayList<QuadTreeSystem.QuadTreeNode> battleTargets = battleSystem.findBattleNodesWithinRange(mine.battleNode, mine.battleNode.battleAttack.range, BattleSystem.DEFAULT_TARGET_CRITERIA);
//
//            for (int j = battleTargets.size() - 1; j >= 0; j--) {
//                BattleNode toInflict = (BattleNode)battleTargets.get(j).unit.node(BattleNode._NAME);
//                toInflict.onAttacked(battleSystem, this, this.battleAttack.amount);
//            }
//        }
    }

    public void configure(PlayerUnit playerUnit) {
        movementNode.reset();
        battleNode.reset();

        movementNode.maxSpeed.v = 0.9;

        battleNode.playerUnitPtr.v = playerUnit;

        renderNode.color.v = Constants.colorForTeam(playerUnit.playerNode.playerData.team) & 0xaaffffff;
        renderNode.animationName.v = Animations.ANIMATION_MINE_IDLING;
        renderNode.isGfxInterpolated.v = 0;
        renderNode.width.v = 0.95f;
        renderNode.height.v = 0.95f;
        renderNode.z.v = 2;
    }
}
