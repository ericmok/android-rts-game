package noteworthyengine.units;

import android.graphics.Color;

import art.Animations;
import art.Constants;
import noteworthyengine.FieldNode;
import noteworthyengine.FormationNode;
import noteworthyengine.MovementNode;
import noteworthyengine.QuadTreeSystem;
import noteworthyengine.RenderNode;
import noteworthyengine.RenderSystem;
import noteworthyengine.SelectionNode;
import noteworthyengine.SeparationNode;
import noteworthyengine.battle.BasicAttackEffect;
import noteworthyengine.battle.BattleBalance;
import noteworthyengine.battle.BattleNode;
import noteworthyengine.battle.BattleSystem;
import noteworthyengine.players.PlayerUnit;
import noteworthyframework.Unit;
import structure.Sprite2dDef;
import structure.TemporarySprite2dDef;
import utils.Orientation;
import utils.VoidFunc;

//import noteworthyframework.Gamer;

/**
 * Created by eric on 3/7/15.
 */
public class Platoon extends Unit {
    public static final String NAME = "Troopy";

    //public GridNode gridNode;
    public MovementNode movementNode;

    public FieldNode fieldNode;
    public BattleNode battleNode;
    public SeparationNode separationNode;
    public FormationNode formationNode;
    public FormationNode.FormationSheep formationSheep;
    public SelectionNode selectionNode;

    public RenderNode renderNode;

    public boolean onAttackSwingAnim = false;
    public BattleNode[] target = new BattleNode[1];

    private float time = 0;

    public Platoon() {
        this.name = NAME;

        this.addNode(QuadTreeSystem.QuadTreeNode.class, new QuadTreeSystem.QuadTreeNode(this));

        movementNode = new MovementNode(this);

        fieldNode = FieldNode.createAgentFieldNode(this);

        battleNode = new BattleNode(this);
        battleNode.battleEffects.add(new PlatoonBattleEffect(this));

        renderNode = new RenderNode(this);
        renderNode.onDraw = this.onDraw;

        separationNode = new SeparationNode(this);
        formationNode = new FormationNode(this);

        //gridNode = new GridNode(this, separationNode, battleNode);
        selectionNode = new SelectionNode(this);

        formationSheep = new FormationNode.FormationSheep(this);
    }

    public void configure(PlayerUnit playerUnit) {
        movementNode.reset();
        battleNode.reset();
        selectionNode.reset();

        movementNode.maxSpeed.v = 0.7;
        battleNode.playerUnitPtr.v = playerUnit;

        battleNode.hp.v = 50;
        battleNode.battleAttack.range = 4;
        battleNode.battleAttack.amount = 8;
        battleNode.battleAttack.type = BattleBalance.ATTACK_TYPE_KINETIC;
        battleNode.battleArmor.type = BattleBalance.ARMOR_TYPE_NORMAL;
        battleNode.targetAcquisitionRange.v = battleNode.battleAttack.range + 3;

        float size = 0.95f;
        renderNode.set(0, 0, 0, size, size, 90, Color.WHITE, Animations.ANIMATION_TROOPS_IDLING, 0, 0);
        renderNode.setInterpolated(0, 0);

        onAttackSwingAnim = false;
        battleNode.target.v = null;
        target[0] = null;
    }

    public final VoidFunc<RenderSystem> onDraw = new VoidFunc<RenderSystem>() {
        @Override
        public void apply(RenderSystem system) {
            time += 1;

            // HP Bars:
//            system.drawLine(system.getCameraIndex(renderNode.renderLayer.v),
//                    (float)renderNode.coords.pos.x - 0.5f, (float)renderNode.coords.pos.y + 0.7f,
//                    (float)renderNode.coords.pos.x + 0.5f * (float)(battleNode.hp.v / 50), (float)renderNode.coords.pos.y + 0.7f, 2,
//                    (battleNode.hp.v > 30) ? Color.GREEN : (battleNode.hp.v > 15) ? Color.YELLOW : Color.RED);


            renderNode.color.v = Constants.colorForTeam(battleNode.playerUnitPtr.v.playerNode.playerData.team);
            //renderNode.color.v = Color.argb(10, 255, 255, 255);

            if (selectionNode.isSelected.v == 1) {
                system.defineNewSprite(
                        Animations.ANIMATION_BUTTONS_ACTIVATED,
                        //Animations.ANIMATION_TROOPS_TARGETED,
                        1,
                        (float) renderNode.coords.pos.x,
                        (float) renderNode.coords.pos.y,
                        1,
                        (float) Math.max(1.2, (0.22 * Math.sin(time / 5)) + 1.22f),
                        (float) Math.max(1.2, (0.22 * Math.sin(time / 5)) + 1.22f),
                        time,
                        Color.argb(200, 255, 255, 255),
                        //battleNode.gamer.v.color(),
                        RenderNode.RENDER_LAYER_FOREGROUND
                );
            }
            if (movementNode.hasDestination.v == 1) {
                system.defineNewSprite(
                        Animations.ANIMATION_TROOPS_TARGETED,
                        1,
                        (float) movementNode.destination.x,
                        (float) movementNode.destination.y,
                        0,
                        Math.max(1, (float) (0.3 * Math.sin(time / 15)) + 0.9f),
                        Math.max(1, (float) (0.3 * Math.sin(time / 15)) + 0.9f),
                        time * 2,
                        Constants.colorForTeam(battleNode.playerUnitPtr.v.playerNode.playerData.team),
                        RenderNode.RENDER_LAYER_FOREGROUND
                );
            }

            if (!battleNode.isAlive()) {
                TemporarySprite2dDef tempSprite = system.beginNewTempSprite();

                tempSprite.copy(Animations.ANIMATION_TROOPS_DYING_DEF);
                tempSprite.position.x = battleNode.coords.pos.x;
                tempSprite.position.y = battleNode.coords.pos.y;

                system.endNewTempSprite(tempSprite, 0);
            }

            if (battleNode.battleState.v == BattleNode.BATTLE_STATE_SWINGING && battleNode.hasLivingTarget()) {

                double rx = (target[0].coords.pos.x - battleNode.coords.pos.x);
                double ry =(target[0].coords.pos.y - battleNode.coords.pos.y);

                Sprite2dDef tempSprite = system.defineNewSprite(
                        Animations.ANIMATION_TROOPS_PROJECTILE,
                        0,
                        (float)((0.80 * (battleNode.battleProgress.v / battleNode.battleAttack.swingTime) + 0.1) * rx  + battleNode.coords.pos.x),
                        (float)((0.80 * (battleNode.battleProgress.v / battleNode.battleAttack.swingTime) + 0.1) * ry  + battleNode.coords.pos.y),
                        1,
                        0.4f, 0.4f,
                        (float) Orientation.getDegreesBaseX(rx, ry),
                        Color.argb(240, 255, 255, 255),
                        //battleNode.gamer.v.color(),
                        RenderNode.RENDER_LAYER_FOREGROUND
                        );
            }

            if (onAttackSwingAnim) {
                TemporarySprite2dDef tempSprite = system.beginNewTempSprite();

                tempSprite.copy(Animations.ANIMATION_SMOKE_GUNPOWDER_DEF);
                tempSprite.position.x = (battleNode.coords.pos.x + target[0].coords.pos.x) / 2;
                tempSprite.position.y = (battleNode.coords.pos.y + target[0].coords.pos.y) / 2;
                tempSprite.progress.duration = (float)battleNode.battleAttack.swingTime * 900;
                tempSprite.angle = (float)movementNode.coords.rot.getDegrees();

                system.endNewTempSprite(tempSprite, 0);

                onAttackSwingAnim = false;
            }

            if (formationSheep.hasLeader()) {
//                Sprite2dDef tempSprite = system.drawCompat.spriteAllocator.takeNextWritable();
//                tempSprite.set(Animations.ANIMATION_TRIGGER_FIELDS_EXISTING, 0,
//                        (float)battleNode.coords.pos.x, (float)battleNode.coords.pos.y, 0,
//                        1.5f, 1.5f,
//                        (float)Orientation.getDegrees(
//                                battleNode.coords.pos.x, battleNode.coords.pos.y,
//                                formationSheep.formationDestination.x, formationSheep.formationDestination.y
//                                ),
//                        Color.WHITE, 0);
//
//                tempSprite = system.drawCompat.spriteAllocator.takeNextWritable();
//                tempSprite.set(Animations.ANIMATION_RETICLE_TAP, 0,
//                        (float)formationSheep.formationDestination.x, (float)formationSheep.formationDestination.y, 0,
//                        1.5f, 1.5f,
//                        0,
//                        Color.WHITE, 0);
            }
        }
    };

    public static class PlatoonBattleEffect extends BasicAttackEffect {

        Platoon platoon;

        public PlatoonBattleEffect(Platoon platoon) {
            super(platoon.battleNode);
            this.platoon = platoon;
        }

        @Override
        public void onTargetAcquired(BattleSystem battleSystem) {
            super.onTargetAcquired(battleSystem);
            //renderNode.animationName = Sprite2dDef.ANIMATION_TROOPS_TARGETED;
            //selectedRenderNode.isActive = true;
        }

        @Override
        public void onAttackReady(BattleSystem battleSystem, BattleNode target) {
            super.onAttackReady(battleSystem, target);
            platoon.onAttackSwingAnim = false;
        }

        @Override
        public void onAttackCast(BattleSystem battleSystem, BattleNode target) {
            super.onAttackCast(battleSystem, target);
            platoon.onAttackSwingAnim = false;
        }

        @Override
        public void onAttackSwing(BattleSystem battleSystem, BattleNode target) {
            super.onAttackSwing(battleSystem, target);
            platoon.onAttackSwingAnim = true;
            platoon.target[0] = target;
        }

        @Override
        public void onDie(BattleSystem battleSystem) {
            super.onDie(battleSystem);
        }
    }

//    public static class PlatoonBattleNode extends BattleNode {
//        private Platoon platoon;
//
//        public PlatoonBattleNode(Platoon platoon) {
//            super(platoon);
//            this.platoon = platoon;
//
//            this.battleEffects.add(new PlatoonBattleEffect(platoon));
//
//            this.reset();
//        }
//
//        @Override
//        public void reset() {
//            super.reset();
//            this.hp.v = 50;
//            this.attackRange.v = 4;
//            this.attackDamage.v = 4;
//            this.targetAcquisitionRange.v = this.attackRange.v + 3;
//        }
//    }
}
