package noteworthyengine.units;

import art.Animations;
import art.Constants;
import noteworthyengine.FieldNode;
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
import structure.TemporarySprite2dDef;
import utils.VoidFunc;

/**
 * Created by eric on 10/28/15.
 */
public class PufferZug extends Unit {

    public static final String NAME = "explosion";

    public static final int MAX_BATTLE_NODES_AFFECTED = 20;

    public GridNode gridNode;
    public FieldNode fieldNode;
    public MovementNode movementNode = new MovementNode(this);
    public SeparationNode separationNode = new SeparationNode(this);
    public BattleNode battleNode = new PufferZugBattleNode(this);
    public RenderNode renderNode = new RenderNode(this);

    public RewriteOnlyArray<BattleNode.Target> battleTargets =
            new RewriteOnlyArray<BattleNode.Target>(BattleNode.Target.class, MAX_BATTLE_NODES_AFFECTED);

    public PufferZug() {
        this.name = NAME;

        this.addNode(QuadTreeSystem.class, new QuadTreeSystem.QuadTreeNode(this));

        gridNode = new GridNode(this, separationNode, battleNode);
        fieldNode = FieldNode.createAgentFieldNode(this);
        battleNode.battleEffects.add(new SuicidalAOEAttackBattleEffect());
        //battleNode.battleEffects.add(new PufferZugDieOnAttackBattleEffect());

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

                    system.defineNewSprite(Animations.ANIMATION_MINE_EXPLODING,
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
            }
        };
    }

    public static class PufferZugBattleNode extends BattleNode {
        private PufferZug pufferZug;

        public PufferZugBattleNode(PufferZug pufferZug) {
            super(pufferZug);
            this.pufferZug = pufferZug;
        }

        @Override
        public void reset() {
            super.reset();
            this.nonCancellableSwing.v = 0;
            this.lockOnAttack.v = 0;
            this.fractionToWalkIntoAttackRange.v = 0.2;
            this.targetAcquisitionRange.v = 17;
            this.battleAttack.range = 1.9;
            this.battleAttack.amount = 80;
            this.battleAttack.swingTime = 1.5;
            this.battleAttack.cooldownTime = 3;
            this.startingHp.v = 50;
            this.hp.v = 50;
            this.isAttackable.v = 1;
            this.target.v = null;

        }
    }

    public void configure(PlayerUnit playerUnit) {
        movementNode.reset();
        battleNode.reset();

        movementNode.maxSpeed.v = 0.9;

        battleNode.playerUnitPtr.v = playerUnit;

        renderNode.color.v = Constants.colorForTeam(playerUnit.playerNode.playerData.team);
        renderNode.animationName.v = Animations.ANIMATION_PUFFER_ZUG_IDLING;
        renderNode.isGfxInterpolated.v = 0;
        renderNode.width.v = 0.95f;
        renderNode.height.v = 0.95f;
        renderNode.z.v = 2;
    }
}
