package noteworthyengine.units;

import art.Animations;
import art.Constants;
import noteworthyengine.QuadTreeSystem;
import noteworthyengine.battle.BattleEffect;
import noteworthyengine.battle.BattleNode;
import noteworthyengine.FieldNode;
import noteworthyengine.GridNode;
import noteworthyengine.MovementNode;
import noteworthyengine.RenderNode;
import noteworthyengine.RenderSystem;
import noteworthyengine.SelectionNode;
import noteworthyengine.SeparationNode;
import noteworthyengine.battle.BattleSystem;
import noteworthyengine.players.PlayerUnit;
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

        this.addNode("QuadTreeNode created in constructor", new QuadTreeSystem.QuadTreeNode(this));

        this.battleNode.battleEffects.add(new MissileAttackEffect(this.battleNode));
        this.renderNode.onDraw = new VoidFunc<RenderSystem>() {
            @Override
            public void apply(RenderSystem system) {
                time += 1;

                renderNode.color.v = Constants.colorForTeam(battleNode.playerUnitPtr.v.playerNode.playerData.team);

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
                            Constants.colorForTeam(battleNode.playerUnitPtr.v.playerNode.playerData.team),
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
                        double ratio = (battleNode.battleProgress.v / battleNode.battleAttack.swingTime) * 100;

                        Sprite2dDef sprite2dDef = system.defineNewSprite(
                                Animations.ANIMATION_TROOPS_SWING, (int) ratio,
                                (float)battleNode.coords.pos.x, (float)battleNode.coords.pos.y - renderNode.height.v, 0,
                                1f, 1f,
                                90,
                                Constants.colorForTeam(battleNode.playerUnitPtr.v.playerNode.playerData.team), RenderNode.RENDER_LAYER_FOREGROUND
                                );
                    }
                }
            }
        };
    }

    public void configure(PlayerUnit playerUnit) {
        this.movementNode.reset();
        this.battleNode.reset();

        this.movementNode.maxSpeed.v = 0.32;

        this.battleNode.playerUnitPtr.v = playerUnit;

        this.renderNode.animationName.v = "Animations/Cannons/Idling";
        this.renderNode.width.v = 1.4f;
        this.renderNode.height.v = 1.4f;
    }

    public static class CannonBattleNode extends BattleNode {
        private Cannon cannon;

        public CannonBattleNode(Cannon cannon) {
            super(cannon);
            this.cannon = cannon;
            this.battleEffects.add(new MissileAttackEffect(this));
        }

        @Override
        public void reset() {
            super.reset();

            this.hp.v = 5;
            this.battleAttack.swingTime = 4;
            this.battleAttack.cooldownTime = 7;
            this.battleAttack.amount = 0;
            this.battleAttack.range = 6.5;
            this.targetAcquisitionRange.v = this.battleAttack.range + 3;
        }
//
//        @Override
//        public void onAttackCast(BattleSystem battleSystem, BattleNode target) {
//            super.onAttackCast(battleSystem, target);
//
//            Missile missile = UnitPool.missles.fetchMemory();
//            missile.configure(this.playerUnitPtr.v, this.coords.pos, target.coords.pos);
//            missile.battleNode.coords.pos.copy(this.coords.pos);
//            battleSystem.getBaseEngine().addUnit(missile);
//        }
    }

    public static class MissileAttackEffect extends BattleEffect {
        private BattleNode battleNode;

        public MissileAttackEffect(BattleNode battleNode) {
            this.battleNode = battleNode;
        }

        @Override
        public void update(BattleSystem battleSystem, double dt) {

        }

        @Override
        public void sendEvent(BattleSystem battleSystem, BattleNode battleNode, Event event) {
            if (event == Event.FIND_NEW_TARGET) {
                battleSystem.findAttackablesWithinRange(battleNode.target, battleNode, battleNode.battleAttack.range, BattleSystem.DEFAULT_TARGET_CRITERIA);
            }
            if (event == Event.ATTACK_CAST) {
                Missile missile = UnitPool.missles.fetchMemory();
                missile.configure(battleNode.playerUnitPtr.v, battleNode.coords.pos, battleNode.target.v.coords.pos);
                missile.battleNode.coords.pos.copy(battleNode.coords.pos);
                missile.destinationMovementNode.destination.copy(battleNode.target.v.coords.pos);
                battleSystem.getBaseEngine().addUnit(missile);
            }
        }
    }
}
