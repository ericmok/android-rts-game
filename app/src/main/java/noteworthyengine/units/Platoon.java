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

    public RenderNode renderNode;

    public boolean onAttackSwingAnim = false;
    public BattleNode[] target = new BattleNode[1];

    public Platoon() {
        this.name = NAME;

        movementNode = new MovementNode(this);
        movementNode.maxSpeed.v = 0.8;

        fieldNode = new FieldNode(this);
        fieldNode._fieldAgentNode = new FieldNode.FieldAgentNode(this);

        battleNode = new BattleNode(this);
        battleNode.hp.v = 50;
        battleNode.attackRange.v = 5;
        battleNode.attackDamage.v = 1;
        battleNode.targetAcquisitionRange.v = 6.5;
        battleNode.onTargetAcquired = onTargetAcquired;
        battleNode.onAttackReady = onAttackReady;
        battleNode.onAttackSwing = onAttackSwing;
        battleNode.onAttackCast = onAttackCast;

        renderNode = new RenderNode(this);
        float size = 0.95f;
        renderNode.set(0, 0, 0, size, size, 90, Color.WHITE, Animations.ANIMATION_TROOPS_IDLING, 0, 0);
        renderNode.setInterpolated(0, 0);
        renderNode.onDraw = this.onDraw;

        separationNode = new SeparationNode(this);
        formationNode = new FormationNode(this);

        gridNode = new GridNode(this, separationNode, battleNode);
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

            if (battleNode.hp.v <= 0) {
                TemporarySprite2dDef tempSprite = system.drawCompat.tempSpritesMemoryPool.fetchMemory();
                tempSprite.copy(Animations.ANIMATION_TROOPS_DYING_DEF);
                tempSprite.position.x = battleNode.coords.pos.x;
                tempSprite.position.y = battleNode.coords.pos.y;

                system.drawCompat.drawTemporarySprite(tempSprite);
                system.drawCompat.tempSpritesMemoryPool.recycleMemory(tempSprite);
            }

            if (battleNode.attackState.v == BattleNode.ATTACK_STATE_SWINGING) {
                Sprite2dDef tempSprite = system.drawCompat.spriteAllocator.takeNextWritable();
                double rx = (target[0].coords.pos.x - battleNode.coords.pos.x);
                double ry =(target[0].coords.pos.y - battleNode.coords.pos.y);
                tempSprite.position.x = (0.45 * (battleNode.attackProgress.v / battleNode.attackSwingTime.v) + 0.15) * rx  + battleNode.coords.pos.x;
                tempSprite.position.y = (0.45 * (battleNode.attackProgress.v / battleNode.attackSwingTime.v) + 0.15) * ry  + battleNode.coords.pos.y;
                tempSprite.position.z = 1;
                tempSprite.width = 0.39f;
                tempSprite.height = 0.39f;
                tempSprite.angle = (float) Orientation.getDegreesBaseX(rx, ry);
                tempSprite.isGfxInterpolated = false;
                tempSprite.animationName = Animations.ANIMATION_TROOPS_PROJECTILE;
                tempSprite.animationProgress = 0;
                tempSprite.color = Color.argb(120, 255, 255, 255);
            }

            if (onAttackSwingAnim) {
                TemporarySprite2dDef tempSprite = system.drawCompat.tempSpritesMemoryPool.fetchMemory();
                tempSprite.copy(Animations.ANIMATION_SMOKE_GUNPOWDER_DEF);
                tempSprite.position.x = (battleNode.coords.pos.x + target[0].coords.pos.x) / 2;
                tempSprite.position.y = (battleNode.coords.pos.y + target[0].coords.pos.y) / 2;
                tempSprite.progress.duration = (float)battleNode.attackSwingTime.v * 900;
                tempSprite.angle = (float)movementNode.coords.rot.getDegrees();


                system.drawCompat.drawTemporarySprite(tempSprite);
                system.drawCompat.tempSpritesMemoryPool.recycleMemory(tempSprite);

                onAttackSwingAnim = false;
            }
        }
    };
}
