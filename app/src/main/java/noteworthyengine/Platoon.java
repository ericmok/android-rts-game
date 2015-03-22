package noteworthyengine;

import android.graphics.Color;

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

    GridNode gridNode = new GridNode(this);
    MovementNode movementNode;

    FieldNode fieldNode;
    BattleNode battleNode;
    SeparationNode separationNode;
    FormationNode formationNode;

    RenderNode renderNode;
    RenderNode dyingRenderNode;
    RenderNode selectedRenderNode;

    public boolean onAttackSwingAnim = false;
    public BattleNode[] target = new BattleNode[1];

    public Platoon() {
        this.name = NAME;

        movementNode = new MovementNode(this);
        movementNode.maxSpeed.v = 0.8;

        fieldNode = new FieldNode(this);
        fieldNode._fieldAgentNode = new FieldNode.FieldAgentNode(this);
        //fieldNode._movementNode = movementNode;

        battleNode = new BattleNode(this);
        battleNode.hp.v = 50;
        battleNode.attackRange.v = 4;
        battleNode.targetAcquisitionRange.v = 6.5;
        battleNode.onTargetAcquired = onTargetAcquired;
        battleNode.onAttackReady = onAttackReady;
        battleNode.onAttackSwing = onAttackSwing;
        battleNode.onAttackCast = onAttackCast;

        renderNode = new RenderNode(this);
        float size = Math.random() > 0.5f ? 1f : 0.7f;
        renderNode.animationName = Sprite2dDef.ANIMATION_TROOPS_IDLING;
        renderNode.animationProgress.v = 0;
        renderNode.width.v = size;
        renderNode.height.v = size;
        renderNode.color.v = Color.WHITE;
        renderNode.isGfxInterpolated.v = 1;
        renderNode.onDraw = this.onDraw;

        dyingRenderNode = new RenderNode("dyingRenderNode", this);
        dyingRenderNode.isActive = false;
        dyingRenderNode.animationName = Sprite2dDef.ANIMATION_TROOPS_DYING;
        dyingRenderNode.color.v = Color.WHITE;

        separationNode = new SeparationNode(this);
        formationNode = new FormationNode(this);

        selectedRenderNode = new RenderNode("selectedRenderNode", this);
        selectedRenderNode.animationName = Sprite2dDef.ANIMATION_TROOPS_TARGETED;
        selectedRenderNode.isActive = false;
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
                    BattleNode.INFLICT_DAMAGE_DEFAULT.apply(battleSystem, battleNode2, battleNode, battleNode.attackDamage.v);
                    onAttackSwingAnim = false;
                }
            };

    public final VoidFunc<RenderSystem> onDraw = new VoidFunc<RenderSystem>() {
        @Override
        public void apply(RenderSystem system) {
            renderNode.color.v = Gamer.TeamColors.get(battleNode.gamer.v.team);

            if (battleNode.hp.v <= 0) {
                TemporarySprite2dDef tempSprite = system.drawCompat.tempSpritesMemoryPool.fetchMemory();
                tempSprite.position.x = battleNode.coords.pos.x;
                tempSprite.position.y = battleNode.coords.pos.y;
                tempSprite.position.z = 1;
                tempSprite.width = 1f;
                tempSprite.height = 1f;
                tempSprite.progress.progress = 1;
                tempSprite.progress.duration = 1200;
                tempSprite.isGfxInterpolated = false;
                tempSprite.animationName = Sprite2dDef.ANIMATION_TROOPS_DYING;
                tempSprite.animationProgress = 0;
                tempSprite.color = Color.WHITE;
                tempSprite.angle = 90;

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
                tempSprite.animationName = Sprite2dDef.ANIMATION_TROOPS_PROJECTILE;
                tempSprite.animationProgress = 0;
                tempSprite.color = Color.argb(120, 255, 255, 255);
            }

//            if (onAttackSwingAnim) {
//                TemporarySprite2dDef tempSprite = system.drawCompat.tempSpritesMemoryPool.fetchMemory();
//                //tempSprite.position.x = (battleNode.coords.pos.x + battleNode.coords.rot.x * battleNode.attackRange.v / 2);
//                //tempSprite.position.y = (battleNode.coords.pos.y + battleNode.coords.rot.y * battleNode.attackRange.v / 2);
//                tempSprite.position.x = (battleNode.coords.pos.x + target[0].coords.pos.x) / 2;
//                tempSprite.position.y = (battleNode.coords.pos.y + target[0].coords.pos.y) / 2;
//                tempSprite.position.z = 1;
//                tempSprite.width = 0.3f;
//                tempSprite.height = 0.3f;
//                tempSprite.angle = (float)movementNode.coords.rot.getDegrees();
//                tempSprite.progress.progress = 1;
//                tempSprite.progress.duration = (float)battleNode.attackSwingTime.v * 1000; // should be 1000 but...
//                tempSprite.isGfxInterpolated = false;
//                tempSprite.animationName = Sprite2dDef.ANIMATION_TROOPS_PROJECTILE;
//                tempSprite.animationProgress = 0;
//                tempSprite.color = Color.argb(120, 255, 255, 255);
//
//                system.drawCompat.drawTemporarySprite(tempSprite);
//                system.drawCompat.tempSpritesMemoryPool.recycleMemory(tempSprite);
//
//                onAttackSwingAnim = false;
//            }

            //dyingRenderNode.isActive = true;
        }
    };
}
