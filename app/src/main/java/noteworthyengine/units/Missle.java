package noteworthyengine.units;

import android.graphics.Color;

import art.Animations;
import noteworthyengine.BattleNode;
import noteworthyengine.BattleSystem;
import noteworthyengine.DestinationMovementNode;
import noteworthyengine.DestinationMovementSystem;
import noteworthyengine.RenderSystem;
import noteworthyframework.Gamer;
import structure.TemporarySprite2dDef;
import utils.BooleanFunc2;
import utils.Vector2;
import utils.VoidFunc;
import utils.VoidFunc2;
import utils.VoidFunc3;

/**
 * Created by eric on 3/27/15.
 */
public class Missle extends Mine {

    public static final String NAME = "missle";
    public DestinationMovementNode destinationMovementNode = new DestinationMovementNode(this);

    private Vector2 firingSource = new Vector2();
    private Vector2 temp = new Vector2();

    public Missle(Gamer gamer) {
        super(gamer);

        this.movementNode._enabled = false;

        this.battleNode.onAttackReady = new VoidFunc3<BattleSystem, BattleNode, BattleNode>() {
            @Override
            public void apply(BattleSystem battleSystem, BattleNode battleNode, BattleNode battleNode2) {
                battleNode.attackState.v = BattleNode.ATTACK_STATE_SWINGING;
                battleNode.attackProgress.v = 0;
            }
        };

        final BooleanFunc2<BattleNode, BattleNode> allTargetsEvenSelf = new BooleanFunc2<BattleNode, BattleNode>() {
            @Override
            public boolean apply(BattleNode battleNode, BattleNode battleNode2) {
                return BattleSystem.battleNodeShouldAttackOther(battleNode, battleNode2);
            }
        };

        this.battleNode.onAttackCast = new VoidFunc3<BattleSystem, BattleNode, BattleNode>() {
            @Override
            public void apply(BattleSystem battleSystem, BattleNode battleNode, BattleNode battleNode2) {
                battleSystem.findAttackablesWithinRange(battleTargets, battleNode, battleNode.attackRange.v, allTargetsEvenSelf);

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

        this.destinationMovementNode.onDestinationReached = new VoidFunc<DestinationMovementSystem>() {
            @Override
            public void apply(DestinationMovementSystem element) {
                battleNode.attackState.v = BattleNode.ATTACK_STATE_SWINGING;
                battleNode.attackProgress.v = 0;
                battleNode.hp.v = 0;
            }
        };

        this.renderNode.onDraw = new VoidFunc<RenderSystem>() {
            @Override
            public void apply(RenderSystem system) {
                Vector2.subtract(temp, battleNode.coords.pos, firingSource);
                temp.setNormalized();

                system.drawLine(system.getCameraId(renderNode.cameraType.v),
                        (float) (firingSource.x + 0.5 * (temp.x)),
                        (float) (firingSource.y + 0.5 * (temp.y)),
                        (float) (battleNode.coords.pos.x - 0.5 * (temp.x)),
                        (float) (battleNode.coords.pos.y - 0.5 * (temp.y)),
                        1, Color.argb(50, 255, 255, 200));

                system.defineNewSprite(Animations.ANIMATION_TROOPS_SELECTED, 1,
                        (float) renderNode.coords.pos.x, (float) renderNode.coords.pos.y, 0,
                        1.4f, 1.4f,
                        90f,
                        Color.argb(40, 255, 255, 255),
                        0);

                if (battleNode.target.v != null) {
                    system.defineNewSprite(Animations.ANIMATION_TROOPS_SELECTED, 1,
                            (float) battleNode.target.v.coords.pos.x, (float) battleNode.target.v.coords.pos.y, 0,
                            1.8f, 1.8f,
                            90f,
                            renderNode.color.v & 0x88FFFFFF,
                            0);
                }

                if (battleNode.attackState.v == BattleNode.ATTACK_STATE_SWINGING) {
                    float ratio = (float)(battleNode.attackProgress.v / battleNode.attackSwingTime.v);
                    float rad = (float)(battleNode.attackRange.v * ratio);

                    system.defineNewSprite(Animations.ANIMATION_MINE_EXPLODING, (int) (ratio * 100),
                            (float) battleNode.coords.pos.x,
                            (float) battleNode.coords.pos.y,
                            3,
                            (float) 2 * rad,
                            (float) 2 * rad,
                            0,
                            renderNode.color.v,
                            0);
                }

                if (battleNode.hp.v <= 0) {
                    TemporarySprite2dDef tempSprite = system.beginNewTempSprite();

                    tempSprite.copy(Animations.ANIMATION_TROOPS_DYING_DEF);
                    tempSprite.position.x = battleNode.coords.pos.x;
                    tempSprite.position.y = battleNode.coords.pos.y;

                    system.endNewTempSprite(tempSprite, 0);
                }
            }
        };

    }

    public void reset() {
        super.reset();

        this.movementNode.maxSpeed.v = 0.75;

        this.battleNode.isAttackable.v = 1;
        this.battleNode.attackDamage.v = 25;
        this.battleNode.attackSwingTime.v = 3;
        this.battleNode.attackRange.v = 2.5;
        this.battleNode.targetAcquisitionRange.v = 20;
        this.battleNode.hp.v = 60;
        this.battleNode.fractionToWalkIntoAttackRange.v = 0.02;
        this.battleNode.stickyAttack.v = 0;

        this.renderNode.animationName.v = Animations.ANIMATION_PROJECTILE_BASIC;
        this.renderNode.width.v = 1.2f;
        this.renderNode.height.v = 1.2f;
    }

    public void configure(Gamer gamer, Vector2 firingSource, Vector2 destination) {
        this.battleNode.gamer.v = gamer;
        this.renderNode.color.v = Gamer.TeamColors.get(gamer.team);

        this.firingSource.copy(firingSource);
        destinationMovementNode.destination.copy(destination);
    }
}
