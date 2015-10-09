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

    public Cannon(final Gamer gamer) {
        this.name = this.getClass().getSimpleName();

        this.reset();
        this.configure(gamer);

        this.battleNode.onAttackCast = new VoidFunc3<BattleSystem, BattleNode, BattleNode>() {
            @Override
            public void apply(BattleSystem battleSystem, BattleNode battleNode, BattleNode battleNode2) {
                //Missle missle = new Missle(battleNode.gamer.v);
                Missle missle = UnitPool.missles.fetchMemory();
                missle.configure(battleNode.gamer.v, battleNode.coords.pos, battleNode2.coords.pos);
                missle.movementNode.coords.pos.copy(movementNode.coords.pos);
                battleSystem.getBaseEngine().addUnit(missle);
            }
        };

        this.renderNode.onDraw = new VoidFunc<RenderSystem>() {
            @Override
            public void apply(RenderSystem system) {
                renderNode.color.v = Gamer.TeamColors.get(battleNode.gamer.v.team);

                if (battleNode.hp.v <= 0) {
                    //system.drawCompat.tempSpritesMemoryPool.fetchMemory();
                    TemporarySprite2dDef tempSprite = system.beginNewTempSprite(); //system.defineNewTempSprite(Animations.ANIMATION_TROOPS_DYING_DEF, 0);
                    tempSprite.copy(Animations.ANIMATION_TROOPS_DYING_DEF);
                    tempSprite.position.x = battleNode.coords.pos.x;
                    tempSprite.position.y = battleNode.coords.pos.y;
                    system.endNewTempSprite(tempSprite, 0);

                    //system.drawCompat.drawTemporarySprite(tempSprite);
                    //system.drawCompat.tempSpritesMemoryPool.recycleMemory(tempSprite);
                }

                if (battleNode.attackState.v == BattleNode.ATTACK_STATE_SWINGING) {

                    if (battleNode.target.v != null) {
                        double ratio = (battleNode.attackProgress.v / battleNode.attackSwingTime.v);

                        //Sprite2dDef sprite2dDef = system.drawCompat.spriteAllocator.takeNextWritable();
                        //sprite2dDef.set(
                        Sprite2dDef sprite2dDef = system.defineNewSprite(
                                Animations.ANIMATION_TROOPS_SWING, (int)ratio * 100,
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
        super.reset();

        this.movementNode.maxSpeed.v = 0.32;

        this.battleNode.hp.v = 5;
        this.battleNode.attackSwingTime.v = 6;
        this.battleNode.attackCooldown.v = 20;
        this.battleNode.attackDamage.v = 0;
        this.battleNode.attackRange.v = 6;
        this.battleNode.targetAcquisitionRange.v = 18.5;
        this.battleNode.attackState.v = BattleNode.ATTACK_STATE_READY;

        this.renderNode.animationName.v = "Animations/Cannons/Idling";
        this.renderNode.width.v = 1.4f;
        this.renderNode.height.v = 1.4f;
    }
}
