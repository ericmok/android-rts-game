package noteworthyengine.units;

import art.Animations;
import art.Constants;
import noteworthyengine.BattleNode;
import noteworthyengine.BattleSystem;
import noteworthyengine.FieldNode;
import noteworthyengine.GridNode;
import noteworthyengine.MovementNode;
import noteworthyengine.RenderNode;
import noteworthyengine.RenderSystem;
import noteworthyengine.SelectionNode;
import noteworthyengine.SeparationNode;
import noteworthyframework.Gamer;
import noteworthyframework.Unit;
import structure.Sprite2dDef;
import structure.TemporarySprite2dDef;
import utils.VoidFunc;

/**
 * Created by eric on 3/23/15.
 */
public class Cannon extends Unit {

    public MovementNode movementNode = new MovementNode(this);

    public FieldNode fieldNode = FieldNode.createAgentFieldNode(this);

    public SeparationNode separationNode = new SeparationNode(this);
    public SelectionNode selectionNode = new SelectionNode(this);

    public BattleNode battleNode = new CannonBattleNode(this);
    public RenderNode renderNode = new RenderNode(this);

    public GridNode gridNode = new GridNode(this, separationNode, battleNode);

    private float time = 0;

    public Cannon() {
        this.name = this.getClass().getSimpleName();

        this.renderNode.onDraw = new VoidFunc<RenderSystem>() {
            @Override
            public void apply(RenderSystem system) {
                time += 1;

                renderNode.color.v = Constants.colorForTeam(battleNode.gamer.v.team);

                if (selectionNode.isSelected.v == 1) {
                    system.defineNewSprite(
                            Animations.ANIMATION_BUTTONS_ACTIVATED,
                            1,
                            (float) renderNode.coords.pos.x,
                            (float) renderNode.coords.pos.y,
                            1,
                            1.7f, 1.7f,
                            time,
                            //Color.argb(128, 255, 255, 255),
                            Constants.colorForTeam(battleNode.gamer.v.team),
                            RenderNode.RENDER_LAYER_FOREGROUND
                    );
                }

                if (!battleNode.isAlive()) {
                    TemporarySprite2dDef tempSprite = system.beginNewTempSprite(); //system.defineNewTempSprite(Animations.ANIMATION_TROOPS_DYING_DEF, 0);
                    tempSprite.copy(Animations.ANIMATION_TROOPS_DYING_DEF);
                    tempSprite.position.x = battleNode.coords.pos.x;
                    tempSprite.position.y = battleNode.coords.pos.y;
                    system.endNewTempSprite(tempSprite, 0);
                }

                if (battleNode.battleState.v == BattleNode.BATTLE_STATE_SWINGING) {

                    if (battleNode.target.v != null) {
                        double ratio = (battleNode.battleProgress.v / battleNode.attackSwingTime.v) * 100;

                        Sprite2dDef sprite2dDef = system.defineNewSprite(
                                Animations.ANIMATION_TROOPS_SWING, (int) ratio,
                                (float)battleNode.coords.pos.x, (float)battleNode.coords.pos.y - renderNode.height.v, 0,
                                1f, 1f,
                                90,
                                Constants.colorForTeam(battleNode.gamer.v.team), RenderNode.RENDER_LAYER_FOREGROUND
                                );
                    }
                }
            }
        };
    }

    public void configure(Gamer gamer) {
        this.movementNode.reset();
        this.battleNode.reset();

        this.movementNode.maxSpeed.v = 0.32;

        this.battleNode.gamer.v = gamer;

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
            super.reset();

            this.hp.v = 5;
            this.attackSwingTime.v = 4;
            this.attackCooldown.v = 7;
            this.attackDamage.v = 0;
            this.attackRange.v = 6.5;
            this.targetAcquisitionRange.v = this.attackRange.v + 3;
        }

        @Override
        public void onAttackCast(BattleSystem battleSystem, BattleNode target) {
            super.onAttackCast(battleSystem, target);

            Missle missle = UnitPool.missles.fetchMemory();
            missle.configure(this.gamer.v, this.coords.pos, target.coords.pos);
            missle.battleNode.coords.pos.copy(this.coords.pos);
            battleSystem.getBaseEngine().addUnit(missle);
        }
    }
}
