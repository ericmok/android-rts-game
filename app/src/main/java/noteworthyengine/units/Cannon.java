package noteworthyengine.units;

import android.graphics.Color;

import art.Animations;
import noteworthyengine.BattleNode;
import noteworthyengine.BattleSystem;
import noteworthyengine.FieldNode;
import noteworthyengine.GridNode;
import noteworthyengine.MovementNode;
import noteworthyengine.RenderNode;
import noteworthyengine.RenderSystem;
import noteworthyengine.SeparationNode;
import noteworthyframework.Gamer;
import noteworthyframework.Unit;
import structure.Sprite2dDef;
import structure.TemporarySprite2dDef;
import utils.VoidFunc;
import utils.VoidFunc3;

/**
 * Created by eric on 3/23/15.
 */
public class Cannon extends Unit {

    public MovementNode movementNode = new MovementNode(this);

    public FieldNode fieldNode = new FieldNode(this) {{
        this._fieldAgentNode = new FieldAgentNode(Cannon.this);
    }};

    public SeparationNode separationNode = new SeparationNode(this);

    public BattleNode battleNode = new CannonBattleNode(this);
    public RenderNode renderNode = new RenderNode(this);

    public GridNode gridNode = new GridNode(this, separationNode, battleNode);

    public Cannon(final Gamer gamer) {
        this.name = this.getClass().getSimpleName();

        this.reset();
        this.configure(gamer);

        this.renderNode.onDraw = new VoidFunc<RenderSystem>() {
            @Override
            public void apply(RenderSystem system) {
                renderNode.color.v = Gamer.TeamColors.get(battleNode.gamer.v.team);

                if (battleNode.hp.v <= 0) {
                    TemporarySprite2dDef tempSprite = system.beginNewTempSprite(); //system.defineNewTempSprite(Animations.ANIMATION_TROOPS_DYING_DEF, 0);
                    tempSprite.copy(Animations.ANIMATION_TROOPS_DYING_DEF);
                    tempSprite.position.x = battleNode.coords.pos.x;
                    tempSprite.position.y = battleNode.coords.pos.y;
                    system.endNewTempSprite(tempSprite, 0);
                }

                if (battleNode.attackState.v == BattleNode.ATTACK_STATE_SWINGING) {

                    if (battleNode.target.v != null) {
                        double ratio = battleNode.attackProgress.v;

                        Sprite2dDef sprite2dDef = system.defineNewSprite(
                                Animations.ANIMATION_TROOPS_SWING, (int) ratio,
                                (float)battleNode.coords.pos.x, (float)battleNode.coords.pos.y - renderNode.height.v, 0,
                                1f, 1f,
                                90,
                                Gamer.colorForTeam(battleNode.gamer.v.team), 0
                                );
                    }
                }
            }
        };
    }

    public void configure(Gamer gamer) {
        this.battleNode.gamer.v = gamer;
    }

    public void reset() {
        this.movementNode.maxSpeed.v = 0.32;

        battleNode.reset();

        this.renderNode.animationName.v = "Animations/Cannons/Idling";
        this.renderNode.width.v = 1.4f;
        this.renderNode.height.v = 1.4f;
    }

    public static class CannonBattleNode extends BattleNode {
        private Cannon cannon;

        public CannonBattleNode(Cannon cannon) {
            super(cannon);
            this.cannon = cannon;
        }

        @Override
        public void reset() {
            this.hp.v = 5;
            this.attackSwingTime.v = 6;
            this.attackCooldown.v = 20;
            this.attackDamage.v = 0;
            this.attackRange.v = 6;
            this.targetAcquisitionRange.v = 18.5;
            this.attackState.v = BattleNode.ATTACK_STATE_READY;
        }

        @Override
        public void onAttackCast(BattleSystem battleSystem, BattleNode target) {
            super.onAttackCast(battleSystem, target);
            //Missle missle = new Missle(battleNode.gamer.v);
            Missle missle = UnitPool.missles.fetchMemory();
            missle.configure(this.gamer.v, this.coords.pos, target.coords.pos);
            missle.movementNode.coords.pos.copy(this.coords.pos);
            battleSystem.getBaseEngine().addUnit(missle);
        }
    }
}
