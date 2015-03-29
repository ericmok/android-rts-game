package noteworthyengine.units;

import android.graphics.Color;

import art.Animations;
import noteworthyengine.BattleNode;
import noteworthyengine.BattleSystem;
import noteworthyengine.RenderSystem;
import noteworthyframework.Gamer;
import structure.Sprite2dDef;
import structure.TemporarySprite2dDef;
import utils.VoidFunc;
import utils.VoidFunc3;

/**
 * Created by eric on 3/23/15.
 */
public class Cannon extends Platoon {

    public Cannon(Gamer gamer) {
        this.name = this.getClass().getSimpleName();

        this.movementNode.maxSpeed.v = 0.2;

        this.battleNode.gamer.v = gamer;
        this.battleNode.hp.v = 5;
        this.battleNode.attackSwingTime.v = 10;
        this.battleNode.attackCooldown.v = 10;
        this.battleNode.attackDamage.v = 0;
        this.battleNode.attackRange.v = 7.5;
        this.battleNode.targetAcquisitionRange.v = 8.5;
        this.battleNode.onAttackCast = new VoidFunc3<BattleSystem, BattleNode, BattleNode>() {
            @Override
            public void apply(BattleSystem battleSystem, BattleNode battleNode, BattleNode battleNode2) {
                Missle missle = new Missle(battleNode.gamer.v);
                missle.movementNode.coords.pos.copy(movementNode.coords.pos);
                battleSystem.getBaseEngine().addUnit(missle);
            }
        };

        this.renderNode.animationName.v = "Animations/Cannons/Idling";
        this.renderNode.width.v = 1.4f;
        this.renderNode.height.v = 1.4f;
        this.renderNode.onDraw = new VoidFunc<RenderSystem>() {
            @Override
            public void apply(RenderSystem system) {
                renderNode.color.v = Gamer.TeamColors.get(battleNode.gamer.v.team);

                if (battleNode.hp.v <= 0) {
                    TemporarySprite2dDef tempSprite = system.drawCompat.tempSpritesMemoryPool.fetchMemory();
                    tempSprite.copy(Animations.ANIMATION_TROOPS_DYING_DEF);
                    tempSprite.position.x = battleNode.coords.pos.x;
                    tempSprite.position.y = battleNode.coords.pos.y;

//                    tempSprite.position.z = 1;
//                    tempSprite.width = 1f;
//                    tempSprite.height = 1f;
//                    tempSprite.progress.progress = 1;
//                    tempSprite.progress.duration = 1200;
//                    tempSprite.isGfxInterpolated = false;
//                    tempSprite.animationName = Animations.ANIMATION_TROOPS_DYING;
//                    tempSprite.animationProgress = 0;
//                    tempSprite.color = Color.WHITE;
//                    tempSprite.angle = 90;

                    system.drawCompat.drawTemporarySprite(tempSprite);
                    system.drawCompat.tempSpritesMemoryPool.recycleMemory(tempSprite);
                }

                if (battleNode.attackState.v == BattleNode.ATTACK_STATE_SWINGING) {

                    if (battleNode.target[0] != null) {
                        double ratio = (battleNode.attackProgress.v / battleNode.attackSwingTime.v);

                        Sprite2dDef sprite2dDef = system.drawCompat.spriteAllocator.takeNextWritable();
                        sprite2dDef.set(Animations.ANIMATION_TROOPS_SWING, (int)ratio * 100,
                                (float)battleNode.coords.pos.x, (float)battleNode.coords.pos.y - renderNode.height.v, 0,
                                1f, 1f,
                                90,
                                Gamer.colorForTeam(battleNode.gamer.v.team), 0
                                );
//                        sprite2dDef.isGfxInterpolated = false;
//                        sprite2dDef.position.x = ratio * (battleNode.target[0].coords.pos.x - battleNode.coords.pos.x) + battleNode.coords.pos.x;
//                        sprite2dDef.position.y = ratio * (battleNode.target[0].coords.pos.y - battleNode.coords.pos.y) + battleNode.coords.pos.y;
//                        sprite2dDef.position.z = 1;
//                        sprite2dDef.animationName = Animations.ANIMATION_PROJECTILE_BASIC;
//                        sprite2dDef.animationProgress = 1;
//                        sprite2dDef.color = renderNode.color.v;
//                        sprite2dDef.angle = (float) Orientation.getDegreesBaseX(battleNode.target[0].coords.pos.x - battleNode.coords.pos.x,
//                                battleNode.target[0].coords.pos.y - battleNode.coords.pos.y);
//                        sprite2dDef.width = 0.9f;
//                        sprite2dDef.height = 0.9f;
                    }
                }
            }
        };
    }
}