package noteworthyengine.units;

import android.graphics.Color;

import art.Animations;
import art.Constants;
import noteworthyengine.battle.BattleEffect;
import noteworthyengine.battle.BattleNode;
import noteworthyengine.battle.BattleSystem;
import noteworthyengine.DestinationMovementNode;
import noteworthyengine.DestinationMovementSystem;
import noteworthyengine.GridNode;
import noteworthyengine.RenderNode;
import noteworthyengine.RenderSystem;
import noteworthyengine.SeparationNode;
import noteworthyengine.players.PlayerUnit;
import noteworthyframework.Unit;
import structure.RewriteOnlyArray;
import structure.TemporarySprite2dDef;
import utils.BooleanFunc2;
import utils.Vector2;
import utils.VoidFunc;

/**
 * Created by eric on 3/27/15.
 */
public class Missle extends Unit {

    public static final String NAME = "missle";

    public static final int MAX_BATTLE_NODES_AFFECTED = 20;

    public GridNode gridNode;
    public DestinationMovementNode destinationMovementNode = new DestinationMovementNode(this);
    public SeparationNode separationNode = new SeparationNode(this);
    public BattleNode battleNode = new MissileBattleNode(this);
    public RenderNode renderNode = new RenderNode(this);

    public RewriteOnlyArray<BattleNode.Target> battleTargets =
            new RewriteOnlyArray<BattleNode.Target>(BattleNode.Target.class, MAX_BATTLE_NODES_AFFECTED);


    private Vector2 firingSource = new Vector2();
    private Vector2 temp = new Vector2();


    public Missle() {
        this.name = NAME;

        gridNode = new GridNode(this, separationNode, battleNode);

        this.destinationMovementNode.onDestinationReached = new VoidFunc<DestinationMovementSystem>() {
            @Override
            public void apply(DestinationMovementSystem element) {
                battleNode.battleState.v = BattleNode.BATTLE_STATE_SWINGING;
                battleNode.battleProgress.v = 0;
                battleNode.hp.v = 0;
            }
        };

        this.renderNode.onDraw = new VoidFunc<RenderSystem>() {
            @Override
            public void apply(RenderSystem system) {
                Vector2.subtract(temp, battleNode.coords.pos, firingSource);
                temp.setNormalized();

                system.drawLine(system.getCameraIndex(renderNode.renderLayer.v),
                        (float) (firingSource.x + 0.5 * (temp.x)),
                        (float) (firingSource.y + 0.5 * (temp.y)),
                        (float) (battleNode.coords.pos.x - 0.5 * (temp.x)),
                        (float) (battleNode.coords.pos.y - 0.5 * (temp.y)),
                        1, Color.argb(50, 255, 255, 200));

                system.defineNewSprite(Animations.ANIMATION_TROOPS_SELECTED, 1,
                        (float) renderNode.coords.pos.x, (float) renderNode.coords.pos.y, 0,
                        1.4f, 1.4f,
                        90f,
                        Color.argb(50, 255, 255, 255),
                        RenderNode.RENDER_LAYER_FOREGROUND);

                if (battleNode.target.v != null) {
                    system.defineNewSprite(Animations.ANIMATION_TROOPS_SELECTED, 1,
                            (float) destinationMovementNode.destination.x, (float) destinationMovementNode.destination.y, 0,
                            1.3f, 1.3f,
                            90f,
                            renderNode.color.v,
                            RenderNode.RENDER_LAYER_FOREGROUND);
                }

                if (battleNode.battleState.v == BattleNode.BATTLE_STATE_SWINGING) {
                    float ratio = (float)(battleNode.battleProgress.v / battleNode.attackSwingTime.v);
                    float rad = (float)(battleNode.attackRange.v * ratio);

                    system.defineNewSprite(Animations.ANIMATION_MINE_EXPLODING, (int) (ratio * 100),
                            (float) battleNode.coords.pos.x,
                            (float) battleNode.coords.pos.y,
                            3,
                            (float) 2 * rad,
                            (float) 2 * rad,
                            0,
                            renderNode.color.v,
                            RenderNode.RENDER_LAYER_FOREGROUND);
                }

                if (!battleNode.isAlive()) {
                    TemporarySprite2dDef tempSprite = system.beginNewTempSprite();

                    tempSprite.copy(Animations.ANIMATION_TROOPS_DYING_DEF);
                    tempSprite.position.x = battleNode.coords.pos.x;
                    tempSprite.position.y = battleNode.coords.pos.y;

                    system.endNewTempSprite(tempSprite, 0);
                }
            }
        };

    }

    public void configure(PlayerUnit playerUnit, Vector2 firingSource, Vector2 destination) {
        this.firingSource.copy(firingSource);
        this.destinationMovementNode.destination.copy(destination);
        this.destinationMovementNode.maxSpeed.v = 0.82;

        this.battleNode.reset();
        this.battleNode.playerUnitPtr.v = playerUnit;
        this.battleNode.nonCancellableSwing.v = 1;

        this.renderNode.animationName.v = Animations.ANIMATION_PROJECTILE_BASIC;
        this.renderNode.isGfxInterpolated.v = 0;
        this.renderNode.width.v = 1.2f;
        this.renderNode.height.v = 1.2f;
        this.renderNode.color.v = Constants.colorForTeam(playerUnit.playerNode.playerData.team);
    }

    public static class MissleBattleBehavior extends BattleEffect {

        protected static final BooleanFunc2<BattleNode, BattleNode> allTargetsEvenSelf = new BooleanFunc2<BattleNode, BattleNode>() {
            @Override
            public boolean apply(BattleNode battleNode, BattleNode battleNode2) {
                return BattleSystem.battleNodeShouldAttackOther(battleNode, battleNode2);
            }
        };

        public Missle missle;

        public MissleBattleBehavior(Missle missle) {
            this.missle = missle;
        }

        @Override
        public void onAttackReady(BattleSystem battleSystem, BattleNode target) {
            super.onAttackReady(battleSystem, target);

            // Swing immediately without waiting to find a target
            missle.battleNode.battleState.v = BattleNode.BATTLE_STATE_SWINGING;
            missle.battleNode.battleProgress.v = 0;
        }

        @Override
        public void onAttackCast(BattleSystem battleSystem, BattleNode target) {
            battleSystem.findAttackablesWithinRange(missle.battleTargets,
                    missle.battleNode,
                    missle.battleNode.attackRange.v,
                    allTargetsEvenSelf);

            for (int j = missle.battleTargets.size() - 1; j >= 0; j--) {
                BattleNode toInflict = missle.battleTargets.get(j).v;

                toInflict.onAttacked(battleSystem,
                        missle.battleNode,
                        missle.battleNode.attackDamage.v);

            }

            missle.battleNode.hp.v = 0;
        }

        @Override
        public void onAttackCastFail(BattleSystem battleSystem) {
            missle.battleNode.hp.v = 0;
        }

        @Override
        public void update(BattleSystem battleSystem, double dt) {
        }

        @Override
        public void sendEvent(BattleSystem battleSystem, BattleNode battleNode, Event event) {
        }

        @Override
        public void reset() {
        }
    }

    public static class MissileBattleNode extends BattleNode {

        public Missle missle;

        public MissileBattleNode(Missle missle) {
            super(missle);
            this.missle = missle;
            this.battleEffects.add(new MissleBattleBehavior(missle));
        }

        @Override
        public void reset() {
            super.reset();

            this.isAttackable.v = 0;
            this.attackDamage.v = 50;
            this.attackSwingTime.v = 1.5;
            this.attackRange.v = 2.4;
            this.attackCooldown.v = 3;
            this.targetAcquisitionRange.v = 20;
            this.hp.v = 60;
            this.fractionToWalkIntoAttackRange.v = 0.02;
            this.nonCancellableSwing.v = 0;
            this.target.v = null;
        }
    }
}
