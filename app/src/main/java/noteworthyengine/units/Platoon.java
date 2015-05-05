package noteworthyengine.units;

import android.graphics.Color;

import art.Animations;
import noteworthyengine.BattleNode;
import noteworthyengine.BattleSystem;
import noteworthyengine.FieldNode;
import noteworthyengine.FormationNode;
import noteworthyengine.GridNode;
import noteworthyengine.MovementNode;
import noteworthyengine.RenderNode;
import noteworthyengine.RenderSystem;
import noteworthyengine.SeparationNode;
import noteworthyframework.Gamer;
import noteworthyframework.Unit;
import structure.Sprite2dDef;
import structure.TemporarySprite2dDef;
import utils.Orientation;
import utils.VoidFunc;
import utils.VoidFunc2;
import utils.VoidFunc3;

/**
 * Created by eric on 3/7/15.
 */
public class Platoon extends Unit {
    public static final String NAME = "Troopy";

    public GridNode gridNode;
    public MovementNode movementNode;

    public FieldNode fieldNode;
    public BattleNode battleNode;
    public SeparationNode separationNode;
    public FormationNode formationNode;
    public FormationNode.FormationSheep formationSheep;

    public RenderNode renderNode;

    public boolean onAttackSwingAnim = false;
    public BattleNode[] target = new BattleNode[1];

    public Platoon() {
        this.name = NAME;

        movementNode = new MovementNode(this);

        fieldNode = new FieldNode(this);
        fieldNode._fieldAgentNode = new FieldNode.FieldAgentNode(this);

        battleNode = new BattleNode(this);
        battleNode.onTargetAcquired = onTargetAcquired;
        battleNode.onAttackReady = onAttackReady;
        battleNode.onAttackSwing = onAttackSwing;
        battleNode.onAttackCast = onAttackCast;

        renderNode = new RenderNode(this);
        renderNode.onDraw = this.onDraw;

        separationNode = new SeparationNode(this);
        formationNode = new FormationNode(this);

        gridNode = new GridNode(this, separationNode, battleNode);

        formationSheep = new FormationNode.FormationSheep(this);

        reset();
    }

    public void reset() {
        movementNode.maxSpeed.v = 0.7;
        battleNode.hp.v = 50;
        battleNode.attackRange.v = 4.5;
        battleNode.attackDamage.v = 2;
        battleNode.targetAcquisitionRange.v = 16.5;
        battleNode.attackState.v = BattleNode.ATTACK_STATE_READY;

        float size = 0.95f;
        renderNode.set(0, 0, 0, size, size, 90, Color.WHITE, Animations.ANIMATION_TROOPS_IDLING, 0, 0);
        renderNode.setInterpolated(0, 0);

        onAttackSwingAnim = false;
        battleNode.target.v = null;
        target[0] = null;
    }

    public void configure(Gamer gamer) {
        battleNode.gamer.v = gamer;
    }

    public final VoidFunc3<BattleSystem, BattleNode, BattleNode> onTargetAcquired =
        new VoidFunc3<BattleSystem, BattleNode, BattleNode>() {
            @Override
            public void apply(BattleSystem battleSystem, BattleNode battleNode, BattleNode battleNode2) {
                //renderNode.animationName = Sprite2dDef.ANIMATION_TROOPS_TARGETED;
                //selectedRenderNode.isActive = true;
            }
        };

    public final VoidFunc3<BattleSystem, BattleNode, BattleNode> onAttackReady =
            new VoidFunc3<BattleSystem, BattleNode, BattleNode>() {
                @Override
                public void apply(BattleSystem battleSystem, BattleNode battleNode, BattleNode battleNode2) {
                    onAttackSwingAnim = false;
                }
            };

    public final VoidFunc3<BattleSystem, BattleNode, BattleNode> onAttackSwing =
            new VoidFunc3<BattleSystem, BattleNode, BattleNode>() {
                @Override
                public void apply(BattleSystem system, BattleNode node, BattleNode otherNode) {
                    onAttackSwingAnim = true;
                    target[0] = otherNode;
                }
            };

    public final VoidFunc3<BattleSystem, BattleNode, BattleNode> onAttackCast =
            new VoidFunc3<BattleSystem, BattleNode, BattleNode>() {
                @Override
                public void apply(BattleSystem battleSystem, BattleNode battleNode, BattleNode battleNode2) {
                    //BattleNode.INFLICT_DAMAGE_DEFAULT.apply(battleSystem, battleNode2, battleNode, battleNode.attackDamage);
                    battleNode2.inflictDamage.apply(battleSystem, battleNode2, battleNode, battleNode.attackDamage);
                    onAttackSwingAnim = false;
                }
            };

    public final VoidFunc<RenderSystem> onDraw = new VoidFunc<RenderSystem>() {
        @Override
        public void apply(RenderSystem system) {
            renderNode.color.v = Gamer.TeamColors.get(battleNode.gamer.v.team);
            //renderNode.color.v = Color.argb(10, 255, 255, 255);

            if (battleNode.hp.v <= 0) {
                //TemporarySprite2dDef tempSprite = system.defineNewTempSprite(Animations.ANIMATION_TROOPS_DYING_DEF, 0);
                TemporarySprite2dDef tempSprite = system.beginNewTempSprite();

                //system.drawCompat.tempSpritesMemoryPool.fetchMemory();
                tempSprite.copy(Animations.ANIMATION_TROOPS_DYING_DEF);
                tempSprite.position.x = battleNode.coords.pos.x;
                tempSprite.position.y = battleNode.coords.pos.y;

                system.endNewTempSprite(tempSprite, 0);

                //system.drawCompat.drawTemporarySprite(tempSprite);
                //system.drawCompat.tempSpritesMemoryPool.recycleMemory(tempSprite);
            }

            if (battleNode.attackState.v == BattleNode.ATTACK_STATE_SWINGING) {

                double rx = (target[0].coords.pos.x - battleNode.coords.pos.x);
                double ry =(target[0].coords.pos.y - battleNode.coords.pos.y);

                Sprite2dDef tempSprite = system.defineNewSprite(
                        Animations.ANIMATION_TROOPS_PROJECTILE,
                        0,
                        (float)((0.45 * (battleNode.attackProgress.v / battleNode.attackSwingTime.v) + 0.15) * rx  + battleNode.coords.pos.x),
                        (float)((0.45 * (battleNode.attackProgress.v / battleNode.attackSwingTime.v) + 0.15) * ry  + battleNode.coords.pos.y),
                        1,
                        0.39f, 0.39f,
                        (float) Orientation.getDegreesBaseX(rx, ry),
                        Color.argb(120, 255, 255, 255),
                        0
                        );
                //system.drawCompat.spriteAllocator.takeNextWritable();

//                tempSprite.position.x = (0.45 * (battleNode.attackProgress.v / battleNode.attackSwingTime.v) + 0.15) * rx  + battleNode.coords.pos.x;
//                tempSprite.position.y = (0.45 * (battleNode.attackProgress.v / battleNode.attackSwingTime.v) + 0.15) * ry  + battleNode.coords.pos.y;
//                tempSprite.position.z = 1;
//                tempSprite.width = 0.39f;
//                tempSprite.height = 0.39f;
//                tempSprite.angle = (float) Orientation.getDegreesBaseX(rx, ry);
//                tempSprite.isGfxInterpolated = false;
//                tempSprite.animationName = Animations.ANIMATION_TROOPS_PROJECTILE;
//                tempSprite.animationProgress = 0;
//                tempSprite.color = Color.argb(120, 255, 255, 255);
            }

            if (onAttackSwingAnim) {
                //TemporarySprite2dDef tempSprite = system.defineNewTempSprite(Animations.ANIMATION_SMOKE_GUNPOWDER_DEF, 0);
                TemporarySprite2dDef tempSprite = system.beginNewTempSprite();
                //system.drawCompat.tempSpritesMemoryPool.fetchMemory();
                tempSprite.copy(Animations.ANIMATION_SMOKE_GUNPOWDER_DEF);
                tempSprite.position.x = (battleNode.coords.pos.x + target[0].coords.pos.x) / 2;
                tempSprite.position.y = (battleNode.coords.pos.y + target[0].coords.pos.y) / 2;
                tempSprite.progress.duration = (float)battleNode.attackSwingTime.v * 900;
                tempSprite.angle = (float)movementNode.coords.rot.getDegrees();

                system.endNewTempSprite(tempSprite, 0);
                //system.drawCompat.drawTemporarySprite(tempSprite);
                //system.drawCompat.tempSpritesMemoryPool.recycleMemory(tempSprite);

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
}
